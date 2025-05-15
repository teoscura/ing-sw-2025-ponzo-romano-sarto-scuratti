package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectShipReconnectState extends CardState {

	private final CardState resume;
	private final Player awaiting;

	public SelectShipReconnectState(VoyageState state, CardState resume, Player awaiting) {
		super(state);
		if (awaiting == null || resume == null) throw new NullPointerException();
		this.resume = resume;
		this.awaiting = awaiting;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		/*XXX*/System.out.println("CardState -> Reconnect New Cabin State for Player '" + awaiting.getUsername() + "'!");

	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.awaiting.getSpaceShip().getBlobsSize() > 1 && !this.awaiting.getDisconnected()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (this.awaiting.getSpaceShip().getCrew()[0] <= 0) this.state.loseGame(awaiting);
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = Collections.singletonList(awaiting.getColor());
		return new ClientNewCenterCardStateDecorator(this.resume.getClientCardState(), new ArrayList<>(tmp));
	}

	@Override
	public CardState getNext() {
		return this.resume;
	}

	@Override
	public void selectBlob(Player p, ShipCoords blob_coord) {
		if (!p.equals(awaiting)) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to set a new center during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set a new center during another player's turn!"));
			return;
		}
		try {
			p.getSpaceShip().selectShipBlob(blob_coord);
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' selected blob that contains coords " + blob_coord + ".");
		} catch (IllegalTargetException e) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to set his new center on an empty space!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center on an empty space!"));
		} catch (ForbiddenCallException e) {
			//Should be unreachable.
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to set his new center while having a unbroken ship!"));
		}
	}

	@Override
	public void disconnect(Player p) {
		return;
	}

}
