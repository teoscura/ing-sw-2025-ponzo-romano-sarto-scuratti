package it.polimi.ingsw.model.cards.state;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.util.ArrayList;

public class CombatZoneAnnounceState extends CardState {

	private final int card_id;
	private final ArrayList<CombatZoneSection> sections;
	private final ProjectileArray shots;
	private final ArrayList<Player> awaiting;
	private Player target;

	public CombatZoneAnnounceState(VoyageState state, int card_id, ArrayList<CombatZoneSection> sections, ProjectileArray shots) {
		super(state);
		if (sections == null || shots == null) throw new NullPointerException();
		if (card_id < 1 || card_id > 120) throw new IllegalArgumentException();
		this.card_id = card_id;
		this.sections = sections;
		this.shots = shots;
		this.awaiting = new ArrayList<>(this.state.getOrder(CardOrder.NORMAL));
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		if (this.state.getOrder(CardOrder.NORMAL).size() <= 1) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Only one player left playing, skipping state!");
			this.transition();
			return;
		}
		if (sections.size() == 3)
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "New CardState -> Combat Zone Announce State! [Section " + (3 - sections.size()) + " - " + this.sections.getFirst().getCriteria() + " - " + this.sections.getFirst().getPenalty() + "].");
		else
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "CardState -> Combat Zone Announce State! [Section " + (3 - sections.size()) + " - " + this.sections.getFirst().getCriteria() + " - " + this.sections.getFirst().getPenalty() + "].");
		for (Player p : this.state.getOrder(CardOrder.NORMAL)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + p.voyageInfo(this.state.getPlanche()));
		}
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!awaiting.isEmpty()) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.target = this.state.findCriteria(this.sections.getFirst().getCriteria());
		if(target!=null) Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Applying penalty to player: '" + target.getUsername() + "'");
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCombatZoneIndexCardStateDecorator(
			new ClientAwaitConfirmCardStateDecorator(
				new ClientBaseCardState(
					this.getClass().getSimpleName(),	
					card_id),
				new ArrayList<>(this.awaiting.stream().map(p -> p.getColor()).toList())),
			this.sections.getFirst(),
			3 - this.sections.size());
	}

	@Override
	public CardState getNext() {
		if (this.state.getOrder(CardOrder.NORMAL).size() > 1)
			return new CombatZonePenaltyState(state, card_id, sections, shots, target);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "...Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component after motioning to progress!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component after motioning to progress!"));
			return;
		}
		try {
			p.getSpaceShip().turnOn(target_coords, battery_coords);
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' turned on component at" + target_coords + " using battery from " + battery_coords + "!");
		} catch (IllegalTargetException e) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to turn on a component with invalid coordinates!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (!this.awaiting.contains(p)) {
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' attempted to progress the turn while already having done so!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress the turn while already having done so!"));
			return;
		}
		this.awaiting.remove(p);
		Logger.getInstance().print(LoggerLevel.MODEL, "[" + state.getModelID() + "] " + "Player: '" + p.getUsername() + "' motioned to progress! (" + (this.state.getCount().getNumber() - this.awaiting.size()) + ").");
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (awaiting.contains(p)) {
			this.awaiting.remove(p);
		}

	}

}
