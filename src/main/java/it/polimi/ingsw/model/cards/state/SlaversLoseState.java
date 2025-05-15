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

public class SlaversLoseState extends CardState {

	private final SlaversCard card;
	private final ArrayList<Player> list;
	private boolean responded = false;
	private int done = 0;

	protected SlaversLoseState(VoyageState state, SlaversCard card, ArrayList<Player> list) {
		super(state);
		if (list.size() > this.state.getCount().getNumber() || list.size() < 1 || list == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.card = card;
		this.list = list;
	}

	@Override
	public void init(ClientState new_state) {
		super.init(new_state);
		/*XXX*/System.out.println("CardState -> Slavers Lose State!");
		for (Player p : this.list) {
			Logger.getInstance().print(LoggerLevel.LOBBY, "[Lobby id:"+this.state.getModelID()+"] "+p.voyageInfo(this.state.getPlanche()));
		}
	}

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
				new ClientBaseCardState(this.card.getId()),
				this.list.getFirst().getColor(),
				this.card.getCrewLost() - this.done);
	}

	@Override
	public CardState getNext() {
		if (this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()) {
			this.list.removeFirst();
			if (!this.list.isEmpty()) return new SlaversAnnounceState(state, card, list);
			/*XXX*/System.out.println("...Card exhausted, moving to a new one!");
			return null;
		}
		this.list.removeFirst();
		if (!list.isEmpty()) return new SlaversAnnounceState(state, card, list);
		/*XXX*/System.out.println("...Card exhausted, moving to a new one!");
		return null;
	}

	@Override
	public void removeCrew(Player p, ShipCoords cabin_coords) throws ForbiddenCallException {
		if (p != this.list.getFirst()) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member during another player's turn!"));
			return;
		}
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		try {
			p.getSpaceShip().getComponent(cabin_coords).check(v);
			this.done++;
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' removed a crewmate from " + cabin_coords + "!");
		} catch (IllegalTargetException e) {
			/*XXX*/System.out.println("Player '" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to remove a crew member from invalid coordinates!"));
			return;
		}
		if (p.getSpaceShip().getCrew()[0] == 0) {
			this.state.loseGame(p);
			return;
		}
		if (this.done >= this.card.getCrewLost()) {
			this.responded = true;
		}
	}

	@Override
	public void disconnect(Player p) throws ForbiddenCallException {
		if (this.list.getFirst() == p) {

			this.responded = true;
			return;
		}
		this.list.remove(p);

	}

}
