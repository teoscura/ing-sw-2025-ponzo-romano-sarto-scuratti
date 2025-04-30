package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.RemoveComponentMessage;
import it.polimi.ingsw.message.server.SelectBlobMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.SetCrewMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

class VerifyStateTest {

    private DummyModelInstance model;
    private VerifyState state;
    private Player player1;
    private ClientDescriptor p1desc;
    private Player player2;
    private ClientDescriptor p2desc;
    private Player player3;
    private ClientDescriptor p3desc;

    @BeforeEach
    void setUp() {
        BaseComponent c;
        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();
        ComponentFactory f3 = new ComponentFactory();

        //player stabile
        player1 = new Player(GameModeType.LVL2, "player1", PlayerColor.RED);
        c = f1.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f1.getComponent(79);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f1.getComponent(26);
        c.rotate(ComponentRotation.U180);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 1));
        c = f1.getComponent(25);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 2));
        p1desc = new ClientDescriptor("player1", null);
        p1desc.bindPlayer(player1);

        //player ciambella
        player2 = new Player(GameModeType.LVL2, "player2", PlayerColor.BLUE);
        c = f2.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f2.getComponent(78);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f2.getComponent(67);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 1));
        c = f2.getComponent(58);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 1));
        c = f2.getComponent(43);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 2));
        c = f2.getComponent(59);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 3));
        c = f2.getComponent(23);
        c.rotate(ComponentRotation.U090);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 3));
        p2desc = new ClientDescriptor("player2", null);
        p2desc.bindPlayer(player2);

        //player manubrio
        player3 = new Player(GameModeType.LVL2, "player3", PlayerColor.GREEN);
        c = f3.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f3.getComponent(79);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f3.getComponent(26);
        c.rotate(ComponentRotation.U180);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 1));
        c = f3.getComponent(25);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 2));
        c = f3.getComponent(57);
        c.rotate(ComponentRotation.U090);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 2)); //componente da colpire per separare la nave a metà
        c = f3.getComponent(19);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 2));
        c = f3.getComponent(22);
        c.rotate(ComponentRotation.U270);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 1));
        c = f3.getComponent(70);
        c.rotate(ComponentRotation.U090);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 3));
        p3desc = new ClientDescriptor("player3", null);
        p3desc.bindPlayer(player3);

        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player2, player1, player3}));
        model = new DummyModelInstance(0, null, GameModeType.LVL2, PlayerCount.THREE);
        state = new VerifyState(model, GameModeType.LVL2, PlayerCount.THREE, players, new LevelTwoCards(), order);
    }

    @Test
    public void testAliveCabinChoice() throws ForbiddenCallException {
        ServerMessage message = null;
        model.setState(state);
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 1, 2));
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 1));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Selects blob, but takes a while deciding if he wants to switch the crew types.
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 5, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Player 3 is fast as hell and does everything before player2, it doesnt matter though.
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        //He selects his blob.
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 3, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        //Hes done.
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //P2 is done.
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        //State should be voyage.
        assertTrue(model.getState() instanceof VoyageState);
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player1)==GameModeType.LVL2.getLength()+6);
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player2)==GameModeType.LVL2.getLength()+3);
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player3)==GameModeType.LVL2.getLength()+1);
    }


    @Test
    public void testDeadCabinChoice() throws ForbiddenCallException {
        ServerMessage message = null;
        model.setState(state);
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 1, 2));
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 1));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Selects blob, but takes a while deciding if he wants to switch the crew types.
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 5, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Player 3 is fast as hell and does everything before player2, it doesnt matter though.
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        player3.getSpaceShip().printBlobs();
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 5, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        //Hes done.
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //P2 is done.
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        //State should be voyage.
        assertTrue(model.getState() instanceof VoyageState);
        ((VoyageState)model.getState()).getPlanche().printOrder();
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player1)==GameModeType.LVL2.getLength()+6);
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player2)==GameModeType.LVL2.getLength()+3);
        assertTrue(player3.getRetired());
    }

    @Test
    public void alienSetTest() throws ForbiddenCallException {
        var tmp = player1.getSpaceShip().bulkVerify();
        for(int i = 0; i < GameModeType.LVL2.getHeight(); i++){
            for(int y = 0; y < GameModeType.LVL2.getWidth(); y++){
                System.out.print(tmp[i][y]+" - "+new ShipCoords(GameModeType.LVL2, y, i)+" |");
            }
            System.out.println("");
        }
        //Adding life support component to p1;
        BaseComponent c;
        ComponentFactory f1 = new ComponentFactory();
        c = f1.getComponent(137);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 1));
        int x = player1.getSpaceShip().getTotalCrew();
        //Test start
        ServerMessage message = null;
        model.setState(state);
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 1, 2));
        message.setDescriptor(p1desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 1));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Removes the wrong ones
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Selects blob, but takes a while deciding if he wants to switch the crew types.
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 5, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //P3 tries setting crew before being allowed to.
        message = new SetCrewMessage(new ShipCoords(GameModeType.LVL2, 3, 2), AlienType.PURPLE);
        message.setDescriptor(p3desc);
        model.validate(message);
        //Player 3 is fast as hell and does everything before player2, it doesnt matter though.
        message = new RemoveComponentMessage(new ShipCoords(GameModeType.LVL2, 4, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        player3.getSpaceShip().printBlobs();
        message = new SelectBlobMessage(new ShipCoords(GameModeType.LVL2, 5, 2));
        message.setDescriptor(p3desc);
        model.validate(message);
        //Hes done.
        message = new SendContinueMessage();
        message.setDescriptor(p3desc);
        model.validate(message);
        //P2 is done.
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        //P1 tries to set aliens on a cabin that doesn't support them.
        message = new SetCrewMessage(new ShipCoords(GameModeType.LVL2, 3, 2), AlienType.PURPLE);
        message.setDescriptor(p1desc);
        model.validate(message);
        //P1 tries on a cabin that does support them this time.
        message = new SetCrewMessage(new ShipCoords(GameModeType.LVL2, 3, 1), AlienType.BROWN);
        message.setDescriptor(p1desc);
        model.validate(message);
        //Then continues.
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        //State should be voyage.
        assertTrue(model.getState() instanceof VoyageState);
        ((VoyageState)model.getState()).getPlanche().printOrder();
        assertEquals(x-1, player1.getSpaceShip().getTotalCrew());
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player1)==GameModeType.LVL2.getLength()+6);
        assertTrue(((VoyageState)model.getState()).getPlanche().getPlayerPosition(player2)==GameModeType.LVL2.getLength()+3);
        assertTrue(player3.getRetired());
    }

}