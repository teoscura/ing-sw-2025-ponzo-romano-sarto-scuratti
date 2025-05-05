package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;

public class LevelTwoConstructionState extends ConstructionState {

    private final ConstructionStateHourglass hourglass;

	public LevelTwoConstructionState(ModelInstance model, GameModeType type, PlayerCount count, ArrayList<Player> players, int seconds) {
		super(model, type, count, players, new LevelTwoCards());
        this.hourglass = new ConstructionStateHourglass(seconds, 3);
	}

	@Override
	public void init() {
        System.out.println("New Game State -> Construction State");
		this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
		this.hourglass.start();
	}

	@Override
	public ClientState getClientState() {
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
	public void sendContinue(Player p) throws ForbiddenCallException {
		super.sendContinue(p);
	}

	@Override
	public void putComponent(Player p, ShipCoords coords, ComponentRotation rotation) throws ForbiddenCallException {
        if(!hourglass.canAct()){
			System.out.println("Player '" + p.getUsername() + "' attempted to place a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to place a component, but the hourglass has ran out on the last space!"));
			return;
		}
        super.putComponent(p, coords, rotation);
	}

	@Override
	public void takeComponent(Player p) throws ForbiddenCallException {
        if(!hourglass.canAct()){
			System.out.println("Player '" + p.getUsername() + "' attempted to take a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a component, but the hourglass has ran out on the last space!"));
			return;
		}
        super.takeComponent(p);
	}

	@Override
	public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
		if(!hourglass.canAct()){
			System.out.println("Player '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to take a discarded component, but the hourglass has ran out on the last space!"));
			return;
		}
        super.takeDiscarded(p, id);
	}

	@Override
	public void discardComponent(Player p, int id) throws ForbiddenCallException {
		if(!hourglass.canAct()){
			System.out.println("Player '" + p.getUsername() + "' attempted to discard a component, but the hourglass has ran out on the last space!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to discard a component, but the hourglass has ran out on the last space!"));
			return;
		}
        super.discardComponent(p, id);
	}

	@Override
	public void toggleHourglass(Player p){
		if(hourglass.isRunning()){
			System.out.println("Player '" + p.getUsername() + "' attempted to move and toggle the hourglass, but sand is still falling!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to move and toggle the hourglass, but sand is still falling!"));
			return;
		}
		if(hourglass.timesLeft()>1){
			try {
				hourglass.toggle();
				System.out.println("Player '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: "+hourglass.timesLeft()+")");
				this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' turned the hourglass and moved it to the next slot! (Times remaining: "+hourglass.timesLeft()+")"));
				return;
			} catch (ForbiddenCallException e) {
				//Checks were done beforehand, should never happen. Getting here is gamebreaking.
				System.exit(-1);
			}
		}
		if(this.building.contains(p)){
			System.out.println("Player '" + p.getUsername() + "' attempted to move and toggle the hourglass to the last slot, but they're still building!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to move and toggle the hourglass to the last slot, but they're still building!"));
			return;
		}
		try {
			hourglass.toggle();
		} catch (ForbiddenCallException e) {
			System.out.println("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but it's already run out on the last slot!");
			this.broadcastMessage(new ViewMessage("Player '" + p.getUsername() + "' attempted to toggle the hourglass, but it's already run out on the last slot!"));
			return;
		}
	}

}
