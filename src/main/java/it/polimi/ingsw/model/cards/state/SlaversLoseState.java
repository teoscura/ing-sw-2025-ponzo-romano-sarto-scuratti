package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SlaversCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
/**
 * Class representing the Lose State of the {@link SlaversCard}.
 */
public class SlaversLoseState extends CardState {

	private final SlaversCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private int done = 0;

	/**
	 * Construct a {@link SlaversLoseState} object.
	 * 
	 * @param state {@link VoyageState} The current voyage state
	 * @param card  {@link SlaversCard} The card being played.
	 * @param list  List of {@link Player} players in order of distance.
	 */
	protected SlaversLoseState(VoyageState state, SlaversCard card, ArrayList<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Slavers Lose State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link ServerMessage} and if the front of the remaining player list has removed the necessary crew, transition.
	 *
	 * @param message {@link ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded && !this.list.getFirst().getRetired()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCrewPenaltyCardStateDecorator(
				new ClientBaseCardState(
						this.getClass().getSimpleName(),
						card.getId()),
				this.list.getFirst().getColor(),
				this.card.getCrewLost() - this.done);
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new SlaversAnnounceState(state, card, list);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
			return null;
		}
		this.list.removeFirst();
		if (!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} tries to remove crew from a cabin.
	 *
	 * @param p {@link Player} The player
	 * @param cabin_coords {@link ShipCoords} the coordinates of the cabin
	 */
	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove a crew member during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member during another player's turn!"));
			return;
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
			this.done++;
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' removed a crewmate from " + cabin_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!"));
			return;
		}
		if (this.done >= this.card.getCrewLost() || p.getSpaceShip().getCrew()[0] == 0) {
			this.responded = true;
		}
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {

			this.responded = true;
			return;
		}
		this.list.remove(p);

	}

}
