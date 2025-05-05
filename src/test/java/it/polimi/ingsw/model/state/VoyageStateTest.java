package it.polimi.ingsw.model.state;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.PlayerGiveUpMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.DummyTestFlightCards;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class VoyageStateTest {
    
    private DummyModelInstance model;
    private VoyageState state;
    private Planche planche;
    private iCards deck;
    private Player player1;
    private ClientDescriptor p1desc;
    private Player player2;
    private ClientDescriptor p2desc;
    private Player player3;
    private ClientDescriptor p3desc;

    @BeforeEach
    void setUp(){
        ComponentFactory f = new ComponentFactory(); 
        BaseComponent c = null;
        //Has 1 engine power and doesnt retire
        player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
        c = f.getComponent(71);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        p1desc = new ClientDescriptor("p1", new DummyConnection());
        p1desc.bindPlayer(player1);
        //Has 1 engine power and retires
        player2 = new Player(GameModeType.TEST, "p2", PlayerColor.BLUE);
        c = f.getComponent(72);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        p2desc = new ClientDescriptor("p2", new DummyConnection());
        p2desc.bindPlayer(player2);
        //Has 0 engine power and retires
        player3 = new Player(GameModeType.TEST, "p3", PlayerColor.GREEN);
        p3desc = new ClientDescriptor("p3", new DummyConnection());
        p3desc.bindPlayer(player3);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        model = new DummyModelInstance(0, GameModeType.TEST, PlayerCount.THREE);
        model.setController(new DummyController(model.getID()));
        deck = new DummyTestFlightCards();
        planche = new Planche(GameModeType.TEST, players);
        state = new VoyageState(model, GameModeType.TEST, PlayerCount.THREE, players, deck, planche);   
        model.setState(state);
    }

    @Test
    void giveUpTests() throws ForbiddenCallException{
        ServerMessage message = null;
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //Everyone continues;
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //Lets check who retired;
        assertTrue(!player1.getRetired());
        assertTrue(player2.getRetired());
        assertTrue(player3.getRetired());
    }

    @Test
    void giveUpDisconnectTest() throws ForbiddenCallException{
        ServerMessage message = null;
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //Everyone continues;
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new ServerDisconnectMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //Lets check who retired;
        assertTrue(!player1.getRetired());
        assertTrue(player2.getRetired());
        assertTrue(player3.getRetired());
    }

    @Test
    void everyOneLosing() throws ForbiddenCallException {
        ServerMessage message = null;
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new PlayerGiveUpMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        //Everyone continues;
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new ServerDisconnectMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //Everyone lost, game ended
        assertTrue(model.getState() instanceof EndscreenState);
        assertTrue(player1.getRetired());
        assertTrue(player2.getRetired());
        assertTrue(player3.getRetired());
    }


}
