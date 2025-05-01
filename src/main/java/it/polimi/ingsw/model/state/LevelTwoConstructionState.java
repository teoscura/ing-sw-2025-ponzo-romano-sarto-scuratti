package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;

public class LevelTwoConstructionState extends ConstructionState {

    private final ConstructionStateHourglass hourglass;

	public LevelTwoConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players) {
		super(model, type, count, players, new LevelTwoCards());
        this.hourglass = new ConstructionStateHourglass(90, 3);
	}

	@Override
	public void init() {
        System.out.println("New Game State -> Construction State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
	}

	@Override
	public void validate(ServerMessage message) throws ForbiddenCallException {
		message.receive(this);
		if (this.finished.size() != this.players.size()) {
			this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
			return;
		}
		this.transition();
	}

	@Override
	public GameState getNext() {
		return new VerifyState(model, type, count, players, voyage_deck, finished);
	}

	@Override
	public ClientModelState getClientState() {
		ArrayList<ClientConstructionPlayer> tmp = new ArrayList<>();
		ArrayList<Integer> discarded = new ArrayList<>(this.board.getDiscarded());
		for (Player p : this.players) {
			ArrayList<Integer> stash = new ArrayList<>();
			if (this.current_tile.get(p) != null) stash.add(this.current_tile.get(p).getID());
			stash.addAll(this.hoarded_tile.get(p).stream().filter(t -> t != null).map(t -> t.getID()).toList());
			tmp.add(new ClientConstructionPlayer(p.getUsername(),
					p.getColor(),
					p.getSpaceShip().getClientSpaceShip(),
					stash,
					this.finished.contains(p)));
		}
		return new ClientConstructionState(this.type, tmp, new ArrayList<>(this.voyage_deck.getConstructionCards()), discarded, this.board.getCoveredSize(), this.hourglass.getInstant());
	}

	@Override
	public boolean toSerialize() {
		return true;
	}

	@Override
	public void sendContinue(Player p) throws ForbiddenCallException {
		//XXX condizione extra costruzione.
	}

	@Override
	public void putComponent(Player p, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
        //XXX Condizione extra clessidra
        super.putComponent(p, coords, rotation);
	}

	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
        //XXX Condizione extra clessidra
        super.takeComponent(p);
	}

	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		//XXX condizione extra clessidra
        super.takeDiscarded(p, id);
	}

	@Override
	public void discardComponent(Player p, int id) throws ForbiddenCallException {
		//XXX condizione extra clessidra
        super.discardComponent(p, id);
	}

}
