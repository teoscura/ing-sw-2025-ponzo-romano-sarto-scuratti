package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PlanetCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

/**
 * Class representing the Announce State of the {@link PlanetCard}.
 */
public class PlanetAnnounceState extends CardState {

	private final PlanetCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private int id = -1;

	/**
	 * Constructs a new {@code PlanetAnnounceState}.
	 *
	 * @param state {@link VoyageState} The current voyage state
	 * @param card  {@link PlanetCard} The card being played.
	 * @param list  List of {@link Player} players in order of distance.
	 * @throws IllegalArgumentException if the list is empty or too large
	 */
	public PlanetAnnounceState(VoyageState state, PlanetCard card, ArrayList<Player> list) {
		super(state);
		if (state == null || card == null || list == null) throw new NullPointerException();
		if (list.size() > state.getCount().getNumber() || list.size() < 1)
			throw new IllegalArgumentException("Created unsatisfyable state");
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
		if (list.size() == this.state.getCount().getNumber())
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Planet Announce State!");
		else
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Planet Announce State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	/**
	 * Validates and resolves the {@link Player}'s response to the landing offer.
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
		if (this.id >= 0) {
			this.card.apply(this.list.getFirst(), id);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + this.list.getFirst().getUsername() + "' moved back " + card.getDays());
			this.state.getPlanche().movePlayer(state, list.getFirst(), -card.getDays());
		}
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientLandingCardStateDecorator(
				new ClientBaseCardState(
						this.getClass().getSimpleName(),
						card.getId()),
				this.list.getFirst().getColor(),
				this.card.getDays(),
				0, 0,
				card.getPlanets());
	}

	/**
	 * Computes and returns the next {@code CardState}.
	 *
	 * @return {@link CardState} the next state, or {@code null} if the card is exhausted
	 */
	@Override
	public CardState getNext() {
		if (id != -1) return new PlanetRewardState(state, card, id, list);
		this.list.removeFirst();
		if (!this.list.isEmpty()) return new PlanetAnnounceState(state, card, list);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Moving to a new state!");
		return null;
	}

	/**
	 * Called when a {@link Player} tries to select a landing location.
	 *
	 * @param p {@link Player} The player selecting the landing.
	 * @param planet The id of the landing location chosen.
	 */
	@Override
	public void selectLanding(Player p, int planet) {
		if (!p.equals(this.list.getFirst())) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to land during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to land during another player's turn!"));
			return;
		} else if (planet < -1 || planet >= this.card.getSize()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to land on an invalid id!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to land on an invalid id!"));
			return;
		}
		if (planet == -1) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' chose to not land!");
			this.id = planet;
			this.responded = true;
			return;
		} else if (this.card.getPlanet(planet).getVisited()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to land on a planet that was already visited!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to land on a planet that was already visited!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' landed on: " + planet);
		this.id = planet;
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
		if (this.list.getFirst() == p) {

			this.responded = true;
			this.id = -1;
			return;
		}
		this.list.remove(p);

	}

}
