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
import java.util.Optional;

public abstract class ConstructionState extends GameState {

	protected final iCommonBoard board;
	protected final iCards voyage_deck;
	protected final ArrayList<Player> building;
	protected final ArrayList<Player> finished;
	protected final HashMap<Player, BaseComponent> current_tile;
	protected final HashMap<Player, ArrayList<BaseComponent>> hoarded_tile;

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

	@Override
	public void init() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Construction State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

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
		this.transition();
	}

	@Override
	public GameState getNext() {
		return new VerifyState(model, type, count, players, voyage_deck, finished);
	}

	@Override
	public abstract ClientState getClientState();

	@Override
	public boolean toSerialize() {
		return true;
	}

	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!"));
			return;
		}
		this.building.remove(p);
		this.finished.addLast(p);
		boolean only_disconnected_left = true;
		for (Player other : this.players) {
			if (!other.getDisconnected() && this.building.contains(other)) {
				only_disconnected_left = false;
				break;
			}
		}
		if (only_disconnected_left) this.transition();
	}

	@Override
	public void putComponent(Player p, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!"));
			return;
		}
		if (this.current_tile.get(p) == null) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but they dont have a current one!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but they dont have a current one!"));
			return;
		}
		try {
			this.current_tile.get(p).rotate(rotation);
			p.getSpaceShip().addComponent(this.current_tile.get(p), coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' placed a component in " + coords);
			this.current_tile.put(p, null);
		} catch (OutOfBoundsException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!"));
		} catch (IllegalComponentAdd e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!"));
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!"));
		}
	}

	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!"));
			return;
		}
		BaseComponent tmp = this.board.pullComponent();
		if (tmp == null) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a component, but there are no more to take!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a component, but there are no more to take!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' took component " + tmp.getID() + " from the covered pile!");
		if (this.current_tile.get(p) == null) {
			this.current_tile.put(p, tmp);
		} else {
			BaseComponent old_current = this.current_tile.get(p);
			this.current_tile.put(p, tmp);
			this.hoarded_tile.get(p).addFirst(old_current);
			while (this.hoarded_tile.get(p).size() >= 3) {
				Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Component " + this.hoarded_tile.get(p).getLast().getID() + " added to discarded components.");
				this.board.discardComponent(this.hoarded_tile.get(p).removeLast());
			}
		}
	}

	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!"));
			return;
		}
		BaseComponent tmp = null;
		try {
			tmp = this.board.pullDiscarded(id);
		} catch (ContainerEmptyException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' took component " + tmp.getID() + " from the uncovered pile!");
		BaseComponent oldcurrent = this.current_tile.get(p);
		if (oldcurrent == null) this.current_tile.put(p, tmp);
		else {
			this.current_tile.put(p, tmp);
			this.hoarded_tile.get(p).addFirst(oldcurrent);
		}
		while (this.hoarded_tile.get(p).size() >= 3) {
			this.board.discardComponent(this.hoarded_tile.get(p).removeLast());
		}
	}

	@Override
	public void discardComponent(Player p, int id) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!"));
			return;
		}
		if (this.current_tile.get(p).getID() == id) {
			BaseComponent tmp = this.current_tile.get(p);
			this.current_tile.put(p, null);
			this.board.discardComponent(tmp);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' discarded component " + tmp.getID() + "!");
			return;
		}
		Optional<BaseComponent> c = this.hoarded_tile.get(p).stream().filter(a -> a.getID() == id).findFirst();
		if (c.isPresent()) {
			this.hoarded_tile.get(p).remove(c.get());
			this.board.discardComponent(c.get());
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' discarded component " + c.get().getID() + "!");
		} else {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard a component, but they don't own the one with the id provided!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to discard a component, but they don't own the one with the id provided!"));
		}
	}

	@Override
	public void connect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (!p.getDisconnected()) throw new ForbiddenCallException();
		p.reconnect();
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (p.getDisconnected()) throw new ForbiddenCallException();
		p.disconnect();
	}

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

	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}


}