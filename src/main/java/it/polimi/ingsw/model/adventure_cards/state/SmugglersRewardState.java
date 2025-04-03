package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.components.StorageComponent;
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
    private List<ShipCoords> coords = null;
    private List<ShipmentType> merch = null;

    public SmugglersRewardState(VoyageState state, SmugglersCard card, List<Player> list){
        super(state);
        if(state==null||card==null||list==null||list.size()>this.state.getCount().getNumber()||list.size()<1) throw new IllegalArgumentException("Created unsatisfyable state");
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        if(took_reward){
            for(int i=0;i<this.coords.size();i++){
                ((StorageComponent) this.list.getFirst().getSpaceShip().getComponent(this.coords.get(i))).putIn(this.merch.get(i));
            }
            this.state.getPlanche().movePlayer(this.list.getFirst().getColor(), -this.card.getDays());
        }
        this.transition();
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
}
