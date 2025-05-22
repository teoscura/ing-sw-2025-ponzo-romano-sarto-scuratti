package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PiratesCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

public class PiratesSelectShipState extends CardState {

	private final PiratesCard card;
	private final ArrayList<Player> list;
	private final ProjectileArray shots;

	public PiratesSelectShipState(VoyageState state, PiratesCard card, ArrayList<Player> list, ProjectileArray shots) {
		super(state);
		if (card == null || list == null || shots == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
		this.shots = shots;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Pirates Select Ship State!");
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Awaiting: '" + this.list.getFirst().getUsername() + "'.");
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.list.getFirst().getSpaceShip().getBlobsSize() > 1 && !this.list.getFirst().getDisconnected()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (this.list.getFirst().getSpaceShip().getCrew()[0] <= 0) this.state.loseGame(this.list.getFirst());
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientNewCenterCardStateDecorator(new ClientBaseCardState(
				this.getClass().getSimpleName(),
				card.getId()), 
			new ArrayList<>(List.of(this.list.getFirst().getColor())));
	}

	@Override
	public CardState getNext() {
		if (this.list.getFirst().getRetired()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
			return null;
		}
		if (!this.shots.getProjectiles().isEmpty()) {
			this.shots.getProjectiles().removeFirst();
			return new PiratesPenaltyState(state, card, list, shots);
		}
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void selectBlob(Player p, ShipCoords blob_coord) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set a new center during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set a new center during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().selectShipBlob(blob_coord);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' selected blob that contains coords " + blob_coord + ".");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center on an empty space!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center on an empty space!"));
		} catch (ForbiddenCallException e) {
			//Should be unreachable.
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
		}
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (!this.list.getFirst().equals(p)) {
			this.list.remove(p);
		}

	}

}
