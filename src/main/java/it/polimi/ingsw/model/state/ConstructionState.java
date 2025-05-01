package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.CommonBoard;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;

public class ConstructionState extends GameState {

	private final iCommonBoard board;
	private final ArrayList<Integer> construction_cards;
	private final iCards voyage_deck;
	private final ArrayList<Player> building;
	private final ArrayList<Player> finished;
	private final ConstructionStateHourglass hourglass;
	private HashMap<Player, BaseComponent> current_tile;
	private final HashMap<Player, ArrayList<BaseComponent>> hoarded_tile;

	public ConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players) {
		super(model, type, count, players);
		this.board = new CommonBoard();
		this.voyage_deck = type.getLevel() == -1 ? new TestFlightCards() : new LevelTwoCards();
		this.hourglass = type.getLevel() == -1 ? new ConstructionStateHourglass(60, 0) : new ConstructionStateHourglass(60, 4);
		this.construction_cards = new ArrayList<>(this.voyage_deck.getConstructionCards());
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
        System.out.println("New Game State -> Construction State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.finished.size() != this.players.size()) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public GameState getNext() {
		return new VerifyState(model, type, count, players, voyage_deck, finished);
	}

	@Override
	public ClientModelState getClientState() {
		ArrayList<ClientConstructionPlayer> tmp = new ArrayList<>();
		ArrayList<Integer> construction_cards = new ArrayList<>(this.construction_cards);
		ArrayList<Integer> discarded = new ArrayList<>(this.board.getDiscarded());
		for (Player p : this.players) {
			ArrayList<Integer> stash = new ArrayList<>();
			if (this.current_tile.get(p) != null) stash.add(this.current_tile.get(p).getID());
			stash.addAll(this.hoarded_tile.get(p).stream().filter(t -> t != null).map(t -> t.getID()).toList());
			tmp.add(new ClientConstructionPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					stash,
					this.finished.contains(p)));
		}
		return new ClientConstructionState(this.type, tmp, construction_cards, discarded, this.board.getCoveredSize(), this.hourglass.getInstant());
	}

	@Override
	public boolean toSerialize() {
		return true;
	}

	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to confirm his ship again, but it's already confirmed!"));
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
		if (!this.hourglass.started()) {
			this.hourglass.enable();
			this.hourglass.toggle();
		}
		if (only_disconnected_left) this.transition();
	}

	@Override
	public void putComponent(Player p, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but their ship is already confirmed!"));
			return;
		}
		if (!this.hourglass.running()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but the hourglass has run out!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but the hourglass has run out!"));
			return;
		}
		if (this.current_tile.get(p) == null) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but they dont have a current one!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but they dont have a current one!"));
			return;
		}
		try {
			this.current_tile.get(p).rotate(rotation);
			p.getSpaceShip().addComponent(this.current_tile.get(p), coords);
			System.out.println("Player '" + p.getUsername() + "' placed a component in "+coords);
			this.current_tile = null;
		} catch (OutOfBoundsException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are illegal!"));
		} catch (IllegalComponentAdd e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are already occupied!"));
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but the coordinates are not connected to the rest of the ship!"));
		}
	}

	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a component, but their ship is already confirmed!"));
			return;
		}
		if (!this.hourglass.running()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a component, but the hourglass has run out!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a component, but the hourglass has run out!"));
			return;
		}
		BaseComponent tmp = this.board.pullComponent();
		if (tmp == null) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a component, but there are no more to take!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a component, but there are no more to take!"));
			return;
		}
		System.out.println("Player '" + p.getUsername() + "' took component "+tmp.getID()+" from the covered pile!");
		if (this.current_tile.get(p) == null) {
			this.current_tile.put(p, tmp);
		} else {
			BaseComponent old_current = this.current_tile.get(p);
			this.current_tile.put(p, tmp);
			this.hoarded_tile.get(p).addFirst(old_current);
			while (this.hoarded_tile.get(p).size() >= 3) {
				System.out.println("Component "+this.hoarded_tile.get(p).getLast().getID()+" added to discarded components.");
				this.board.discardComponent(this.hoarded_tile.get(p).removeLast());
			}
		}
	}

	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		if (!this.building.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a discarded component, but their ship is already confirmed!"));
			return;
		}
		if (!this.hourglass.running()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has run out!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has run out!"));
			return;
		}
		BaseComponent tmp = null;
		try {
			tmp = this.board.pullDiscarded(id);
		} catch (ContainerEmptyException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a discarded component, but there aren't any with that ID!"));
			return;
		}
		System.out.println("Player '" + p.getUsername() + "' took component "+tmp.getID()+" from the uncovered pile!");
		BaseComponent oldcurrent = this.current_tile.get(p);
		if(oldcurrent==null) this.current_tile.put(p, tmp);
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
			System.out.println("Player '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to discard a component, but their ship is already confirmed!"));
			return;
		}
		if (!this.hourglass.running()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard a component, but the hourglass has run out!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to discard a component, but the hourglass has run out!"));
			return;
		}
		if (this.current_tile.get(p).getID() == id) {
			BaseComponent tmp = this.current_tile.get(p);
			this.current_tile.put(p, null);
			this.board.discardComponent(tmp);
			System.out.println("Player '" + p.getUsername() + "' discarded component "+tmp.getID()+"!");
			return;
		}
		Optional<BaseComponent> c = this.hoarded_tile.get(p).stream().filter(a -> a.getID() == id).findFirst();
		if (c.isPresent()) {
			this.hoarded_tile.get(p).remove(c.get());
			this.board.discardComponent(c.get());
			System.out.println("Player '" + p.getUsername() + "' discarded component "+c.get().getID()+"!");
		} else {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard a component, but they don't own the one with the id provided!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to discard a component, but they don't own the one with the id provided!"));
		}
	}

	@Override
	public void toggleHourglass(Player p) throws ForbiddenCallException {
		if (this.hourglass.running()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but it's still running!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but it's still running!"));
			return;
		}
		if (this.building.contains(p)) {
			System.out.println("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but they haven't finished building their ship!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but they haven't finished building their ship!"));
			return;
		}
		try {
			this.hourglass.toggle();
		} catch (ForbiddenCallException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but either nobody finished or it has run out!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but either nobody finished or it has run out!"));
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
		this.model.kick(p.getDescriptor());
	}

	@Override
	public String toString() {
		String res = "";
		res.concat("Construction State - ");
		for (Player p : this.players) {
			res.concat(p.getUsername() + ": " + p.getColor().toString() + ", ");
		}
		return res;
	}

	public BaseComponent getCurrent(Player p){
		return this.current_tile.get(p);
	}

	public ArrayList<BaseComponent> getHoarded(Player p){
		return this.hoarded_tile.get(p);
	}

	public ArrayList<Integer> getDiscarded(){
		return this.board.getDiscarded();
	}

}