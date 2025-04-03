package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class PlanetAnnounceState extends CardState {

    private final PlanetCard card;
    private final List<Player> list;
    private boolean responded = false;
    private int id = -1;

    public PlanetAnnounceState(VoyageState state, PlanetCard card, List<Player> list){
        super(state);
        if(state==null||card==null||list==null) throw new NullPointerException();
        if(list.size()>state.getCount().getNumber()||list.size()<1) throw new IllegalArgumentException("Created unsatisfyable state");
        this.card = card;
        this.list = list;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void validate(ServerMessage message)throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.card.apply(this.list.getFirst(), id);
        this.transition();
    }

    @Override
    protected CardState getNext() {
        if(id!=-1) return new PlanetRewardState(state, card, id, list);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PlanetAnnounceState(state, card, list);
        return null;
    }

    @Override
    public void selectLanding(Player p, int planet){
        if(p!=this.list.getFirst()){
            p.getDescriptor().sendMessage(new ViewMessage("It's not your turn!"));
            return;
        }
        else if(planet<-1||planet>=this.card.getSize()){ 
            p.getDescriptor().sendMessage(new ViewMessage("Wrong landing id sent!"));
            return;
        }
        if(this.card.getPlanet(planet).getVisited()){
            p.getDescriptor().sendMessage(new ViewMessage("Planet already visited!"));
            return;
        }
        this.id = planet;
        this.responded = true;
    }
    
}
