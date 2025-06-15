package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the selection of a ship section when a {@link it.polimi.ingsw.model.player.Player} reconnects with a broken ship but not being retired.
 */
public class SelectShipReconnectState extends CardState {

	private final CardState resume;
	private final Player awaiting;

	/**
	 * Constructs a {@link SelectShipReconnectState} object.
	 * 
	 * @param state {@link it.polimi.ingsw.model.state.VoyageState} VoyageState that owns this {@link CardState}.
	 * @param resume {@link CardState} Card state to resume after everyone selected their ship section.
	 * @param awaiting {@link it.polimi.ingsw.model.player.Player} Player that reconnected with a broken ship.
	 */
	public SelectShipReconnectState(VoyageState state, CardState resume, Player awaiting) {
		super(state);
		if (awaiting == null || resume == null) throw new NullPointerException();
		this.resume = resume;
		this.awaiting = awaiting;
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Reconnect New Cabin State for Player '" + awaiting.getUsername() + "'!");

	}

	/**
	 * Validates the {@link it.polimi.ingsw.message.server.ServerMessage} and transitions if the player has set the blob or disconnected.
	 *
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		for(var p : this.state.getOrder(CardOrder.NORMAL)){
			if (p.getSpaceShip().getBlobsSize() > 1 && !p.getDisconnected()) {
				this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
				return;
			}
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = List.of(awaiting.getColor());
		return new ClientNewCenterCardStateDecorator(this.resume.getClientCardState(), new ArrayList<>(tmp));
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		return this.resume;
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} tries to select a ship blob center.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player
	 * @param blob_coord {@link it.polimi.ingsw.model.player.ShipCoords} The coordinates selected
	 */
	@Override
	public void selectBlob(Player p, ShipCoords blob_coord) {
		if (!p.equals(awaiting)) {
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

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} disconnects.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player disconnecting.
	 */
	@Override
	public void disconnect(Player p) {
	}

}
