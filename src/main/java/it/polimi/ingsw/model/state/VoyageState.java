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

public class VoyageState extends GameState {

	private final iPlanche planche;
	private final iCards voyage_deck;
	private final ArrayList<Player> to_give_up;
	private iCard card;
	private CardState state;

	public VoyageState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, iCards deck, iPlanche planche) {
		super(model, type, count, players);
		if (deck == null || planche == null) throw new NullPointerException();
		this.to_give_up = new ArrayList<>();
		this.planche = planche;
		this.voyage_deck = deck;
		this.card = null;
	}

	@Override
	public void init() {
		super.init();
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + model.getID() + "] " + "New Game State -> Voyage State");
		if (this.card == null) this.setCardState(null);
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.state != null && this.getOrder(CardOrder.NORMAL).size() > 0) return;
		this.transition();
	}

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

	@Override
	public boolean toSerialize() {
		return true;
	}

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

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (p == null) throw new NullPointerException();
		if (p.getDisconnected()) throw new ForbiddenCallException();
		p.disconnect();
		ServerMessage disc = new ServerDisconnectMessage();
		disc.setDescriptor(p.getDescriptor());
		this.state.validate(disc);
	}

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

	public void loseGame(Player p) {
		this.planche.loseGame(p);
		p.retire();
	}

	public List<Player> getAllConnectedPlayers() {
		List<Player> tmp = new ArrayList<>();
		tmp.addAll(this.players);
		return tmp.stream().filter((p) -> !p.getDisconnected()).toList();
	}

	public List<Player> getOrder(CardOrder order) {
		List<Player> tmp = this.players.stream().filter(p -> !p.getRetired() && !p.getDisconnected()).sorted((Player player1, Player player2) -> Integer.compare(planche.getPlayerPosition(player1), planche.getPlayerPosition(player2))).toList();
		return order != CardOrder.NORMAL ? tmp : tmp.reversed();
	}

	public Player findCriteria(CombatZoneCriteria criteria) {
		List<Player> tmp = new ArrayList<>();
		tmp.addAll(this.players);
		switch (criteria) {
			case LEAST_CANNON:
				double min_cannon_power = tmp.stream().mapToDouble(p -> p.getSpaceShip().getCannonPower()).min().orElse(0);
				return tmp.stream()
						.filter((p) -> p.getSpaceShip().getCannonPower() == min_cannon_power && !p.getRetired() && !p.getDisconnected())
						.sorted((p1, p2) -> -Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p1)))
						.findFirst().orElse(null);
			case LEAST_CREW:
				int min_crew = tmp.stream().mapToInt(p -> p.getSpaceShip().getTotalCrew()).min().orElse(0);
				return tmp.stream()
						.filter((p) -> p.getSpaceShip().getTotalCrew() == min_crew && !p.getRetired() && !p.getDisconnected())
						.sorted((p1, p2) -> -Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p1)))
						.findFirst().orElse(null);
			case LEAST_ENGINE:
				int min_engine_power = tmp.stream().mapToInt(p -> p.getSpaceShip().getEnginePower()).min().orElse(0);
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

	public void setCardState(CardState next) {
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

	@Override
	public String toString() {
		String res = "";
		res.concat("Voyage State - ");
		res.concat("Cards left: " + this.voyage_deck.getLeft() + ", Current card state: " + this.state.getClass().getSimpleName());
		return res;
	}

	@Override
	public CardState getCardState(Player p) {
		return this.state;
	}

	@Override
	public ClientGameListEntry getOngoingEntry(int id) {
		return new ClientGameListEntry(type, count, this.toString(), this.players.stream().map(p -> p.getUsername()).toList(), id);
	}

}
