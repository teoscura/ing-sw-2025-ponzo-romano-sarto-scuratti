package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.SmugglersCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

class SmugglersLoseState extends CardState {

    private final SmugglersCard card;
    private final List<Player> list;
    private final int[] required;
    private boolean responded = false;

    public SmugglersLoseState(VoyageState state, SmugglersCard card, List<Player> list){
        super(state);
        if(state==null||card==null||list==null||list.size()>this.state.getCount().getNumber()||list.size()<1) throw new IllegalArgumentException("Created unsatisfyable state");
        this.card = card;
        this.list = list;
        this.required = new int[5];
        int penalty = this.card.getCargoPenalty();
        int[] player_cargo = list.getFirst().getSpaceShip().getContains();
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) break;
            int tmp = penalty - player_cargo[t.getValue()-1];
            if(tmp<=0){
                this.required[t.getValue()-1] = penalty;
                return;
            }
            else{
                this.required[t.getValue()-1] = player_cargo[t.getValue()-1];
                penalty = tmp;
            }
        }
        if(penalty>0){
            this.required[4] = penalty;
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(this.list.getFirst().getRetired() || this.list.getFirst().getDisconnected()){
            this.list.removeFirst();
            if(!this.list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
            return null;
        }
        this.list.removeFirst();
        if(!list.isEmpty()) return new SmugglersAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void removeCargo(Player p, ShipmentType type, ShipCoords coords){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) break;
            if(this.required[t.getValue()-1] <=0 ) continue;
            ContainsRemoveVisitor v = new ContainsRemoveVisitor(t);
            try {
                p.getSpaceShip().getComponent(coords).check(v);
                this.required[t.getValue()-1]--;
                break;
            } catch (ContainerEmptyException e) {
                p.getDescriptor().sendMessage(new ViewMessage("Give merch up in order of value!"));
                return;
            } catch (IllegalArgumentException e){
                p.getDescriptor().sendMessage(new ViewMessage("Sent invalid coords!"));
                return;
            }
        }
        if(this.required[4]>0 && p.getSpaceShip().getEnergyPower()>0){
            ContainsRemoveVisitor v = new ContainsRemoveVisitor();
            try{
                p.getSpaceShip().getComponent(coords).check(v);
            } catch (ContainerEmptyException e){
                p.getDescriptor().sendMessage(new ViewMessage("There are no batteries in the coords!"));
                return;
            } catch (IllegalArgumentException e){
                p.getDescriptor().sendMessage(new ViewMessage("Sent invalid coords!"));
                return;
            }
        }
        else this.responded = true;
    }

    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
        }
    }
    
}
