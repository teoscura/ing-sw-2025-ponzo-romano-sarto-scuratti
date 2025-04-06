package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class PlanetRewardState extends CardState {

    private final PlanetCard card; //Used for validation
    private final List<Player> list;
    private final int id;
    private boolean responded = false;
    public PlanetRewardState(VoyageState state, PlanetCard card, int id, List<Player> clist) {
        super(state);
        if(clist.size()>this.state.getCount().getNumber()||clist.size()<1||clist==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null) throw new NullPointerException();
        this.id = id;
        this.list = clist;
        this.card = card;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.state.getPlanche().movePlayer(state, list.getFirst(), card.getDays());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.card.getExhausted()) return null;
        this.list.removeFirst();
        if(this.list.isEmpty()) return new PlanetAnnounceState(state, card, list);
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
        if(this.card.getPlanet(id).getContains()[type.getValue()-1]<=0){
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
        this.card.getPlanet(id).getContains()[type.getValue()-1]--;
        for(int i : this.card.getPlanet(id).getContains()){
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

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
            return;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
    }
    
}
