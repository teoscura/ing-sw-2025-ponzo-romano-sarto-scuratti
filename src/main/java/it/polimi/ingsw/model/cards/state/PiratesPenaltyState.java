package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PiratesCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a Penalty State of the {@link PiratesCard}.
 */
public class PiratesPenaltyState extends CardState {

	private final PiratesCard card;
	private final ArrayList<Player> list;
	private final ArrayList<Projectile> shots;
	private boolean responded = false;

	/**
	 * Constructs a new {@code PiratesPenaltyState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card  {@link PiratesCard} The card being played.
	 * @param list  List of {@link Player} players in order of distance.
	 * @param shots     The projectile array
	 * @throws IllegalArgumentException if the list is empty or too large
	 */
	protected PiratesPenaltyState(VoyageState state, PiratesCard card, ArrayList<Player> list, ArrayList<Projectile> shots) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null || shots == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
		this.shots = shots;
	}

	/**
	 * Called when the card state is initialized.
	 * Resets power for all players ships.
	 *
	 * @param new_state {@link ClientState} The new client state to broadcast to all connected listeners.
	 */
	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Pirates Penalty State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link ServerMessage} and if the front of the remaining player list has motioned to progress, apply the penalty and transition.
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (!this.list.getFirst().getDisconnected()) {
			this.list.getFirst().getSpaceShip().handleShot(this.shots.getFirst());
			if (this.list.getFirst().getSpaceShip().getBlobsSize() <= 0) this.state.loseGame(this.list.getFirst());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		if (list.isEmpty()) return new ClientBaseCardState(
				this.getClass().getSimpleName(),
				card.getId());
		return new ClientProjectileCardStateDecorator(
				new ClientAwaitConfirmCardStateDecorator(
						new ClientBaseCardState(
								this.getClass().getSimpleName(),
								card.getId()),
						new ArrayList<>(List.of(this.list.getFirst().getColor()))),
				this.shots.getFirst());
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} The next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
			return null;
		}
		this.shots.removeFirst();
		if (this.list.getFirst().getSpaceShip().getBlobsSize() > 1)
			return new PiratesSelectShipState(state, card, list, shots);
		if (!this.shots.isEmpty()) return new PiratesPenaltyState(state, card, list, shots);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} attempts to power a component.
	 *
	 * @param p {@link Player} The player
	 * @param target_coords {@link ShipCoords} the component to power
	 * @param battery_coords {@link ShipCoords} the battery to use
	 */
	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.list.getFirst().equals(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	/**
	 * Called when a {@link Player} attempts to progress their turn.
	 *
	 * @param p {@link Player} The player
	 */
	@Override
	public void progressTurn(Player p) {
		if (!this.list.getFirst().equals(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		this.responded = true;
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress!");
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst().equals(p)) {
			this.responded = true;
			return;
		}
		this.list.remove(p);
	}

}
