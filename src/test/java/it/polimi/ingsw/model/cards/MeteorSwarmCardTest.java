package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MeteorSwarmCardTest {

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	ArrayList<Player> order, players;
	private DummyModelInstance model;
	private DummyVoyageState state;
	private TestFlightCards cards;
	private Planche planche;
	private MeteorSwarmCard card;

	@BeforeEach
	void setUp() {
		//Scudi parano piccole/navi lisce pure.
		//Cannoni parano grossi.
		//P1 puo vincere ma non accende, e perde di conseguenza.

		BaseComponent c = null;
		ComponentFactory f1 = new ComponentFactory();
		ComponentFactory f2 = new ComponentFactory();

		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f1.getComponent(101);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f1.getComponent(53);
		c.rotate(ComponentRotation.U270);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(4);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f1.getComponent(152);
		c.rotate(ComponentRotation.U270);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 1));
		c = f1.getComponent(102);
		c.rotate(ComponentRotation.U270);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		p1desc = new ClientDescriptor(player1.getUsername(), new DummyConnection());
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
		p2desc = new ClientDescriptor(player2.getUsername(), new DummyConnection());
		p2desc.bindPlayer(player2);

		order = new ArrayList<>(Arrays.asList(player1, player2));
		players = new ArrayList<>(Arrays.asList(player1, player2));
		model = new DummyModelInstance(1, GameModeType.TEST, PlayerCount.TWO);
		model.setController(new DummyController(model.getID()));
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.TWO, players, cards, planche);
	}

	@Test
	void behaviour1() throws ForbiddenCallException {
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
		//Turns on shield
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		model.validate(message);
		//Continues
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		assertFalse(player1.getRetired());
		//p1 needs to choose its new ship section
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 4, 1));
		message.setDescriptor(p1desc);
		model.validate(message);
		//p2 needs to do nothing, since its smooth.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		player2.getSpaceShip().resetPower();
		assertNotEquals(player2.getSpaceShip().getEmpty(), player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)));
		//done.
		assertNull(state.getCardState(player1));
		assertFalse(player2.getRetired());
	}

	@Test
	void behaviour2() throws ForbiddenCallException {
		//5 meteorites, 3rd one big, front front, left left left.
		player1.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 2, 2));
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
		meteors.set(2, new Projectile(pr.getDirection(), pr.getDimension(), 6));
		pr = meteors.get(3);
		meteors.set(3, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = meteors.get(4);
		meteors.set(4, new Projectile(pr.getDirection(), pr.getDimension(), 8));
		ServerMessage message = null;
		//Both are fine. 1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//Both are fine. 2
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//P1 is hurt and loses cannon, p2 is fine.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//Both block the first small left one
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//P1 loses component, doesnt need to block, p2 is smooht and doesnt.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//last checks.
		assertEquals(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 1)));
		assertEquals(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)));
		assertNull(state.getCardState(player1));
		assertFalse(player1.getRetired());
		assertFalse(player2.getRetired());
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
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
		meteors.set(3, new Projectile(pr.getDirection(), pr.getDimension(), 8));
		pr = meteors.get(4);
		meteors.set(4, new Projectile(pr.getDirection(), pr.getDimension(), 8));
		//P2 doesn't need to do anything to make it work.
		ServerMessage message = new ServerDisconnectMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		assertTrue(player1.getDisconnected());
		//P2 takes a bunch of meteors.
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		assertTrue(player1.getDisconnected());
		//P2 takes a bunch of meteors.
		model.connect(player1);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//P2 takes a bunch of meteors.
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		//Last two.
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		//Last one.
		message = new ServerDisconnectMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
	}

}
