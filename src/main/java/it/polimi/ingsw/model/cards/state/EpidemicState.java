package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.EpidemicCard;
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
import java.util.List;
/**
 * Class representing a State of the {@link EpidemicCard}.
 */
public class EpidemicState extends CardState {

	private final EpidemicCard card;
	private final ArrayList<Player> awaiting;

	/**
	 * Constructs a new {@code AbandonedStationRewardState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card  {@link EpidemicCard} The card being played.
	 */
	public EpidemicState(VoyageState state, EpidemicCard card) {
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Epidemic State!");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link ServerMessage} and if everyone has motioned to progress, applies the {@link EpidemicCard} and trasntions.
	 *
	 * @param message {@link ServerMessage} The message received from the player
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
			try {
				card.apply(this.state, p);
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
			}
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		List<PlayerColor> tmp = this.awaiting.stream().map(p -> p.getColor()).toList();
		return new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(this.getClass().getSimpleName(), card.getId()),
				new ArrayList<>(tmp));
	}

	/**
	 * Called when a {@link Player} attempts to progress their turn.
	 *
	 * @param p {@link Player} The player
	 */
	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress! (" + this.awaiting.size() + " missing).");
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} The next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Card exhausted, moving to a new one!");
		return null;
	}

	/**
	 * Called when a {@link Player} disconnects.
	 *
	 * @param p {@link Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		this.awaiting.remove(p);

	}

}
