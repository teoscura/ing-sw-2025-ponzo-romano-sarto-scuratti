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
        player1 = new Player(GameModeType.LVL2, "Player1", PlayerColor.RED);
        player2 = new Player(GameModeType.LVL2, "Player2", PlayerColor.BLUE);
        player3 = new Player(GameModeType.LVL2, "Player3", PlayerColor.GREEN);
        p1desc = new ClientDescriptor(player1.getUsername(), null);
        p1desc.bindPlayer(player1);
        p2desc = new ClientDescriptor(player2.getUsername(), null);
        p2desc.bindPlayer(player2);
        p3desc = new ClientDescriptor(player3.getUsername(), null);
        p3desc.bindPlayer(player3);
    }

    @Test
    void behaviour() throws ForbiddenCallException {
        //player1 connects
        waiting_state.connect(p1desc);
        //player1 attempts connection again
        waiting_state.connect(p1desc);
        //player 2 attempts to diconnect before being connected
        waiting_state.disconnect(p2desc);
        //player 2 connects
        waiting_state.connect(p2desc);

        //assertInstanceOf(ConstructionState.class, model.getState());
        waiting_state.connect(p3desc);
        //waiting_state.transition();
    }
}