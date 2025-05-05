package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerConnectMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.DummyVoyageState;
import it.polimi.ingsw.model.state.ResumeWaitingState;

public class ModelInstanceTest {
    //TOdo: test serial restart.

    @Test
    void resumeFromSerial() throws ForbiddenCallException{
        ServerMessage mess = null;
        ModelInstance model = new ModelInstance(0, GameModeType.TEST, PlayerCount.TWO);
        model.setController(new DummyController(model.getID(), model));
        Player player1 = new Player(GameModeType.TEST, "bibo1", PlayerColor.RED);
        ClientDescriptor p1desc = new ClientDescriptor("bibo1", null);
        p1desc.bindPlayer(player1);
        Player player2 = new Player(GameModeType.TEST, "bibo2", PlayerColor.BLUE);
        ClientDescriptor p2desc = new ClientDescriptor("bibo2", null);
        p2desc.bindPlayer(player2);
        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1,player2}));
        Planche planche = new Planche(GameModeType.TEST, order);
        iCards deck = new TestFlightCards();
        DummyVoyageState state = new DummyVoyageState(model, GameModeType.TEST, PlayerCount.TWO, order, deck, planche);
        model.setState(state);
        assertTrue(model.getState() instanceof DummyVoyageState);
        model.afterSerialRestart();
        assertTrue(model.getState() instanceof ResumeWaitingState);
        //Now that we're restarting, lets create new descriptors for the same usernames.
        ClientDescriptor p1descagain = new ClientDescriptor("bibo1", null);
        ClientDescriptor p2descagain = new ClientDescriptor("bibo2", null);
        mess = new ServerConnectMessage(p1descagain);
        model.validate(mess);
        mess = new ServerDisconnectMessage();
        mess.setDescriptor(p1descagain);
        model.validate(mess);
        mess = new ServerConnectMessage(p2descagain);
        model.validate(mess);
        mess = new ServerConnectMessage(p1descagain);
        model.validate(mess);
        assertTrue(model.getState() instanceof DummyVoyageState);
    }

}
