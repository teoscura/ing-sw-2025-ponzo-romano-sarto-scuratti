package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;

public class MeteorSwarmCardTest {
    
    private DummyModelInstance model;
	private DummyVoyageState state;
	private TestFlightCards cards;
	private Planche planche;
	private MeteorSwarmCard card;

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	ArrayList<Player> order, players;

    @BeforeEach
    void setUp(){
        //Scudi parano piccole/navi lisce pure.
        //Cannoni parano grossi.
        //P1 puo vincere ma non accende, e perde di conseguenza.

        iBaseComponent c = null;
        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();

        player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
        c = f1.getComponent(101);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
        c = f1.getComponent(53);
        c.rotate(ComponentRotation.U270);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
        c = f1.getComponent(1);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f1.getComponent(152);
        c.rotate(ComponentRotation.U270);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 1));
        c = f1.getComponent(102);
        c.rotate(ComponentRotation.U270);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
        c = f2.getComponent(101);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
        c = f2.getComponent(53);
        c.rotate(ComponentRotation.U270);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
        c = f2.getComponent(1);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f2.getComponent(152);
        c.rotate(ComponentRotation.U270);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 1));
        c = f2.getComponent(102);
        c.rotate(ComponentRotation.U270);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

		order = new ArrayList<>(Arrays.asList(player1, player2));
		players = new ArrayList<>(Arrays.asList(player1, player2));
		model = new DummyModelInstance(1, null, GameModeType.TEST, PlayerCount.TWO);
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.TWO, players, cards, planche);
    }

    @Test
    void behaviour1() throws ForbiddenCallException{
        //3 meteorites, first one big, front left right.
        player1.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 2, 2));
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (MeteorSwarmCard) factory.getCard(9);
		model.setState(state);
		state.setCard(card);
        ArrayList<Projectile> meteors = card.getMeteorites();
        Projectile pr = meteors.get(0);
        meteors.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(1);
        meteors.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(2);
        meteors.set(2, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		state.setCardState(card.getState(state));
        ServerMessage message = null;
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        //phase 2 - p1 does nothing.
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
        iBaseComponent c1 = player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2));
        iBaseComponent c2 = player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2));
        iBaseComponent c3 = player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2));
        //Turns on shield
        message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 1), new ShipCoords(GameModeType.TEST, 3, 3));
        message.setDescriptor(p2desc);
        model.validate(message);
        //Continues
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        assertTrue(player1.getRetired());
        //p2 needs to do nothing, since its smooth.
        assertEquals(player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)), c1);
        assertEquals(player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)), c2);
        assertEquals(player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)), c3);
        //P2 is alone, continues,
        message = new SendContinueMessage();
        message.setDescriptor(p2desc);
        model.validate(message);
        player2.getSpaceShip().resetPower();
        assertArrayEquals(player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).getConnectors(), c2.getConnectors());
        assertArrayEquals(player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).getConnectors(), c3.getConnectors());
        //done.
        assertNull(state.getCardState(player1));
        assertTrue(!player2.getRetired());
    }

    @Test
    void behaviour2() throws ForbiddenCallException{
        //5 meteorites, 3rd one big, front front, left left left.
        LevelTwoCardFactory factory = new LevelTwoCardFactory();
        card = (MeteorSwarmCard) factory.getCard(109);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
        ArrayList<Projectile> meteors = card.getMeteorites();
        Projectile pr = meteors.get(0);
        meteors.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(1);
        meteors.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(2);
        meteors.set(2, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(3);
        meteors.set(3, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        pr = meteors.get(4);
        meteors.set(4, new Projectile(pr.getDirection(), pr.getDimension(), 7));
        ServerMessage message = null;
        message = new SendContinueMessage();
        message.setDescriptor(p1desc);
        model.validate(message);
    }
}
