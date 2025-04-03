package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
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
    private final List<Player> to_give_up;
    private CardState state;

    //XXX finish implementing

    public VoyageState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players, iCards deck, iPlanche planche){
        super(model, type, count, players);
        if(deck==null||planche==null) throw new NullPointerException();
        this.to_give_up = new ArrayList<>();
        this.planche = planche;
        this.voyage_deck = deck;
        this.setCardState(null);
    }

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

    public void giveUp(ClientDescriptor client){
        if(client.getPlayer()==null) return;
        if(client.getPlayer().getRetired()==true){
            client.sendMessage(new ViewMessage("You have already retired/lost!"));
            return;
        }
        this.to_give_up.add(client.getPlayer());
    }

    public void loseGame(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=this.count.getNumber()) throw new PlayerNotFoundException("Player is not playing in current game");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has already lost or retired");
        this.players[c.getOrder()].retire();
    }

    public iPlanche getPlanche(){
        return planche;
    }

    @Override
    public void setCardState(CardState next) {
        if(next==null){
            for(Player p : this.to_give_up){
                p.retire();
            }
            this.to_give_up.clear();
            iCard card = this.voyage_deck.pullCard();
            if(card==null) this.transition();
            this.state = card.getState(this);
            this.state.init();
        }
        this.state = next;
        next.init();
    }

    @Override
    public GameState getNext(){
        return new EndscreenState(model, type, count, players);
    }
}
