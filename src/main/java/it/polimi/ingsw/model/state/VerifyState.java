package it.polimi.ingsw.model.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.AlienTypeAlreadyPresentException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CrewSetVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

public class VerifyState extends GameState {

	private final iCards voyage_deck;
	private final ArrayList<Player> to_remove_broken;
	private final ArrayList<Player> to_choose_blob;
	private final ArrayList<Player> finish_order;
	private final ArrayList<Player> awaiting;
	private final ArrayList<Player> starts_losing;

	public VerifyState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, iCards voyage_deck, ArrayList<Player> finish_order) {
		super(model, type, count, players);
		if (voyage_deck == null || finish_order == null || players == null) throw new NullPointerException();
		this.voyage_deck = voyage_deck;
		this.to_remove_broken = new ArrayList<>();
		this.to_choose_blob = new ArrayList<>();
		this.finish_order = finish_order;
		this.awaiting = new ArrayList<>();
		this.awaiting.addAll(players);
		this.starts_losing = new ArrayList<>();
	}

	@Override
	public void init() {
		for (Player p : this.finish_order) {
			if (p.getSpaceShip().bulkVerifyResult() && p.getSpaceShip().getBlobsSize() == 1) continue;
			else if (!p.getSpaceShip().bulkVerifyResult()) this.to_remove_broken.add(p);
			else this.to_choose_blob.add(p);
		}
		for (Player p : this.to_choose_blob) {
			finish_order.remove(p);
		}
		for (Player p : this.to_remove_broken) {
			finish_order.remove(p);
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"New Game State -> Verify State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));

	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		int disconnected = 0;
		for (Player p : this.awaiting) {
			if (p.getDisconnected()) disconnected++;
		}
		if (this.awaiting.size() - disconnected > 0) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			this.model.serialize();
			return;
		}
		this.transition();
	}

	@Override
	public GameState getNext() {
		for (Player p : this.players) {
			if (p.getDisconnected()) this.starts_losing.add(p);
		}
		for (Player p : this.players) {
			if (p.getSpaceShip().getBlobsSize() <= 0 || this.starts_losing.contains(p)) this.finish_order.remove(p);
		}
		Planche planche = new Planche(type, finish_order);
		VoyageState res = new VoyageState(model, type, count, players, voyage_deck, planche);
		for (Player p : this.players) {
			if (p.getSpaceShip().getBlobsSize() <= 0 || this.starts_losing.contains(p)) res.loseGame(p);
		}
		return res;
	}

	@Override
	public ClientState getClientState() {
		ArrayList<ClientVerifyPlayer> tmp = new ArrayList<>();
		for (Player p : this.players) {
			tmp.add(new ClientVerifyPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip().getVerifyShip(p.getSpaceShip().bulkVerify()),
					p.getSpaceShip().bulkVerify(),
					this.finish_order.contains(p),
					this.finish_order.indexOf(p)));
		}
		return new ClientVerifyState(tmp);
	}

	@Override
	public boolean toSerialize() {
		return true;
	}

	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' already finished validating!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' already finished validating!"));
			return;
		}
		if (this.to_choose_blob.contains(p) || this.to_remove_broken.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' motioned to progress without validating and finalizing his ship!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' motioned to progress without validating and finalizing his ship!"));
			return;
		}
		this.awaiting.remove(p);
		Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' motioned to progress! (" + (this.awaiting.size()) + " missing).");
	}

	@Override
	public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
		if (!this.to_remove_broken.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to act on the ship after cleaning it!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to act on the ship after cleaning it!"));
			return;
		}
		try {
			p.getSpaceShip().removeComponent(coords);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' tried to remove an empty component!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove an empty component!"));
			return;
		}
		if (!p.getSpaceShip().bulkVerifyResult()) return;
		this.to_remove_broken.remove(p);
		if (p.getSpaceShip().getBlobsSize() > 1) this.to_choose_blob.add(p);
		else this.finish_order.addLast(p);
	}

	@Override
	public void selectBlob(Player p, ShipCoords blob_coord) {
		if (!this.to_choose_blob.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set a new center during another player's turn!");
			this.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set a new center during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().selectShipBlob(blob_coord);
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' selected blob that contains coords " + blob_coord + ".");
			if (p.getSpaceShip().getCrew()[0] <= 0) this.starts_losing.add(p);
			this.to_choose_blob.remove(p);
			this.finish_order.addLast(p);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to select a nonexistant blob!");
			this.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to select a nonexistant blob!"));
		} catch (ForbiddenCallException e) {
			//Should be unreachable.
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
		}
	}


	@Override
	public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
		if (this.to_choose_blob.contains(p) || this.to_remove_broken.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' tried to set crew type before having a valid ship!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set crew type before having a valid ship!"));
			return;
		}
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' tried to set crew type after finishing!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set crew type after finishing!"));
			return;
		}
		try {
			CrewSetVisitor v = new CrewSetVisitor(p.getSpaceShip(), type);
			p.getSpaceShip().getComponent(coords).check(v);
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' set crew type " + type + " on coords " + coords + "!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' set crew type " + type + " on coords " + coords + "!"));
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set crew on a invalid coordinate!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to set crew on a invalid coordinate!"));
		} catch (IllegalArgumentException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set crew with an invalid alien type!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to set crew with an invalid alien type!"));
		} catch (UnsupportedAlienCabinException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set crew on a cabin that doesn't support the type: '" + type.toString() + "'!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to set crew on a cabin that doesn't support the type: '" + type + "'!"));
		} catch (AlienTypeAlreadyPresentException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "["+model.getID()+"] "+"Player: '" + p.getUsername() + "' attempted to set the type: '" + type.toString() + "' but it's already present, can only have one!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to set the type: '" + type + "' but it's already present, can only have one!"));
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
		return "Verify State";
	}

	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}

}
