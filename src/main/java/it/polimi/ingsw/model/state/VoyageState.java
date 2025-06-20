package it.polimi.ingsw.model.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.iCard;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.SelectShipReconnectState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.CombatZoneCriteria;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Space voyage phase of a match of Galaxy Trucker.
 */
public class VoyageState extends GameState {

	private final iPlanche planche;
	private final iCards voyage_deck;
	private final ArrayList<Player> to_give_up;
	private iCard card;
	private CardState state;

	/**
	 * Constructs a new {@link it.polimi.ingsw.model.state.VoyageState} object.
	 * 
	 * @param model {@link it.polimi.ingsw.model.ModelInstance} ModelInstance that owns this {{@link it.polimi.ingsw.model.state.GameState}.
	 * @param type {@link GameModeType} Ruleset of the state.
	 * @param count {@link it.polimi.ingsw.model.PlayerCount} Size of the match.
	 * @param players Array of all {@link it.polimi.ingsw.model.player.Player players} in the match.
	 * @param deck {@link iCards} Voyage deck containing the cards.
	 * @param planche {@link iPlanche} Planche to use during the match.
	 */
	public VoyageState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, iCards deck, iPlanche planche) {
		super(model, type, count, players);
		if (deck == null || planche == null) throw new NullPointerException();
		this.to_give_up = new ArrayList<>();
		this.planche = planche;
		this.voyage_deck = deck;
		this.card = null;
	}
	
	/**
	 * Picks the first {@link iCard} from the deck.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Voyage State");
		if (this.card == null) this.setCardState(null);
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	/**
	 * Processes the {@link it.polimi.ingsw.message.server.ServerMessage} by passing it through the current {@link CardState}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.state != null && this.getOrder(CardOrder.NORMAL).size() > 0) return;
		this.transition();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameState getNext() {
		ArrayList<Player> tmp = new ArrayList<>();
		tmp.addAll(this.players);
		//Retired players dont count in the distance scoring.
		tmp = new ArrayList<>(tmp.stream().filter(p -> !p.getRetired()).toList());
		//Sort in descending order, so the farthest one gets the first index, second farthest gets second index and so on.
		tmp.sort((p1, p2) -> Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p2)));
		return new EndscreenState(model, type, count, players, new ArrayList<>(tmp.reversed()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientState getClientState() {
		ArrayList<ClientVoyagePlayer> tmp = new ArrayList<>();
		for (Player p : this.players) {
			tmp.add(new ClientVoyagePlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					this.planche.getPlayerPosition(p),
					p.getCredits(),
					p.getDisconnected(),
					p.getRetired()));
		}
		return new ClientVoyageState(type, tmp, this.state.getClientCardState(), this.voyage_deck.getLeft());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean toSerialize() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (!p.getDisconnected()) throw new ForbiddenCallException();
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' reconnected!");
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' reconnected!"));
		p.reconnect();
		if (!p.getRetired() && p.getSpaceShip().getBlobsSize() > 1) {
			this.setCardState(new SelectShipReconnectState(this, this.state, p));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (p.getDisconnected()) throw new ForbiddenCallException();
		p.disconnect();
		ServerMessage disc = new ServerDisconnectMessage();
		disc.setDescriptor(p.getDescriptor());
		this.state.validate(disc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void giveUp(Player p) throws ForbiddenCallException {
		if (p == null) return;
		if (p.getRetired()) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' attempted to give up, but they already aren't playing!");
			this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' attempted to give up, but they already aren't playing!"));
			return;
		}
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "Player: '" + p.getUsername() + "' gave up!");
		this.broadcastMessage(new ViewMessage("Player: '" + p.getUsername() + "' gave up!"));
		this.to_give_up.add(p);
	}

	/**
	 * Loses the game for a {@link it.polimi.ingsw.model.player.Player}.
	 * 
	 * @param p {@link it.polimi.ingsw.model.player.Player} Player that lost.
	 */
	public void loseGame(Player p) {
		this.planche.loseGame(p);
		p.retire();
	}

	public List<Player> getAllConnectedPlayers() {
		List<Player> tmp = new ArrayList<>();
		tmp.addAll(this.players);
		return tmp.stream().filter((p) -> !p.getDisconnected()).toList();
	}

	/**
	 * @param order {@link CardOrder} Order to sort the list by.
	 * @return A list of {@link it.polimi.ingsw.model.player.Player} sorted in respect to the specified order on the planche.
	 */
	public List<Player> getOrder(CardOrder order) {
		List<Player> tmp = this.players.stream().filter(p -> !p.getRetired() && !p.getDisconnected()).sorted((Player player1, Player player2) -> Integer.compare(planche.getPlayerPosition(player1), planche.getPlayerPosition(player2))).toList();
		return order != CardOrder.NORMAL ? tmp : tmp.reversed();
	}

	/**
	 * Finds the farthest {@link it.polimi.ingsw.model.player.Player} along the planche with the worst value specified by the {@link CombatZoneCriteria}.
	 * 
	 * @param criteria {@link CombatZoneCriteria} Criteria according to which the {@link it.polimi.ingsw.model.player.Player} is picked.
	 * @return {@link it.polimi.ingsw.model.player.Player} Farthest player along the planche with the worst value specified by the {@link CombatZoneCriteria}.
	 */
	public Player findCriteria(CombatZoneCriteria criteria) {
		List<Player> tmp = new ArrayList<>();
		tmp.addAll(this.players);
		switch (criteria) {
			case LEAST_CANNON:
				double min_cannon_power = tmp.stream().filter(p->!p.getRetired()&&!p.getDisconnected()).mapToDouble(p -> p.getSpaceShip().getCannonPower()).min().orElse(0);
				return tmp.stream()
						.filter((p) -> p.getSpaceShip().getCannonPower() == min_cannon_power && !p.getRetired() && !p.getDisconnected())
						.sorted((p1, p2) -> -Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p1)))
						.findFirst().orElse(null);
			case LEAST_CREW:
				int min_crew = tmp.stream().filter(p->!p.getRetired()&&!p.getDisconnected()).mapToInt(p -> p.getSpaceShip().getTotalCrew()).min().orElse(0);
				return tmp.stream()
						.filter((p) -> p.getSpaceShip().getTotalCrew() == min_crew && !p.getRetired() && !p.getDisconnected())
						.sorted((p1, p2) -> -Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p1)))
						.findFirst().orElse(null);
			case LEAST_ENGINE:
				int min_engine_power = tmp.stream().filter(p->!p.getRetired()&&!p.getDisconnected()).mapToInt(p -> p.getSpaceShip().getEnginePower()).min().orElse(0);
				return tmp.stream()
						.filter((p) -> p.getSpaceShip().getEnginePower() == min_engine_power && !p.getRetired() && !p.getDisconnected())
						.sorted((p1, p2) -> -Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p1)))
						.findFirst().orElse(null);
			default:
				return null;
		}
	}

	public iPlanche getPlanche() {
		return planche;
	}

	/**
	 * Sets a new {@link CardState}, if {@code null} then picks a new {@link iCard} from the {@link iCards deck}.
	 * Makes every player set to lose or give up retire.
	 * 
	 * @param next {@link CardState} Next state to set.
	 */
	public void setCardState(CardState next) {
		for(var p : this.players){
			p.getSpaceShip().resetPower();
		}
		if (next == null) {
			if (this.getOrder(CardOrder.NORMAL).size() == 0) {
				this.transition();
				return;
			}
			for (Player p : this.getOrder(CardOrder.NORMAL)) {
				if (p.getSpaceShip().getCrew()[0] == 0 || p.getSpaceShip().getBlobsSize() <= 0) loseGame(p);
			}
			for (Player p : this.getOrder(CardOrder.NORMAL)) {
				if (planche.checkLapped(p)) loseGame(p);
			}
			for (Player p : this.to_give_up) {
				if (!p.getRetired()) this.loseGame(p);
			}
			this.to_give_up.clear();
			this.card = this.voyage_deck.pullCard();
			if (this.card == null) {
				this.transition();
				return;
			}
			this.state = card.getState(this);
			this.model.serialize();
			this.state.init(this.getClientState());
			return;
		}
		this.state = next;
		next.init(this.getClientState());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String res = "";
		res.concat("Voyage State - ");
		res.concat("Cards left: " + this.voyage_deck.getLeft() + ", Current card state: " + this.state.getClass().getSimpleName());
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CardState getCardState(Player p) {
		return this.state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}

}
