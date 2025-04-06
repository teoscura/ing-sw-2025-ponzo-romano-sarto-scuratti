package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneCriteria;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;

public class VoyageState extends GameState {
    
    private final iPlanche planche;
    private final iCards voyage_deck;
    private final List<Player> to_give_up;
    private CardState state;

    public VoyageState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players, iCards deck, iPlanche planche){
        super(model, type, count, players);
        if(deck==null||planche==null) throw new NullPointerException();
        this.to_give_up = new ArrayList<>();
        this.planche = planche;
        this.voyage_deck = deck;
    }

    @Override
    public void init(){
        super.init();
        this.setCardState(null);
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.state != null) return;
        this.transition();
    }

    @Override
    public GameState getNext(){
        List<Player> tmp = Arrays.asList(this.players);
        //Retired players dont count in the distance scoring.
        tmp = tmp.stream().filter(p->!p.getRetired()).toList();
        //Sort in descending order, so the farthest one gets the first index, second farthest gets second index and so on.
        tmp.sort((p1,p2)-> this.planche.getPlayerPosition(p1)<this.planche.getPlayerPosition(p2) ? 1 : -1);
        return new EndscreenState(model, type, count, players, tmp);
    }

    @Override
    public void connect(ClientDescriptor client) throws ForbiddenCallException {
        //XXX Add that if 3 people disconnected game ends.
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    @Override
    public void disconnect(ClientDescriptor client) throws ForbiddenCallException {
        //XXX Add that if 3 people disconnected game ends.
        throw new ForbiddenCallException("This state doesn't support this function.");
    }

    @Override
    public void giveUp(Player p) throws ForbiddenCallException {
        if(p==null) return;
        if(p.getRetired()==true){
            p.getDescriptor().sendMessage(new ViewMessage("You have already retired/lost!"));
            return;
        }
        this.to_give_up.add(p);
    }

    public void loseGame(Player p){
        int sum = 0;
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()<=0) continue;
            sum += p.getSpaceShip().getContains()[t.getValue()-1]*t.getValue();
        }
        p.addScore(sum/2+sum%2);
        this.planche.loseGame(p);
        p.retire();
    }

    public List<Player> getOrder(CardOrder order){
        List<Player> tmp = Arrays.asList(this.players);
        switch(order){
            case NORMAL: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1)>this.planche.getPlayerPosition(p2) ? 1: -1);
                return tmp.stream().filter((p)->!p.getRetired()&&!p.getDisconnected()).toList();
            }
            case INVERSE: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1)>this.planche.getPlayerPosition(p2) ? -1: 1);
                return tmp.stream().filter((p)->!p.getRetired()&&!p.getDisconnected()).toList();
            }
        }
        return tmp;
    }

    public Player findCriteria(CombatZoneCriteria criteria){
        switch(criteria){
            case LEAST_CANNON:
                int min_cannon_power = Arrays.stream(this.players).mapToInt(p->p.getSpaceShip().getCannonPower()).min().orElse(0);
                return Arrays.stream(this.players)
                .filter((p)->p.getSpaceShip().getCannonPower()==min_cannon_power&&!p.getRetired()&&!p.getDisconnected())
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1)>this.planche.getPlayerPosition(p2) ? 1 : -1)
                .findFirst().orElse(null);
            case LEAST_CREW:
                int min_crew= Arrays.stream(this.players).mapToInt(p->p.getSpaceShip().getTotalCrew()).min().orElse(0);
                return Arrays.stream(this.players)
                .filter((p)->p.getSpaceShip().getTotalCrew()==min_crew&&!p.getRetired()&&!p.getDisconnected())
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1)>this.planche.getPlayerPosition(p2) ? 1 : -1)
                .findFirst().orElse(null);
            case LEAST_ENGINE:
                int min_engine_power = Arrays.stream(this.players).mapToInt(p->p.getSpaceShip().getEnginePower()).min().orElse(0);
                return Arrays.stream(this.players)
                .filter((p)->p.getSpaceShip().getEnginePower()==min_engine_power&&!p.getRetired()&&!p.getDisconnected())
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1)>this.planche.getPlayerPosition(p2) ? 1 : -1)
                .findFirst().orElse(null);
            default: return null;
        }
    }

    public iPlanche getPlanche(){
        return planche;
    }

    public void setCardState(CardState next) {
        if(next==null){
            for(Player p : this.to_give_up){
                if(!p.getRetired()) this.loseGame(p);
            }
            this.to_give_up.clear();
            iCard card = this.voyage_deck.pullCard();
            if(card==null) return;
            this.state = card.getState(this);
            this.state.init();
        }
        this.state = next;
        next.init();
    }

}
