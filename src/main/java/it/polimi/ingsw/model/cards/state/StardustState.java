package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.OpenSpaceCard;
import it.polimi.ingsw.model.cards.StardustCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Class representing a State of the {@link StardustCard}.
 */
public class StardustState extends CardState {

	private final StardustCard card;
	private final ArrayList<Player> awaiting;

	/**
	 * Constructs a new {@code StardustState}.
	 *
	 * @param state {@link it.polimi.ingsw.model.state.VoyageState} The current voyage state
	 * @param card  {@link StardustCard} The card being played.
	 */
	public StardustState(VoyageState state, StardustCard card) {
		super(state);
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.awaiting = new ArrayList<>(state.getOrder(CardOrder.NORMAL));
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Stardust State!");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link it.polimi.ingsw.message.server.ServerMessage} and if everyone motioned to progress, applies the {@link OpenSpaceCard} effect.
	 *
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		for (Player p : this.state.getOrder(CardOrder.INVERSE)) {
			card.apply(this.state, p);
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		ArrayList<PlayerColor> tmp = new ArrayList<>(this.awaiting.stream().map(p -> p.getColor()).toList());
		return new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(
						this.getClass().getSimpleName(),
						card.getId()),
				tmp);
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} attempts to progress their turn.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player
	 */
	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress!");
		this.awaiting.remove(p);
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} disconnects.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);
	}

}
