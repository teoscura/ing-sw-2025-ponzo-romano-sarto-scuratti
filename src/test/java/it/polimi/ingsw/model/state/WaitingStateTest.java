package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.ServerController;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingStateTest {
    DummyModelInstance model;
    WaitingState waiting_state;
    Player player1;
    ClientDescriptor p1desc;
    Player player2;
    ClientDescriptor p2desc;
    Player player3;
    ClientDescriptor p3desc;

    @BeforeEach
    void setUp() {
        model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
        waiting_state = new WaitingState(model, GameModeType.LVL2, PlayerCount.THREE);
        model.setState(waiting_state);
        p1desc = new ClientDescriptor("Gigio1", null);
        p2desc = new ClientDescriptor("Gigio2", null);
        p3desc = new ClientDescriptor("Gigio3", null);
    }

    @Test
    void behaviour() throws ForbiddenCallException {
        //player1 connects
        model.connect(p1desc);
        //player1 attempts connection again
        model.connect(p1desc);
        //player 2 attempts to diconnect before being connected
        model.disconnect(p2desc);
        //player 2 connects
        model.connect(p2desc);
        model.connect(p3desc);
        assertInstanceOf(ConstructionState.class, model.getState());
        //Did everyone generate with a player.
        assertNotNull(p1desc.getPlayer());
        assertEquals(p1desc.getPlayer().getColor(), PlayerColor.RED);
        assertEquals(p1desc.getUsername(), p1desc.getPlayer().getUsername());
        assertNotNull(p2desc.getPlayer());
        assertEquals(p2desc.getPlayer().getColor(), PlayerColor.BLUE);
        assertEquals(p2desc.getUsername(), p2desc.getPlayer().getUsername());
        assertNotNull(p3desc.getPlayer());
        assertEquals(p3desc.getPlayer().getColor(), PlayerColor.GREEN);
        assertEquals(p3desc.getUsername(), p3desc.getPlayer().getUsername());
    }
}