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

public class WaitingState extends GameState {

	private final ArrayList<ClientDescriptor> connected;
	private final PlayerCount count;

	public WaitingState(ModelInstance model, GameModeType type, PlayerCount count) {
		super(model, type, count, null);
		this.connected = new ArrayList<>();
		this.count = count;
	}

	@Override
	public void init() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Waiting Room State!");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.connected.size() == 0) {
			this.model.endGame();
			return;
		}
		if (this.connected.size() < count.getNumber()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Missing " + (this.count.getNumber() - this.connected.size()) + " players to start the game!");
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public GameState getNext() {
		if (this.connected.size() == 0) return null;
		ArrayList<Player> playerlist = new ArrayList<>();
		for (PlayerColor c : PlayerColor.values()) {
			if (c.getOrder() < 0) continue;
			if (c.getOrder() >= this.count.getNumber()) continue;
			String username = this.connected.get(c.getOrder()).getUsername();
			playerlist.addLast(new Player(this.type, username, c));
			try {
				this.connected.get(c.getOrder()).bindPlayer(playerlist.get(c.getOrder()));
			} catch (Exception e) {
				//Unreachable, but needed by compiler.
				e.printStackTrace();
			}
		}
		this.model.startGame();
		if (type.getLevel() > 1) return new LevelTwoConstructionState(model, type, count, playerlist, 90);
		return new TestFlightConstructionState(model, type, count, playerlist);
	}

	@Override
	public ClientState getClientState() {
		ArrayList<ClientWaitingPlayer> tmp = new ArrayList<>();
		for (PlayerColor c : PlayerColor.values()) {
			if (c.getOrder() < tmp.size()) {
				if (c.getOrder() + 1 > this.count.getNumber()) break;
				if (c.getOrder() < 0) continue;
				tmp.add(new ClientWaitingPlayer(this.connected.get(c.getOrder()).getUsername(), c));
			}
		}
		return new ClientWaitingRoomState(type, tmp);
	}

	@Override
	public boolean toSerialize() {
		return false;
	}

	public void connect(ClientDescriptor client) throws ForbiddenCallException {
		if (this.connected.contains(client)) {
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to connect from an already connected connection!"));
			return;
		}
		this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' connected!"));
		this.connected.add(client);
	}

	public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
		if (!this.connected.contains(client)) {
			this.broadcastMessage(new ViewMessage("Client '" + client.getUsername() + "' attempted to disconnect from a connection that isn't connected!"));
			return;
		}
		this.connected.remove(client);
	}

	@Override
	public String toString() {
		return "Waiting State";
	}

	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), connected.stream().map(c -> c.getUsername()).toList(), id);
	}

}
