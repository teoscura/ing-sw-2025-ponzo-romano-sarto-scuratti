package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.io.Serializable;

public abstract class CardState implements Serializable {

	protected final VoyageState state;

	protected CardState(VoyageState state) {
		if (state == null) throw new NullPointerException();
		this.state = state;
	}

	public void init(ClientState new_state) {
		for (Player p : state.getOrder(CardOrder.NORMAL)) {
			p.getSpaceShip().resetPower();
		}
		this.state.broadcastMessage(new NotifyStateUpdateMessage(new_state));
	}

	public abstract void validate(ServerMessage message) throws ForbiddenCallException;

	public abstract ClientCardState getClientCardState();

	public abstract CardState getNext();

	public void transition() {
		this.state.setCardState(this.getNext());
	}

	public void selectBlob(Player p, ShipCoords blob_coord) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set a new center in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to set a new center in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove crew in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void moveCargo(Player p, ShipmentType shipment, ShipCoords target_coords, ShipCoords source_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to remove crew in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void takeCargo(Player p, ShipmentType type, ShipCoords storage_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take cargo in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to take cargo in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to discard cargo in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to discard cargo in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void selectLanding(Player p, int planet) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to select landing in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to select landing in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void progressTurn(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to progress in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to progress in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void setTakeReward(Player p, boolean take) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to take the reward in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to take the reward in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void connect(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to connect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

	public void disconnect(Player p) throws ForbiddenCallException {
		this.state.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!"));
		/*XXX*/System.out.println("Player: '" + p.getUsername() + "' tried to disconnect in a state that doesn't allow it!");
		throw new ForbiddenCallException("This state doesn't support this function.");
	}

}
