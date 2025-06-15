package it.polimi.ingsw.model.state;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.CommonBoard;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract class representing the shared construction phase logic between rulesets.
 */
public abstract class ConstructionState extends GameState {

	protected final iCommonBoard board;
	protected final iCards voyage_deck;
	protected final ArrayList<Player> building;
	protected final ArrayList<Player> finished;
	protected final HashMap<Player, BaseComponent> current_tile;
	protected final HashMap<Player, ArrayList<BaseComponent>> hoarded_tile;

	/**
	 * Constructs a {@link ConstructionState} object.
	 * 
	 * @param model {@link ModelInstance} ModelInstance that owns this {@link GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link PlayerCount} Size of the match.
	 * @param players Array of all {@link Player players} in the match.
	 * @param deck {@link iCards} Deck of cards used during the remainder of the match.
	 */
	public ConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, iCards deck) {
		super(model, type, count, players);
		this.board = new CommonBoard();
		this.voyage_deck = deck;
		this.finished = new ArrayList<>();
		this.building = new ArrayList<>();
		this.building.addAll(this.players);
		this.current_tile = new HashMap<>();
		this.hoarded_tile = new HashMap<>();
		for (Player p : this.players) {
			this.current_tile.put(p, null);
			this.hoarded_tile.put(p, new ArrayList<>());
		}
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
	 * Checks if all {@link Player players} have either finished constructing or have disconnected.
	 * Removes a credit for each unused {@link BaseComponent} from each {@link Player}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		boolean only_disconnected_left = true;
		for (Player p : this.players) {
			if (!p.getDisconnected() && this.building.contains(p)) {
				only_disconnected_left = false;
				break;
			}
		}
		if (this.finished.size() != this.players.size() && !only_disconnected_left) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			this.model.serialize();
			return;
		}
		for(Player p : this.players){
			if(this.current_tile.get(p)!=null) p.giveCredits(-1);
			p.giveCredits(-this.hoarded_tile.get(p).size());
		}
		this.transition();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameState getNext() {
		return new VerifyState(model, type, count, players, voyage_deck, finished);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract ClientState getClientState();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean toSerialize() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!"));
			return;
		}
		this.building.remove(p);
		this.finished.addLast(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putComponent(Player p, int id, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!"));
			return;
		}
		ArrayList<Integer> holding = new ArrayList<>();
		boolean hoarded = false;
		if(this.current_tile.get(p)!= null) holding.add(this.current_tile.get(p).getID());
		for(var c : this.hoarded_tile.get(p)) holding.add(c.getID());
		if(!holding.contains(id)){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but they don't have it!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but they don't have it!"));
			return;
		}
		BaseComponent c = null;
		if(this.current_tile.get(p)!=null && this.current_tile.get(p).getID()==id){
			c = this.current_tile.get(p);
		} else {
			c = this.hoarded_tile.get(p).stream().filter(cm->cm.getID()==id).findAny().orElse(null);
			hoarded = true;
		}
		try {
			var tmp = c;
			c.rotate(rotation);
			p.getSpaceShip().addComponent(c, new ShipCoords(type, coords.x, coords.y));
			if(hoarded) this.hoarded_tile.get(p).remove(tmp);
			else this.current_tile.remove(p);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' placed a component in " + coords);
		} catch (OutOfBoundsException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!"));
			return;
		} catch (IllegalComponentAdd e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!"));
			return;
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!"));
			return;
		} catch (NullPointerException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but it's null!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but it's null!"));
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!"));
			return;
		}
		if(this.current_tile.get(p)!=null){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but they are holding one!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but they are holding one!"));
			return;
		}
		BaseComponent c = this.board.pullComponent();
		if (c == null) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but there are no more to take!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but there are no more to take!"));
			return;
		} else {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' took component " + c.getID() + " from the covered pile!");
			this.current_tile.put(p, c);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!"));
			return;
		}
		if(this.current_tile.get(p)!=null){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but they are holding one!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but they are holding one!"));
			return;
		}
		BaseComponent c = null;
		try {
			c = this.board.pullDiscarded(id);
		} catch (ContainerEmptyException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' took component: " + c.getID() + " from the discarded pile!");
		this.broadcastMessage(new ViewMessage("[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' took component: " + c.getID() + " from the discarded pile!"));
		this.current_tile.put(p, c);
	}

	/**
	 * {@inheritDoc}
	 */
	public void reserveComponent(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to reserve a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to reserve a component, but their ship is already confirmed!"));
			return;
		}
		if(this.current_tile.get(p)==null){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to reserve a component, but they aren't holding any!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to reserve a component, but they aren't holding any!"));
			return;
		}
		if(this.hoarded_tile.get(p).size()>=2){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to reserve a component, but their stash is full!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to reserve a component, but their stash is full!"));
			return;
		}
		var c = this.current_tile.get(p);
		this.current_tile.put(p, null);
		this.hoarded_tile.get(p).addFirst(c);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' reserved component: " + c.getID() + "!");
		this.broadcastMessage(new ViewMessage("[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' reserved component: " + c.getID() + "!"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void discardComponent(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!"));
			return;
		}
		if(this.current_tile.get(p)==null){
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a component, but they aren't holding any!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to discard a component, but they aren't holding any!"));
			return;
		}
		var c = this.current_tile.get(p);
		this.current_tile.put(p, null);
		this.board.discardComponent(c);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' discarded component: " + c.getID() + "!");
		this.broadcastMessage(new ViewMessage("[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' discarded component: " + c.getID() + "!"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (!p.getDisconnected()) throw new ForbiddenCallException();
		p.reconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (p.getDisconnected()) throw new ForbiddenCallException();
		p.disconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Construction State";
	}

	public BaseComponent getCurrent(Player p) {
		return this.current_tile.get(p);
	}

	public ArrayList<BaseComponent> getHoarded(Player p) {
		return this.hoarded_tile.get(p);
	}

	public ArrayList<Integer> getDiscarded() {
		return this.board.getDiscarded();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}


}