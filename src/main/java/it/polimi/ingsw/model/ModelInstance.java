package it.polimi.ingsw.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.LevelOneCardFactory;
import it.polimi.ingsw.model.adventure_cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.*;

public class ModelInstance {
    
    private final GameModeType type;
    private final PlayerCount count;

    private final iCommonBoard board;
    private final Player[] players;
    private final iPlanche planche;

    private final int[] construction_cards;
    private final iCards voyage_deck;

    private GameState state;
    // private CardState state = null;
    // private int turn = 1;
    
    public ModelInstance(GameModeType type, PlayerCount count){
        this.type = type;
        this.count = count;
        this.board = new CommonBoard();
        this.players = new Player[count.getNumber()];
        // XXX this.lost = new boolean[count.getNumber()];
        // Arrays.fill(this.lost, false);
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()>=players.length) break;
            this.players[c.getOrder()] = new Player(type, c);
        }
        this.planche = new Planche(type, count);
        LevelOneCardFactory l1 = new LevelOneCardFactory();
        LevelTwoCardFactory l2 = new LevelTwoCardFactory();
        if(type.getLevel()==-1){
            ArrayList<iCard> test_cards = new ArrayList<>(){{
                add(l1.getCard(2));
                add(l1.getCard(4));
                add(l1.getCard(5));
                add(l1.getCard(9));
                add(l1.getCard(13));
                add(l1.getCard(16));
                add(l1.getCard(18));
                add(l1.getCard(19));
            }};
            Collections.shuffle(test_cards);
            this.construction_cards = null;
            this.voyage_deck = new Cards(test_cards);
        }
        else{
            ArrayList<iCard> lv1_cards = new ArrayList<>(){{
                for(int i=1;i<=20;i++) add(l1.getCard(i));
            }};
            ArrayList<iCard> lv2_cards = new ArrayList<>(){{
                for(int i=101;i<=120;i++) add(l2.getCard(i));
            }};
            Collections.shuffle(lv1_cards);
            ArrayDeque<iCard> lv1_queue = new ArrayDeque<>(lv1_cards);
            Collections.shuffle(lv2_cards);
            ArrayDeque<iCard> lv2_queue = new ArrayDeque<>(lv2_cards);
            ArrayList<iCard> tmp = new ArrayList<>(){{
                for(int i=0;i<4;i++){
                    add(lv2_queue.poll());
                    add(lv2_queue.poll());
                    add(lv1_queue.poll());
                }
            }};
            this.construction_cards = tmp.stream().mapToInt((c)->c.getId()).toArray();
            this.voyage_deck = new Cards(tmp);
        }
    }

    public List<Player> getOrder(CardOrder order){
        //TODO filtrare gli abbandonati.
        List<Player> tmp = Arrays.asList(this.players);
        switch(order){
            case NORMAL: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? 1: -1);
                return tmp.stream().filter((p)->!p.getRetired()).toList();
            }
            case INVERSE: {
                Collections.sort(tmp, (p1,p2) -> this.planche.getPlayerPosition(p1.getColor())>this.planche.getPlayerPosition(p2.getColor()) ? -1: 1);
                return tmp.stream().filter((p)->!p.getRetired()).toList();
            }
        }
        return tmp;
    }

    //TODO method find each criteria.

    public void loseGame(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=this.count.getNumber()) throw new PlayerNotFoundException("Player is not playing in current game");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has already lost or retired");
        this.players[c.getOrder()].retire();
    }

    public int[] getShowed(){
        return this.construction_cards;
    }

    public PlayerCount getCount(){
        return this.count;
    }

    public iCommonBoard getBoard(){
        return this.board;
    }

    public Player getPlayer(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=players.length) throw new PlayerNotFoundException("Player color is not present in this match");
        if(this.players[c.getOrder()].getRetired()) throw new PlayerNotFoundException("Player has lost or retired from the game");
        return this.players[c.getOrder()];
    }

    public iPlanche getPlanche(){
        return planche;
    }

    public iCard pickCard(){
        return this.voyage_deck.pullCard();
    }

    //XXX move inside voyagestate
    // public void setCardState(CardState next) {
    //     if(next==null){
    //         if(this.turn==this.type.getAmountOfCards()) endTheGame;
    //         this.state = this.voyage_deck.pullCard().getState();
    //         this.state.init(); 
    //         this.turn++;
    //     }
    //     this.state = next;
    //     next.init();
    // }
}
