package it.polimi.ingsw.model.cards.utils;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanetTest {

    Planet planet;

    @Test
    void createPlanetCard() {
        try{
            planet = new Planet(new int[]{0, 0, 1});
            fail("Exception not thrown");
        }catch (IllegalArgumentException e){}
        try{
            planet = new Planet(new int[]{-1, 1, 2, 4});
            fail("Exception not thrown");
        }catch (NegativeArgumentException e2){}
    }

    @Test
    void getContains() {
    }

    @Test
    void visit() {
    }

    @Test
    void getVisited() {
    }
}