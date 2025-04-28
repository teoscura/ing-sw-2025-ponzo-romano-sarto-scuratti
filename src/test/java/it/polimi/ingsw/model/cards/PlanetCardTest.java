package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PlanetCardTest {

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
	private PlanetCard card;

	@BeforeEach
	void setUp() throws IOException {
		iBaseComponent c = null;
		ComponentFactory f = new ComponentFactory();

		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f.getComponent(18); //Double normal storage.
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f.getComponent(62); //Single special
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
		c = f.getComponent(24); //Double normal storage.
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f.getComponent(63); //Single special
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.RED);
		c = f.getComponent(22); //Double normal storage.
		c.rotate(ComponentRotation.U180);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f.getComponent(68); //Double special
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p3desc = new ClientDescriptor(player3.getUsername(), null);
		p3desc.bindPlayer(player3);

		LevelOneCardFactory factory = new LevelOneCardFactory();
		order = new ArrayList<>(Arrays.asList(player1, player2, player3));
		players = new ArrayList<>(Arrays.asList(player1, player2, player3));
		model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		card = (PlanetCard) factory.getCard(13);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		//Planet1(new int[]{0, 0, 0, 2}),
		//Planet2(new int[]{2, 0, 0, 1}),
		//Planet3(new int[]{0, 0, 1, 0}),
		ServerMessage mess = null;
		int x = 0;
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p1desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		//P1 lands on 0
		x = state.getPlanche().getPlayerPosition(player1);
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//Muove indietro di days
		assertEquals(state.getPlanche().getPlayerPosition(player1), x - card.getDays());
		//He takes a red, but places it wrong.
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//Lets validate nothing changed.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//Now he takes a red in the right spot.
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 4, 2), ShipmentType.RED);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//Lets validate the changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//P2 attempts to do something, lets see if something changes.
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 4, 2), ShipmentType.RED);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//p1 is done, next up is p2.
		x = this.planche.getPlayerPosition(player2);
		mess = new SendContinueMessage();
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//P1 lands on 0 - unallowed.
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//P2 lands on 1
		mess = new SelectLandingMessage(1);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Deve saltare p3, muove indietro di days. + 1
		assertEquals(state.getPlanche().getPlayerPosition(player2), x - this.card.getDays() - 1);
		//p2 takes a yellow, but its not there!
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//p2 takes a blue, but its not there!
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//p2 moves the blue!
		mess = new MoveCargoMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//E' stato effettivamente spostato.
		assertTrue(0 == ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2))).howMany(ShipmentType.BLUE) &&
				1 == ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2))).howMany(ShipmentType.BLUE));
		//Takes a red but in the wrong coords.
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//Stops.
		x = this.planche.getPlayerPosition(player3);
		mess = new SendContinueMessage();
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Player3 tests a non-existant planet;
		mess = new SelectLandingMessage(12);
		mess.setDescriptor(p3desc);
		state.validate(mess);
		assertEquals(state.getPlanche().getPlayerPosition(player3), x);
		//Player3 doesnt land.
		mess = new SelectLandingMessage(-1);
		mess.setDescriptor(p3desc);
		state.validate(mess);
		assertEquals(state.getPlanche().getPlayerPosition(player3), x);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 1}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
		//Planet1(new int[]{0, 0, 0, 2}),
		//Planet2(new int[]{2, 0, 0, 1}),
		//Planet3(new int[]{0, 0, 1, 0}),
		ServerMessage mess = null;
		int x = 0;
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p1desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		//P1 disconnects.
		x = state.getPlanche().getPlayerPosition(player1);
		state.disconnect(player1);
		assertEquals(x, state.getPlanche().getPlayerPosition(player1));
		//P1 lands on 0 - unallowed.
		x = state.getPlanche().getPlayerPosition(player2);
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		//P2 lands on 1
		mess = new SelectLandingMessage(1);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Ha dovuto saltare p3, quindi ora e' dietro di lui, muove indietro di days + 1
		assertEquals(state.getPlanche().getPlayerPosition(player2), x - card.getDays() - 1);
		//p2 takes a yellow, but its not there!
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Player3 disconnects
		x = this.planche.getPlayerPosition(player3);
		state.disconnect(player3);
		assertEquals(state.getPlanche().getPlayerPosition(player3), x);
		//Lets validate the changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//p2 takes a blue, but its not there!
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//p2 moves the blue!
		mess = new MoveCargoMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//E' stato effettivamente spostato.
		assertTrue(0 == ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2))).howMany(ShipmentType.BLUE) &&
				1 == ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2))).howMany(ShipmentType.BLUE));
		//Takes a red but in the wrong coords.
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		//Stops.
		mess = new SendContinueMessage();
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Lets validate the lack of changes.
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 1, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

}
