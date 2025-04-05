package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

import it.polimi.ingsw.model.adventure_cards.LevelOneCardFactory;
import it.polimi.ingsw.model.adventure_cards.iCard;

public class TestFlightCards implements iCards {
    
    private final Queue<iCard> cards;

    public TestFlightCards(){
        LevelOneCardFactory l1 = new LevelOneCardFactory();
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
        this.cards = new ArrayDeque<iCard>(test_cards);
    }

    @Override
    public iCard pullCard(){
        return this.cards.poll();
    }

    @Override
    public int[] getConstructionCards(){
        return null;
    }

}