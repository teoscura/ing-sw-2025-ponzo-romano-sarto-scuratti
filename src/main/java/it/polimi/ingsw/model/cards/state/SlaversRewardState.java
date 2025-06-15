package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.SlaversCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCreditsRewardCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
/**
 * Class representing the Reward State of the {@link SlaversCard}.
 */
class SlaversRewardState extends CardState {

	private final SlaversCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private boolean took_reward = false;

	/**
	 * Construct a {@link SlaversRewardState} object.
	 * 
	 * @param state {@link it.polimi.ingsw.model.state.VoyageState} The current voyage state
	 * @param card  {@link SlaversCard} The card being played.
	 * @param list  List of {@link it.polimi.ingsw.model.player.Player} players in order of distance.
	 */
	public SlaversRewardState(VoyageState state, SlaversCard card, ArrayList<Player> list) {
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
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Slavers Reward State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates the {@link it.polimi.ingsw.message.server.ServerMessage} and if the front of the remaining player list has decided to take the reward, transition.
	 *
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} The message received from the player
	 * @throws ForbiddenCallException if the message is not allowed
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		if (took_reward) {
			this.list.getFirst().giveCredits(card.getCredits());
			this.state.getPlanche().movePlayer(state, list.getFirst(), -card.getDays());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCreditsRewardCardStateDecorator(
				new ClientBaseCardState(
						this.getClass().getSimpleName(),
						card.getId()),
				this.list.getFirst().getColor(),
				this.card.getCredits(),
				this.card.getDays());
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
	 * Called when a {@link it.polimi.ingsw.model.player.Player} decides to take a reward.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player taking the reward.
	 * @param take True if the player wants to take the reward.
	 */
	@Override
	public void setTakeReward(Player p, boolean take) {
		if (p != this.list.getFirst()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to accept a reward during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to accept a reward during another player's turn!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' took the reward? " + take);
		this.took_reward = take;
		this.responded = true;
	}

	/**
	 * Called when a {@link it.polimi.ingsw.model.player.Player} disconnects.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player disconnecting.
	 * @throws ForbiddenCallException when the state refuses the action.
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {
			this.responded = true;
			this.took_reward = false;
		}

	}

}
