package it.polimi.ingsw.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.server.EnterSetupMessage;
import it.polimi.ingsw.message.server.LeaveSetupMessage;
import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

import org.junit.jupiter.api.Test;

public class MainServerControllerTest {

    private MainServerController t;
    private ClientDescriptor p1, p2, p3, p4, p5, p6, s1, s2, s3, s4;

    @BeforeEach 
    void setUp(){
        t = MainServerController.getInstance();
        t.init("localhost", 0);
        t.start();
        p1 = new ClientDescriptor("p1", new DummyConnection());
        p2 = new ClientDescriptor("p2", new DummyConnection());
        p3 = new ClientDescriptor("p3", new DummyConnection());
        p4 = new ClientDescriptor("p4", new DummyConnection());
        p5 = new ClientDescriptor("p5", new DummyConnection());
        p6 = new ClientDescriptor("p6", new DummyConnection());
        s1 = new ClientDescriptor("s1", new DummyConnection());
        s2 = new ClientDescriptor("s2", new DummyConnection());
        s3 = new ClientDescriptor("s3", new DummyConnection());
        s4 = new ClientDescriptor("s4", new DummyConnection());
    }

    @Test
    void connectionTest() throws ForbiddenCallException, InterruptedException{
        ServerMessage mess = null;
        t.connect(p1);
        t.connect(p2);
        t.connect(p3);
        t.connect(p2);
        t.connect(p4);
        t.connect(p5);
        mess = new EnterSetupMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        mess = new EnterSetupMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        mess = new LeaveSetupMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        mess = new EnterSetupMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        assertEquals(0, t.getLobbyList().size());
        mess = new OpenLobbyMessage(GameModeType.TEST, PlayerCount.TWO);
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        Thread.sleep(100);
        assertEquals(1, p1.getId());
        assertEquals(1, t.getLobbyList().size());
    }
}


