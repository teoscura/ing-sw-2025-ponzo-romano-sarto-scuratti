package it.polimi.ingsw.model.adventure_cards.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class PiratesPenaltyState extends CardState {

    private final PiratesCard card;
    private final ArrayList<Player> list;
    private final ProjectileArray shots;
    private boolean responded = false;

    protected PiratesPenaltyState(VoyageState state, PiratesCard card, ArrayList<Player> list, ProjectileArray shots) {
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null||shots==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
        this.shots = shots;
    }

    @Override
    public void init(ClientModelState new_state){
        super.init(new_state);
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded){
            this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
            return;
        }
        this.list.getFirst().getSpaceShip().handleShot(this.shots.getProjectiles().getFirst());
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        List<PlayerColor> awaiting = Arrays.asList(new PlayerColor[]{this.list.getFirst().getColor()});
        return new ClientProjectileCardStateDecorator(
            new ClientAwaitConfirmCardStateDecorator(
                new ClientBaseCardState(this.card.getId()), 
                new ArrayList<>(awaiting)),
            this.shots.getProjectiles().getFirst());
    }

    @Override
    protected CardState getNext() {
        if(this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()){
            this.list.removeFirst();
            if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
            return null;
        }
        this.shots.getProjectiles().removeFirst();
        if(!this.list.getFirst().getSpaceShip().getBrokeCenter()) this.list.getFirst().getSpaceShip().verifyAndClean();
        else{
            return new PiratesNewCabinState(state, card, list, shots);
        }
        if(!this.shots.getProjectiles().isEmpty()) return new PiratesPenaltyState(state, card, list, shots);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void turnOn(Player p, ShipCoords target_coords, ShipCoords battery_coords){
        if(this.list.getFirst()!=p){
            System.out.println("Player '"+p.getUsername()+"' attempted to turn on a component during another player's turn!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to turn on a component during another player's turn!"));
            return;
        }
        try{
            p.getSpaceShip().turnOn(target_coords, battery_coords);
        } catch (IllegalTargetException e){
            System.out.println("Player '"+p.getUsername()+"' attempted to turn on a component with invalid coordinates!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to turn on a component with invalid coordinates!"));
            return;
        }
    } 

    @Override
    public void progressTurn(Player p){
        if(p!=this.list.getFirst()){
            System.out.println("Player '"+p.getUsername()+"' attempted to progress during another player's turn!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to progress during another player's turn!"));
            return;
        }
        this.responded = true;
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
    }

}
