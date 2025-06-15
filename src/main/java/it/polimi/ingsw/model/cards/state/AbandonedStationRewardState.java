package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.AbandonedStationCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.ContainerMoveValidationVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCargoRewardCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;
import it.polimi.ingsw.model.components.StorageComponent;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a Reward State of the {@link AbandonedStationCard}.
 */
public class AbandonedStationRewardState extends CardState {

	private final AbandonedStationCard card;
	private final ArrayList<Player> list;
	private int left;
	private boolean responded = false;

	/**
	 * Constructs a new {@code AbandonedStationRewardState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card  {@link AbandonedStationCard} The card being played.
	 * @param list  List of {@link Player} players in order of distance.
	 * @throws IllegalArgumentException if the list is empty or too large
	 */
	public AbandonedStationRewardState(VoyageState state, AbandonedStationCard card, List<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.left = this.card.getPlanet().getTotalContains();
		this.list = new ArrayList<>(list);
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Abandoned Station Announce State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates and resolves the {@link Player}'s response to the reward.
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCargoRewardCardStateDecorator(new ClientBaseCardState(
				this.getClass().getSimpleName(),
				this.card.getId()),
				this.list.getFirst().getColor(),
				0, this.card.getPlanet().getContains());
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} attempts to take cargo.
	 *
	 * @param p {@link Player} The player doing this action
	 * @param type {@link ShipmentType} The cargo type.
	 * @param target_coords {@link ShipCoords} The coordinates of the {@link StorageComponent}.
	 */
	@Override
	public void takeCargo(Player p, ShipmentType type, ShipCoords target_coords) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to take cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to take cargo during another player's turn!"));
			return;
		}
		if (type.getValue() < 0) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to take cargo with an illegal shipment type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "'  attempted to take cargo with an illegal shipment type!"));
			return;
		}
		if (this.card.getPlanet().getContains()[type.getValue() - 1] <= 0) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to take cargo the card doesn't have!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "'  attempted to take cargo the card doesn't have!"));
			return;
		}
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(p.getSpaceShip(), type);
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
			this.card.getPlanet().getContains()[type.getValue() - 1]--;
			this.left--;
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' took cargo type: " + type + ", placed it at " + target_coords);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to position cargo in illegal coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in illegal coordinates!"));
			return;
		} catch (ContainerFullException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to position cargo in a storage that's full!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in a storage that's full!"));
			return;
		} catch (ContainerNotSpecialException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to position cargo in a storage that doesn't support it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in a storage that doesn't support it!"));
			return;
		}
		for (int i : this.card.getPlanet().getContains()) {
			if (i > 0) return;
		}
		if (left == 0) this.responded = true;
	}

	/**
	 * Called when a {@link Player} attempts to move cargo.
	 *
	 * @param p {@link Player} The player
	 * @param type {@link ShipmentType} The cargo type
	 * @param target_coords {@link ShipCoords} the coordinates of the target
	 * @param source_coords {@link ShipCoords} the coordinates of the source
	 */
	@Override
	public void moveCargo(Player p, ShipmentType type, ShipCoords target_coords, ShipCoords source_coords) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to move cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to move cargo during another player's turn!"));
			return;
		}
		if (type.getValue() <= 0) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to move an invalid shipment type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to move an invalid shipment type!"));
			return;
		}
		ContainerMoveValidationVisitor v = new ContainerMoveValidationVisitor(type);
		try {
			p.getSpaceShip().getComponent(source_coords).check(v);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to move cargo from an invalid source!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to move cargo from an invalid source!"));
			return;
		}
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to move cargo to coords that can't contain the shipment!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to move cargo to coords that can't contain the shipment!"));
			return;
		}
		ContainsRemoveVisitor vr = new ContainsRemoveVisitor(p.getSpaceShip(), type);
		ContainsLoaderVisitor vl = new ContainsLoaderVisitor(p.getSpaceShip(), type);
		p.getSpaceShip().getComponent(source_coords).check(vr);
		p.getSpaceShip().getComponent(target_coords).check(vl);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' moved cargo type: " + type + ", from " + source_coords + " to " + target_coords);
	}

	/**
	 * Called when a {@link Player} tries to discard cargo.
	 *
	 * @param p {@link Player} The player
	 * @param type {@link ShipmentType} The cargo type
	 * @param target_coords {@link ShipCoords} the coordinates of the target
	 */
	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (type.getValue() <= 0) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo with an invalid type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo with an invalid type!"));
			return;
		}
		ContainsRemoveVisitor v = new ContainsRemoveVisitor(p.getSpaceShip(), type);
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' removed cargo type: " + type + " from " + target_coords);
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
		} catch (ContainerEmptyException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to discard cargo from an empty storage component!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from an empty storage component!"));
		}
	}

	/**
	 * Called when a {@link Player} attempts to progress their turn.
	 *
	 * @param p {@link Player} The player
	 */
	@Override
	public void progressTurn(Player p) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		this.responded = true;
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) this.transition();

	}

}
