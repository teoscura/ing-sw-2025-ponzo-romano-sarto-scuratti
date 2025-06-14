package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Special subclass of {@link WaitingState} needed to restart a saved game, 
 * only allows {@link ClientDescriptor clients} with the same names as the {@link Player players} that were playing before the {@link ModelInstance} that owned it got closed. 
 *
 * {@inheritDoc}
 */
public class ResumeWaitingState extends WaitingState {

	private final GameState next;
	private final HashMap<String, ClientDescriptor> awaiting;
	private final PlayerCount count;

	/**
	 * Constructs a {@link ResumeWaitingState} object.
	 *
	 * @param model {@link ModelInstance} ModelInstance that owns this {@link GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link PlayerCount} Size of the match.
	 * @param next {@link GameState} State the saved game stopped at.
	 */
	public ResumeWaitingState(ModelInstance model, GameModeType type, PlayerCount count, GameState next) {
		super(model, type, count);
		if (next == null) throw new NullPointerException();
		this.next = next;
		this.awaiting = new HashMap<>();
		for (String s : this.next.players.stream().map(p -> p.getUsername()).toList()) {
			this.awaiting.put(s, null);
		}
		this.count = count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Resume Waiting Room State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	/**
	 * Checks that all the {@link Player players} previously playing have joined, and if so, transitions.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		int here = 0;
		for (String username : this.next.players.stream().map(p -> p.getUsername()).toList()) {
			if (this.awaiting.get(username) == null) continue;
			here++;
		}
		if (here == 0) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Everyone left the room, closing it!");
			this.model.endGame();
			return;
		}
		if (here < this.next.getCount().getNumber()) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		for (Player p : this.next.players) {
			this.awaiting.get(p.getUsername()).bindPlayer(p);
		}
		this.transition();
	}

	/**
	 * Returns the {@link GameState} the match stopped at.
	 */
	@Override
	public GameState getNext() {
		this.model.startGame();
		return this.next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientState getClientState() {
		ArrayList<ClientWaitingPlayer> tmp = new ArrayList<>();
		List<String> tmp2 = this.next.players.stream()
				.sorted((p1, p2) -> p1.getColor().getOrder() > p2.getColor().getOrder() ? 1 : -1)
				.map(p -> p.getUsername()).toList();
		for (PlayerColor c : PlayerColor.values()) {
			if (c.getOrder() + 1 > this.count.getNumber()) break;
			if (c.getOrder() < 0) continue;
			tmp.add(new ClientWaitingPlayer(tmp2.get(c.getOrder()), this.awaiting.get(tmp2.get(c.getOrder())) == null ? PlayerColor.NONE : c));
		}
		return new ClientWaitingRoomState(type, count, tmp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean toSerialize() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		if (!this.awaiting.containsKey(client.getUsername())) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' attempted to connect to a resuming game, but he wasn't playing in it before!");
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to connect to a resuming game, but he wasn't playing in it before!"));
			return;
		}
		if (this.awaiting.get(client.getUsername()) != null) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' attempted to connect to a resuming game, but someone already took that username's place!");
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to connect to a resuming game, but someone already took that username's place!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' connected!");
		this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' connected!"));
		this.awaiting.put(client.getUsername(), client);
	}

	/**
	 * {@inheritDoc}
	 */
	public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
		if (!this.awaiting.containsKey(client.getUsername())) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' attempted to disconnect from a connection that isn't connected!");
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to disconnect from a connection that isn't connected!"));
			return;
		}
		if (this.awaiting.get(client.getUsername()) == null) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' attempted to disconnect from a connection that isn't connected!");
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to disconnect from a connection that isn't connected!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Client '" + client.getUsername() + "' disconnected!");
		this.awaiting.put(client.getUsername(), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Resume Waiting State";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), awaiting.keySet().stream().toList(), id);
	}

}
