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
import it.polimi.ingsw.model.cards.state.SmugglersAnnounceState;
import it.polimi.ingsw.model.cards.state.SmugglersLoseState;
import it.polimi.ingsw.model.cards.state.SmugglersRewardState;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
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

public class SmugglersCardTest {

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
	private SmugglersCard card;

	@BeforeEach
	void setUp() throws IOException {
		BaseComponent c = null;
		ComponentFactory f1 = new ComponentFactory();
		ComponentFactory f2 = new ComponentFactory();
		ComponentFactory f3 = new ComponentFactory();

		//P1 puo vincere ma non accende, e perde di conseguenza.
		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f1.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f1.getComponent(18);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f1.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f1.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(118);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
		p1desc = new ClientDescriptor(player1.getUsername(), new DummyConnection());
		p1desc.bindPlayer(player1);

		//P2 accende e vince
		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
		c = f2.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f2.getComponent(31);
		c.rotate(ComponentRotation.U090);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f2.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f2.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f2.getComponent(118);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
		p2desc = new ClientDescriptor(player2.getUsername(), new DummyConnection());
		p2desc.bindPlayer(player2);

		//deve avere esattamente 4 per pareggiare.
		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.RED);
		c = f3.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f3.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f3.getComponent(128);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		p3desc = new ClientDescriptor(player3.getUsername(), new DummyConnection());
		p3desc.bindPlayer(player3);

		LevelOneCardFactory factory = new LevelOneCardFactory();
		order = new ArrayList<>(Arrays.asList(player1, player3, player2));
		players = new ArrayList<>(Arrays.asList(player1, player2, player3));

		model = new DummyModelInstance(1, GameModeType.LVL2, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));

		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		card = (SmugglersCard) factory.getCard(2);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		//assert che gli storage sian giusti.
		ServerMessage message = null;
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(player1.getSpaceShip(), ShipmentType.BLUE);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		assertArrayEquals(new int[]{3, 2, 0, 0, 0}, player1.getSpaceShip().getContains());
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 parte, ha perso, deve cedere.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SmugglersLoseState.class, state.getCardState(player1));
		//p1 prova a cedere qualcosa che non ha.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 2, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 prova a cedere delle batterie prima di aver esaurito i container blu.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 3), ShipmentType.EMPTY);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 2, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 cede un blue
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p1desc);
		state.validate(message);
		//e va bene
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 cede un blue
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p1desc);
		state.validate(message);
		//e va bene
		assertArrayEquals(new int[]{3, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//Si torna in announce
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P3 vuole pareggiare
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(this.card.getExhausted());
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P2 accende, vince
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		assertInstanceOf(SmugglersRewardState.class, state.getCardState(player1));
		//He takes rewards.
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 0, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.GREEN);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 1, 1, 1, 0}, player2.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

	//Deve dare una batteria,
	@Test
	void behaviour2() throws ForbiddenCallException {
		//assert che gli storage sian giusti.
		ServerMessage message = null;
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(player1.getSpaceShip(), ShipmentType.BLUE);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 parte, ha perso, deve cedere.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SmugglersLoseState.class, state.getCardState(player1));
		//p1 prova a cedere qualcosa che non ha.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 prova a cedere delle batterie prima di aver esaurito i container blu.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 3), ShipmentType.EMPTY);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 cede un blue
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p1desc);
		state.validate(message);
		//e va bene
		assertArrayEquals(new int[]{3, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 cede una batteria
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 3), ShipmentType.EMPTY);
		message.setDescriptor(p1desc);
		state.validate(message);
		//e va bene
		assertArrayEquals(new int[]{2, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//Si torna in announce
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P3 vuole pareggiare
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(this.card.getExhausted());
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P2 accende, vince
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		assertInstanceOf(SmugglersRewardState.class, state.getCardState(player1));
		//He takes rewards.
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 0, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.GREEN);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 1, 1, 1, 0}, player2.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

	//Deve dare una batteria e poi ha finito.
	@Test
	void behaviour3() throws ForbiddenCallException {
		//assert che gli storage sian giusti.
		ServerMessage message = null;
		ContainsRemoveVisitor v = new ContainsRemoveVisitor(player1.getSpaceShip(), ShipmentType.EMPTY);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)).check(v);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)).check(v);
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 parte, ha perso, deve cedere.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SmugglersLoseState.class, state.getCardState(player1));
		//p1 prova a cedere qualcosa che non ha.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 prova a cedere delle batterie.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 3), ShipmentType.EMPTY);
		message.setDescriptor(p1desc);
		state.validate(message);
		//ha gia finito
		assertArrayEquals(new int[]{0, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//Si torna in announce
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P3 vuole pareggiare
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(this.card.getExhausted());
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P2 accende, vince
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		assertTrue(this.card.getExhausted());
		assertInstanceOf(SmugglersRewardState.class, state.getCardState(player1));
		//He takes rewards.
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 0, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.GREEN);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 0, 1, 1, 0}, player2.getSpaceShip().getContains());
		message = new TakeCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertArrayEquals(new int[]{1, 1, 1, 1, 0}, player2.getSpaceShip().getContains());
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
		//assert che gli storage sian giusti.
		ServerMessage message = null;
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(player1.getSpaceShip(), ShipmentType.BLUE);
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 parte, ha perso, deve cedere.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SmugglersLoseState.class, state.getCardState(player1));
		//p1 prova a cedere qualcosa che non ha.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.YELLOW);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 prova a cedere delle batterie prima di aver esaurito i container blu.
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 3), ShipmentType.EMPTY);
		message.setDescriptor(p1desc);
		state.validate(message);
		//succede niente
		assertArrayEquals(new int[]{3, 1, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 cede un blue
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p1desc);
		state.validate(message);
		//e va bene
		assertArrayEquals(new int[]{3, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//p1 disconnette
		model.disconnect(player1);
		//pure p2
		model.disconnect(player2);
		//e non perde niente
		assertArrayEquals(new int[]{3, 0, 0, 0, 0}, player1.getSpaceShip().getContains());
		//Si torna in announce
		assertFalse(this.card.getExhausted());
		assertInstanceOf(SmugglersAnnounceState.class, state.getCardState(player1));
		//P3 vuole pareggiare
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		assertArrayEquals(new int[]{1, 0, 0, 0, 0}, player3.getSpaceShip().getContains());
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(this.card.getExhausted());
		assertNull(state.getCardState(player1));
	}


}
