package it.polimi.ingsw.model.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeComponentMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class ConstructionStateTest {
    
    private DummyModelInstance model;
    private Player player1;
    private ClientDescriptor p1desc;
    private Player player2;
    private ClientDescriptor p2desc;

    @BeforeEach
    void setUp() {
        model = new DummyModelInstance(1, null, GameModeType.TEST, PlayerCount.TWO);
        player1 = new Player(GameModeType.TEST, "Gigio1", PlayerColor.RED);
        p1desc = new ClientDescriptor("Gigio1", null);
        p1desc.bindPlayer(player1);
        player2 = new Player(GameModeType.TEST, "Gigio2", PlayerColor.BLUE);
        p2desc = new ClientDescriptor("Gigio2", null);
        p2desc.bindPlayer(player2);
    }

    @Test
    void testFlightConstruction() throws ForbiddenCallException{
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2}));
        TestFlightConstructionState state = new TestFlightConstructionState(model, GameModeType.TEST, PlayerCount.TWO, players);
        model.setState(state);
        ServerMessage mess = null;
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        int id = state.getCurrent(player1).getID();
        mess = new TakeComponentMessage();
        mess.setDescriptor(p2desc);
        model.validate(mess);
        mess = new DiscardComponentMessage(id);
        mess.setDescriptor(p1desc);
        model.validate(mess);
        assertTrue(state.getDiscarded().contains(id));
        mess = new TakeDiscardedComponentMessage(id);
        mess.setDescriptor(p2desc);
        model.validate(mess);
        assertEquals(state.getCurrent(player2).getID(), id);
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        //Auto discard test.
        System.out.println(Arrays.toString(state.getHoarded(player1).stream().map(c->c.getID()).toArray())); 
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        System.out.println(Arrays.toString(state.getHoarded(player1).stream().map(c->c.getID()).toArray()));
        int test = state.getHoarded(player1).getLast().getID(); 
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        assertTrue(!state.getDiscarded().contains(test));
        System.out.println(Arrays.toString(state.getHoarded(player1).stream().map(c->c.getID()).toArray()));
        test = state.getHoarded(player1).getLast().getID(); 
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        assertTrue(state.getDiscarded().contains(test));
        System.out.println(Arrays.toString(state.getHoarded(player1).stream().map(c->c.getID()).toArray()));
        test = state.getHoarded(player1).getLast().getID(); 
        mess = new TakeComponentMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        assertTrue(state.getDiscarded().contains(test));
        System.out.println(Arrays.toString(state.getHoarded(player1).stream().map(c->c.getID()).toArray()));
        mess = new SendContinueMessage();
        mess.setDescriptor(p2desc);
        model.validate(mess);
        mess = new TakeDiscardedComponentMessage(test);
        mess.setDescriptor(p2desc);
        model.validate(mess);
        int test2 = test;
        assertTrue(state.getHoarded(player2).stream().filter(c->c.getID()==test2).toList().isEmpty());
        mess = new SendContinueMessage();
        mess.setDescriptor(p1desc);
        model.validate(mess);
        assertTrue(model.getState() instanceof VerifyState);
    }

    
}
