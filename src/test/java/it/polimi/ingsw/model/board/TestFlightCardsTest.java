package it.polimi.ingsw.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.cards.iCard;

public class TestFlightCardsTest {
    
    private TestFlightCards cards;

    @BeforeEach
    public void setup(){
        this.cards = new TestFlightCards();
    }

    @Test
    public void test(){
        assertNull(this.cards.getConstructionCards());
        assertEquals(8, cards.getLeft());
        assertInstanceOf(iCard.class ,cards.pullCard());
    }

}
