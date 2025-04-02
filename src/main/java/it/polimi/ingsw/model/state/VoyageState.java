package it.polimi.ingsw.model.state;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CombatZoneCriteria;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iPlanche;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class VoyageState extends GameState {
    
    private final iPlanche planche;
    private final iCards voyage_deck;
    private int turn;
    private CardState state;

    public List<Player> getOrder(CardOrder order){
        List<Player> tmp = Arrays.asList(this.players);
        switch(order){
            case NORMAL: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? 1: -1);
                return tmp.stream().filter((p)->!p.getRetired()&&!p.getDisconnected()).toList();
            }
            case INVERSE: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? -1: 1);
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
                .filter((p)->p.getSpaceShip().getCannonPower()==min_cannon_power)
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? 1 : -1)
                .findFirst().orElse(null);
            case LEAST_CREW:
                int min_crew= Arrays.stream(this.players).mapToInt(p->p.getSpaceShip().getTotalCrew()).min().orElse(0);
                return Arrays.stream(this.players)
                .filter((p)->p.getSpaceShip().getTotalCrew()==min_crew)
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? 1 : -1)
                .findFirst().orElse(null);
            case LEAST_ENGINE:
                int min_engine_power = Arrays.stream(this.players).mapToInt(p->p.getSpaceShip().getEnginePower()).min().orElse(0);
                return Arrays.stream(this.players)
                .filter((p)->p.getSpaceShip().getEnginePower()==min_engine_power)
                .sorted((p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? 1 : -1)
                .findFirst().orElse(null);
            default: return null;
        }
    }

    public void loseGame(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=model.getCount().getNumber()) throw new PlayerNotFoundException("Player is not playing in current game");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has already lost or retired");
        this.players[c.getOrder()].retire();
    }

    public iPlanche getPlanche(){
        return planche;
    }

    public iCard pickCard(){
        return this.voyage_deck.pullCard();
    }

    @Override
    public void setCardState(CardState next) {
        if(next==null){
            iCard card = this.voyage_deck.pullCard();
            if(card==null) this.transition();
            this.state = card.getState(this);
            this.state.init(); 
            this.turn++;
        }
        this.state = next;
        next.init();
    }
}
