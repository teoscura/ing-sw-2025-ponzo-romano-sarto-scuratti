package it.polimi.ingsw.model.cards.state;

import java.util.ArrayList;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.cards.PlanetCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.ContainerMoveValidationVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientCargoRewardCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class PlanetRewardState extends CardState {

	private final PlanetCard card;
	private final ArrayList<Player> list;
	private final int id;
	private boolean responded = false;

	public PlanetRewardState(VoyageState state, PlanetCard card, int id, ArrayList<Player> clist) {
		super(state);
		if (clist.size() > this.state.getCount().getNumber() || clist.size() < 1 || clist == null)
			throw new IllegalArgumentException("Constructed insatisfyable state");
		if (card == null) throw new NullPointerException();
		this.id = id;
		this.list = clist;
		this.card = card;
	}

	@Override
	public void init(ClientModelState new_state) {
		super.init(new_state);
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (!responded) {
			this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
			return;
		}
		this.state.getPlanche().movePlayer(state, list.getFirst(), card.getDays());
		this.transition();
	}

	@Override
	public ClientCardState getClientCardState() {
		return new ClientCargoRewardCardStateDecorator(
				new ClientBaseCardState(this.card.getId()),
				this.list.getFirst().getColor(),
				0,
				this.card.getPlanet(id).getContains());
	}

	@Override
	protected CardState getNext() {
		if (this.card.getExhausted()) return null;
		this.list.removeFirst();
		if (this.list.isEmpty()) return new PlanetAnnounceState(state, card, list);
		return null;
	}

	@Override
	public void takeCargo(Player p, ShipmentType type, ShipCoords target_coords) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to take cargo during another player's turn!"));
			return;
		}
		if (type.getValue() <= 0) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take cargo with an illegal shipment type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "'  attempted to take cargo with an illegal shipment type!"));
			return;
		}
		if (this.card.getPlanet(this.id).getContains()[type.getValue() - 1] <= 0) {
			System.out.println("Player '" + p.getUsername() + "' attempted to take cargo the card doesn't have!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "'  attempted to take cargo the card doesn't have!"));
			return;
		}
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(type);
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to position cargo in illegal coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in illegal coordinates!"));
			return;
		} catch (ContainerFullException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to position cargo in a storage that's full!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in a storage that's full!"));
			return;
		} catch (ContainerNotSpecialException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to position cargo in a storage that doesn't support it!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to position cargo in a storage that doesn't support it!"));
			return;
		}
		this.card.getPlanet(this.id).getContains()[type.getValue() - 1]--;
		for (int i : this.card.getPlanet(this.id).getContains()) {
			if (i > 0) return;
		}
		this.responded = true;
	}

	@Override
	public void moveCargo(Player p, ShipmentType type, ShipCoords target_coords, ShipCoords source_coords) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to load cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to load cargo during another player's turn!"));
			return;
		}
		if (type.getValue() <= 0) {
			System.out.println("Player '" + p.getUsername() + "' attempted to load an invalid shipment type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to load an invalid shipment type!"));
			return;
		}
		ContainerMoveValidationVisitor v = new ContainerMoveValidationVisitor(type);
		try {
			p.getSpaceShip().getComponent(source_coords).check(v);
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to load cargo from an invalid source!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to load cargo from an invalid source!"));
			return;
		}
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to load cargo from coords that dont contain the shipment!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to load cargo from coords that dont contain the shipment!"));
			return;
		}
		ContainsRemoveVisitor vr = new ContainsRemoveVisitor(type);
		ContainsLoaderVisitor vl = new ContainsLoaderVisitor(type);
		p.getSpaceShip().getComponent(source_coords).check(vr);
		p.getSpaceShip().getComponent(target_coords).check(vl);
	}

	@Override
	public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo during another player's turn!"));
			return;
		}
		if (type.getValue() <= 0) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo with an invalid type!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo with an invalid type!"));
			return;
		}
		ContainsRemoveVisitor v = new ContainsRemoveVisitor(type);
		try {
			p.getSpaceShip().getComponent(target_coords).check(v);
		} catch (IllegalTargetException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from illegal coordinates!"));
		} catch (ContainerEmptyException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to discard cargo from an empty storage component!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to discard cargo from an empty storage component!"));
		}
	}

	@Override
	public void progressTurn(Player p) {
		if (p != this.list.getFirst()) {
			System.out.println("Player '" + p.getUsername() + "' attempted to progress during another player's turn!");
			this.state.broadcastMessage(new ViewMessage("Player'" + p.getUsername() + "' attempted to progress during another player's turn!"));
			return;
		}
		this.responded = true;
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
