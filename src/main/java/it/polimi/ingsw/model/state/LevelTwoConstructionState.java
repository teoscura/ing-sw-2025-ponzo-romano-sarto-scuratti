package it.polimi.ingsw.model.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Represents a the construction phase of a Galaxy Trucker match, using level two flight rules, includes a {@link ConstructionStateHourglass}.
 */
public class LevelTwoConstructionState extends ConstructionState {

	private final ConstructionStateHourglass hourglass;

	/**
	 * Constructs a {@link LevelTwoConstructionState} object.
	 * 
	 * @param model {@link ModelInstance} ModelInstance that owns this {@link GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link PlayerCount} Size of the match.
	 * @param players Array of all {@link Player players} in the match.
	 * @param seconds Amount of seconds the {@link ConstructionStateHourglass} lasts.
	 */
	public LevelTwoConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, int seconds) {
		super(model, type, count, players, new LevelTwoCards());
		this.hourglass = new ConstructionStateHourglass(seconds, 3);
		this.hourglass.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Construction State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientState getClientState() {
		ArrayList<ClientConstructionPlayer> tmp = new ArrayList<>();
		ArrayList<Integer> discarded = new ArrayList<>(this.board.getDiscarded());
		for (Player p : this.players) {
			int id = this.current_tile.get(p) == null ? -1 : this.current_tile.get(p).getID();
			ArrayList<Integer> stash = new ArrayList<>();
			stash.addAll(this.hoarded_tile.get(p).stream().filter(t -> t != null).map(t -> t.getID()).toList());
			tmp.add(new ClientConstructionPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					id,
					stash,
					this.finished.contains(p),
					p.getDisconnected()));
		}
		return new ClientConstructionState(this.type, tmp, new ArrayList<>(this.voyage_deck.getConstructionCards()), discarded, this.board.getCoveredSize(), this.hourglass.timesTotal(), this.hourglass.timesLeft(), this.hourglass.getDuration(), this.hourglass.getInstant());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		super.sendContinue(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putComponent(Player p, int id, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		if (!hourglass.canAct()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the hourglass has ran out on the last space!"));
			return;
		}
		super.putComponent(p, id, coords, rotation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
		if (!hourglass.canAct()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but the hourglass has ran out on the last space!"));
			return;
		}
		super.takeComponent(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		if (!hourglass.canAct()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has ran out on the last space!"));
			return;
		}
		super.takeDiscarded(p, id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void discardComponent(Player p) throws ForbiddenCallException {
		if (!hourglass.canAct()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to discard a component, but the hourglass has ran out on the last space!"));
			return;
		}
		super.discardComponent(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void toggleHourglass(Player p) {
		if (hourglass.isRunning()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to move and toggle the hourglass, but sand is still falling!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to move and toggle the hourglass, but sand is still falling!"));
			return;
		}
		if (hourglass.timesLeft() > 1) {
			try {
				hourglass.toggle();
				Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: " + hourglass.timesLeft() + ")");
				this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: " + hourglass.timesLeft() + ")"));
				return;
			} catch (ForbiddenCallException e) {
				//Checks were done beforehand, should never happen. Getting here is gamebreaking.
				System.exit(-1);
			}
		}
		if (this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to move and toggle the hourglass to the last slot, but they're still building!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to move and toggle the hourglass to the last slot, but they're still building!"));
			return;
		}
		try {
			hourglass.toggle();
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: " + hourglass.timesLeft() + ")");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: " + hourglass.timesLeft() + ")"));
		} catch (ForbiddenCallException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to toggle the hourglass, but it's already run out on the last slot!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to toggle the hourglass, but it's already run out on the last slot!"));
		}
	}

}
