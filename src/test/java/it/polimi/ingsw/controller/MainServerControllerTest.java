package it.polimi.ingsw.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.server.EnterLobbyMessage;
import it.polimi.ingsw.message.server.EnterSetupMessage;
import it.polimi.ingsw.message.server.LeaveSetupMessage;
import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.message.server.OpenUnfinishedMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.OneFlightCards;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import org.junit.jupiter.api.Test;

public class MainServerControllerTest {

    private MainServerController t;
    private ClientDescriptor p1, p2, p3, p4, p5, s1, s2, s3;

    @BeforeEach
    void setUp(){
        p1 = new ClientDescriptor("p1", new DummyConnection());
        p2 = new ClientDescriptor("p2", new DummyConnection());
        p3 = new ClientDescriptor("p3", new DummyConnection());
        p4 = new ClientDescriptor("p4", new DummyConnection());
        p5 = new ClientDescriptor("p5", new DummyConnection());
        s1 = new ClientDescriptor("s1", new DummyConnection());
        s2 = new ClientDescriptor("s2", new DummyConnection());
        s3 = new ClientDescriptor("s3", new DummyConnection());
        MainServerController.reset();
    }

    @Test
    void connectionTest() throws ForbiddenCallException, InterruptedException{
        t = MainServerController.getInstance();
        t.init("localhost", 0);
        t.start();
        ServerMessage mess = null;
        t.connect(p1);
        t.connect(p2);
        t.connect(p3);
        t.connect(p2);
        t.connect(p4);
        t.connect(p5);
        t.connect(s1);
        t.connect(s2);
        t.connect(s3);
        int id = t.getNext();
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
        assertEquals(id, p1.getId());
        assertEquals(1, t.getLobbyList().size());
        mess = new EnterSetupMessage();
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        mess = new OpenLobbyMessage(GameModeType.LVL2, PlayerCount.FOUR);
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        Thread.sleep(100);
        assertEquals(id + 1, p2.getId());
        assertEquals(2, t.getLobbyList().size());
        mess = new EnterLobbyMessage(id +1);
        mess.setDescriptor(p3);
        t.receiveMessage(mess);
        mess = new EnterLobbyMessage(id +1);
        mess.setDescriptor(p4);
        t.receiveMessage(mess);
        mess = new EnterLobbyMessage(id +1);
        mess.setDescriptor(p5);
        t.receiveMessage(mess);
        //Spectator joins.
        mess = new EnterLobbyMessage(id +1);
        mess.setDescriptor(s1);
        t.receiveMessage(mess);
        Thread.sleep(100);
        //A dude opens a new Lobby from unfinished, but he cant.
        mess = new EnterSetupMessage();
        mess.setDescriptor(s2);
        t.receiveMessage(mess);
        Thread.sleep(100);
        mess = new OpenUnfinishedMessage(id-1);
        mess.setDescriptor(s2);
        t.receiveMessage(mess);
        Thread.sleep(100);
        t.disconnect(p1);
        Thread.sleep(100);
        t.connect(p1);
        assertEquals(-1, p1.getId());
        assertEquals(1, t.getLobbyList().size());
    }

    @Test
    void openUnfinishedSuccess() throws ForbiddenCallException, InterruptedException{
        t = MainServerController.getInstance();
        t.init("localhost", 0);
        t.start();
        ServerMessage mess = null;
        t.connect(p2);
        t.connect(p3);
        t.connect(p4);
        t.connect(p5);
        assertEquals(0, t.getLobbyList().size());
        mess = new EnterSetupMessage();
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        mess = new OpenUnfinishedMessage(2);
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        Thread.sleep(500);
        assertEquals(2, p2.getId());
        assertEquals(1, t.getLobbyList().size());
        t.disconnect(p2);
        Thread.sleep(300);
        //FIXME
        assertEquals(0, t.getLobbyList().size());
    }


    @Test
    void lobbyClose() throws ForbiddenCallException, InterruptedException{
        
        //XXX testare che EndingState poi chiude in modo pulito
        File saved = new File("gtunfinished-1.gtuf");
        if(saved.exists()){
            saved.delete();
        }

        //Create two ships with only one engine.
        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();
        BaseComponent c1 = f1.getComponent(74);
        BaseComponent c2 = f2.getComponent(74);
        Player pl1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
        Player pl2 = new Player(GameModeType.TEST, "p2", PlayerColor.BLUE);
        ClientDescriptor p1desc = new ClientDescriptor("p1", new DummyConnection());
        ClientDescriptor p2desc = new ClientDescriptor("p2", new DummyConnection());
        p1desc.bindPlayer(pl1);
        p2desc.bindPlayer(pl2);
        pl1.getSpaceShip().addComponent(c1, new ShipCoords(GameModeType.TEST, 3, 3));
        pl2.getSpaceShip().addComponent(c2, new ShipCoords(GameModeType.TEST, 3, 3));
        ArrayList<Player> list = new ArrayList<>(Arrays.asList(new Player[]{pl1,pl2}));
        //Create unfinished game
        LobbyController l = new LobbyController(1);
        ModelInstance model = new ModelInstance(1, new DummyController(1), GameModeType.TEST, PlayerCount.TWO);
        l.setModel(model);
        model.setController(l);
        model.setState(new VoyageState(model, GameModeType.TEST, PlayerCount.TWO, list, new OneFlightCards(), new Planche(GameModeType.TEST, list)));
        l.serializeCurrentGame();
        //open unfinished game, and finish playing it, lets see if it works.
        t = MainServerController.getInstance();
        t.init("localhost", 0);
        t.start();
        t.connect(p1);
        t.connect(p2);
        t.connect(p3);
        ServerMessage mess = new EnterSetupMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        mess = new OpenUnfinishedMessage(1);
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        Thread.sleep(100);
        assertEquals(1, t.getLobbyList().size());
        assertInstanceOf(VoyageState.class, l.getModel().getState());
        Thread.sleep(100);
        mess = new EnterLobbyMessage(1);
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        Thread.sleep(100);
        mess = new SendContinueMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        Thread.sleep(100);
        mess = new SendContinueMessage();
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        Thread.sleep(100);
        mess = new SendContinueMessage();
        mess.setDescriptor(p1);
        t.receiveMessage(mess);
        mess = new SendContinueMessage();
        mess.setDescriptor(p2);
        t.receiveMessage(mess);
        Thread.sleep(200);
        assertEquals(0, t.getLobbyList().size());

    }
}


