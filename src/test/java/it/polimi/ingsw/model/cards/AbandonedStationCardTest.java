package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.AbandonedStationAnnounceState;
import it.polimi.ingsw.model.cards.state.AbandonedStationRewardState;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AbandonedStationCardTest {

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	ArrayList<Player> order, players;
	private DummyModelInstance model;
	private DummyVoyageState state;
	private Planche planche;
	private TestFlightCards cards;
	private AbandonedStationCard card;

	@BeforeEach
	void setup() {
		BaseComponent c = null;
		ComponentFactory f1 = new ComponentFactory();
		ComponentFactory f2 = new ComponentFactory();
		ComponentFactory f3 = new ComponentFactory();

		//Non puo salire.
		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f1.getComponent(18); //Double normal storage.
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f1.getComponent(62); //Single special
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

		//Puo salire
		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
		c = f2.getComponent(45);
		c.rotate(ComponentRotation.U270);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f2.getComponent(35);
		c.rotate(ComponentRotation.U090);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		c = f2.getComponent(24); //Double normal storage.
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f2.getComponent(63); //Single special
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

		//Puo salire.
		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.RED);
		c = f3.getComponent(45);
		c.rotate(ComponentRotation.U270);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f3.getComponent(35);
		c.rotate(ComponentRotation.U090);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		c = f3.getComponent(22); //Double normal storage.
		c.rotate(ComponentRotation.U180);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f3.getComponent(68); //Double special
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
		card = (AbandonedStationCard) factory.getCard(19);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		//Days 1, Crew 5, new int[]{0, 1, 1, 0})
		ServerMessage mess = null;
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		assertEquals(2, player1.getSpaceShip().getTotalCrew());
		assertEquals(6, player2.getSpaceShip().getTotalCrew());
		assertEquals(6, player3.getSpaceShip().getTotalCrew());
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		assertInstanceOf(AbandonedStationAnnounceState.class, state.getCardState(player1));
		mess = new SelectLandingMessage(-1);
		mess.setDescriptor(p1desc);
		state.validate(mess);
		assertInstanceOf(AbandonedStationAnnounceState.class, state.getCardState(player1));
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		assertInstanceOf(AbandonedStationRewardState.class, state.getCardState(player1));
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		assertArrayEquals(new int[]{0, 0, 0, 1, 0}, player2.getSpaceShip().getContains());
		ServerMessage w = new SelectLandingMessage(0);
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		mess = new MoveCargoMessage(new ShipCoords(GameModeType.TEST, 4, 2),
				new ShipCoords(GameModeType.TEST, 2, 2),
				ShipmentType.YELLOW);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		assertEquals(1, ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2))).howMany(ShipmentType.YELLOW));
		assertEquals(0, ((StorageComponent) player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2))).howMany(ShipmentType.YELLOW));
		assertArrayEquals(new int[]{0, 0, 0, 1, 0}, player2.getSpaceShip().getContains());
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.GREEN);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
		//Days 1, Crew 5, new int[]{0, 1, 1, 0})
		ServerMessage mess = null;
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		model.disconnect(player1);
		model.disconnect(player3);
		//Lands
		mess = new SelectLandingMessage(0);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//takes one
		mess = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.GREEN);
		mess.setDescriptor(p2desc);
		state.validate(mess);
		//Leaves
		mess = new SendContinueMessage();
		mess.setDescriptor(p2desc);
		state.validate(mess);
		assertNull(state.getCardState(player1));
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 1, 0, 0}, player2.getSpaceShip().getContains());
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
	}

}
