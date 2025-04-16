package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainerMoveValidationVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
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

class SmugglersRewardState extends CardState {

    private final SmugglersCard card;
    private final List<Player> list;
    private boolean responded = false;
    private boolean took_reward = false;

    public SmugglersRewardState(VoyageState state, SmugglersCard card, List<Player> list){
        super(state);
        if(state==null||card==null||list==null||list.size()>this.state.getCount().getNumber()||list.size()<1) throw new IllegalArgumentException("Created unsatisfyable state");
        this.card = card;
        this.list = list;
    }

    @Override
    public void init(ClientModelState new_state) {
        super.init(new_state);
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded){
            this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
            return;
        }
        if(took_reward){
            this.state.getPlanche().movePlayer(this.state, this.list.getFirst(), -this.card.getDays());
        }
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        return new ClientCargoRewardCardStateDecorator(
            new ClientBaseCardState(this.card.getId()), 
            this.list.getFirst().getColor(), 
            this.card.getDays(), 
            this.card.getReward().getContains());
    }

    @Override
    protected CardState getNext() {
        return null;
    }

    @Override
    public void takeCargo(Player p, ShipmentType type, ShipCoords target_coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("This isn't your turn!"));
            return;
        }
        if(type.getValue()<=0){
            p.getDescriptor().sendMessage(new ViewMessage("Illegal shipment type!"));
            return;
        }
        if(this.card.getReward().getContains()[type.getValue()-1]<=0){
            p.getDescriptor().sendMessage(new ViewMessage("Taking unavailable merch!"));
            return;
        }
        ContainsLoaderVisitor v = new ContainsLoaderVisitor(type);
        try{
            p.getSpaceShip().getComponent(target_coords).check(v);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords are not a storage component!"));
            return;
        } catch (ContainerFullException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target StorageComponent is full!"));
            return;
        } catch (ContainerNotSpecialException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target StorageComponent does not support the type provided!"));
            return;
        }
        this.card.getReward().getContains()[type.getValue()-1]--;
        for(int i : this.card.getReward().getContains()){
            if(i>0) return;
        }
        this.responded = true;
    }

    @Override
    public void moveCargo(Player p, ShipmentType type, ShipCoords target_coords, ShipCoords source_coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("This isn't your turn!"));
            return;
        }
        if(type.getValue()<=0){
            p.getDescriptor().sendMessage(new ViewMessage("Illegal shipment type!"));
            return;
        }
        ContainerMoveValidationVisitor v = new ContainerMoveValidationVisitor(type);
        try {
            p.getSpaceShip().getComponent(source_coords).check(v);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Source coords dont contain the moved shipment!"));
            return;
        }
        try {
            p.getSpaceShip().getComponent(target_coords).check(v);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target coords can't contain the shipment!"));
            return;
        }
        ContainsRemoveVisitor vr = new ContainsRemoveVisitor(type);
        ContainsLoaderVisitor vl = new ContainsLoaderVisitor(type);
        p.getSpaceShip().getComponent(source_coords).check(vr);
        p.getSpaceShip().getComponent(target_coords).check(vl);
    }

    @Override
    public void discardCargo(Player p, ShipmentType type, ShipCoords target_coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("This isn't your turn!"));
            return;
        }
        if(type.getValue()<=0){
            p.getDescriptor().sendMessage(new ViewMessage("Illegal shipment type!"));
            return;
        }
        ContainsRemoveVisitor v = new ContainsRemoveVisitor(type);
        try{
            p.getSpaceShip().getComponent(target_coords).check(v);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords are not a storage component!"));
            return;
        } catch (ContainerEmptyException e){
            p.getDescriptor().sendMessage(new ViewMessage("Target StorageComponent is empty!"));
            return;
        }
    }

    @Override
    public void progressTurn(Player p){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else"));
            return;
        }
        this.responded = true;
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
            this.took_reward = false;
            return;
        }
    }

}
