package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PlanetCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.state.ClientModelState;
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
    public void init(ClientModelState new_state) {
        super.init(new_state);
    }

    @Override
    public void validate(ServerMessage message)throws ForbiddenCallException {
        message.receive(this);
        if(!responded){
            this.state.broadcastMessage(new NotifyStateUpdateMessage(this.state.getClientState()));
            return;
        }
        this.card.apply(this.list.getFirst(), id);
        this.transition();
    }

    @Override
    public ClientCardState getClientCardState(){
        return new ClientLandingCardStateDecorator(
            new ClientBaseCardState(this.card.getId()), 
            this.list.getFirst().getColor(), 
            this.card.getDays(), 
            0,
            this.card.getVisited());
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
            System.out.println("Player '"+p.getUsername()+"' attempted to land during another player's turn!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to land during another player's turn!"));
            return;
        }
        else if(planet<-1||planet>=this.card.getSize()){ 
            System.out.println("Player '"+p.getUsername()+"' attempted to land on an invalid id!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to land on an invalid id!"));
            return;
        }
        if(this.card.getPlanet(planet).getVisited()){
            System.out.println("Player '"+p.getUsername()+"' attempted to land on a planet that was already visited!");
            this.state.broadcastMessage(new ViewMessage("Player'"+p.getUsername()+"' attempted to land on a planet that was already visited!"));
            return;
        }
        this.id = planet;
        this.responded = true;
    }   

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(this.list.getFirst()==p){
            this.responded = true;
            this.id = -1;
            return;
        }
        if(this.list.contains(p)){
            this.list.remove(p);
            return;
        }
    }
    
}
