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
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.*;

public class ModelInstance {

    private final PlayerCount count;
    private final iCommonBoard board;
    private final iSpaceShip[] ships;
    private final boolean[] lost;
    private final iPlanche planche;
    private final int[] construction_cards;
    private final iCards voyage_deck;
    
    public ModelInstance(GameModeType type, PlayerCount count){
        this.count = count;
        this.board = new CommonBoard();
        this.ships = new iSpaceShip[count.getNumber()];
        this.lost = new boolean[count.getNumber()];
        Arrays.fill(this.lost, false);
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()>=ships.length) break;
            this.ships[c.getOrder()] = new SpaceShip(type, c);
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

    public List<iSpaceShip> getOrder(CardOrder order){
        //TODO filtrare gli abbandonati.
        List<iSpaceShip> tmp = Arrays.asList(this.ships);
        switch(order){
            case NORMAL: {
                Collections.sort(tmp, (s1,s2) -> this.planche.getPlayerPosition(s1)>this.planche.getPlayerPosition(s2) ? 1: -1);
                return tmp.stream().filter((s)->!this.lost[s.getColor().getOrder()]).toList();
            }
            case INVERSE: {
                Collections.sort(tmp, (s1,s2) -> this.planche.getPlayerPosition(s1)>this.planche.getPlayerPosition(s2) ? -1: 1);
                return tmp.stream().filter((s)->!this.lost[s.getColor().getOrder()]).toList();
            }
            case COMBATZONE: {
                throw new UnsupportedOperationException("Under no case should getOrder be called with CombatZone");
            }
            case METEORS: throw new UnsupportedOperationException("Under no case should getOrder be called with Meteors");
        }
        return tmp;
    }

    //TODO method find each criteria.

    public void loseGame(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=this.count.getNumber()) throw new PlayerNotFoundException("Player is not playing in current game");
        if(this.lost[c.getOrder()]) throw new PlayerNotFoundException("Player has already lost");
        this.lost[c.getOrder()] = true;
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

    public iSpaceShip getPlayer(PlayerColor c) throws PlayerNotFoundException{
        if(c.getOrder()>=ships.length) throw new PlayerNotFoundException("Player color is not present in this match");
        if(this.lost[c.getOrder()]) throw new PlayerNotFoundException("Player has lost the game");
        return this.ships[c.getOrder()];
    }

    public iPlanche getPlanche(){
        return planche;
    }

    public iCard pickCard(){
        return this.voyage_deck.pullCard();
    }
}
