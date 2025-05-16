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
import it.polimi.ingsw.model.cards.state.SlaversAnnounceState;
import it.polimi.ingsw.model.cards.state.SlaversLoseState;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
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

public class SlaversCardTest {

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
	private SlaversCard card;

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
		c = f1.getComponent(35);
		c.rotate(ComponentRotation.U180);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
		c = f1.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f1.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(128);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
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
		c = f2.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f2.getComponent(132);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f2.getComponent(128);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f2.getComponent(118);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
		p2desc = new ClientDescriptor(player2.getUsername(), new DummyConnection());
		p2desc.bindPlayer(player2);

		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.RED);
		c = f3.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f3.getComponent(126);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
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
		card = (SlaversCard) factory.getCard(1);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		CrewRemoveVisitor v = new CrewRemoveVisitor(player1.getSpaceShip());
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
		ServerMessage message = null;

		assertEquals(3, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 non accende, ha perso.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Confermo
		assertTrue(player1.getRetired());
		//P3 accende e pareggia.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Confermo
		assertFalse(player2.getRetired());
		//Tocca a p2 che accende e vince.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//P2 sceglie di prenderlo, perde giorni ma guadagna crediti.
		int x = state.getPlanche().getPlayerPosition(player2);
		int y = player2.getCredits();
		message = new TakeRewardMessage(true);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(x - card.getDays(), state.getPlanche().getPlayerPosition(player2));
		assertEquals(y + card.getCredits(), player2.getCredits());
		assertNull(state.getCardState(player1));
		assertTrue(player1.getRetired());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
	}

	@Test
	void behaviour2() throws ForbiddenCallException {
		CrewRemoveVisitor v = new CrewRemoveVisitor(player1.getSpaceShip());
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
		ServerMessage message = null;

		assertEquals(3, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 non accende, ha perso.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Confermo
		assertTrue(player1.getRetired());
		//P3 accende e pareggia.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Confermo
		assertFalse(player2.getRetired());
		//Tocca a p2 che accende e vince.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//P2 sceglie di prenderlo, perde giorni ma guadagna crediti.
		int x = state.getPlanche().getPlayerPosition(player2);
		int y = player2.getCredits();
		message = new TakeRewardMessage(false);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(x, state.getPlanche().getPlayerPosition(player2));
		assertEquals(y, player2.getCredits());
		assertNull(state.getCardState(player1));
		assertTrue(player1.getRetired());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
	}

	@Test
	void behaviour3() throws ForbiddenCallException {
		ServerMessage message = null;

		assertEquals(4, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 non accende, ha perso.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SlaversLoseState.class, state.getCardState(player1));
		//P1 rimuove tutti nella cabina sotto e uno al centro
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 2, 3));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 2, 3));
		message.setDescriptor(p1desc);
		state.validate(message);
		//Tenta di gabulare, errore
		ServerMessage w2 = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 2, 3));
		w2.setDescriptor(p1desc);
		state.validate(w2);
		//Finalmente toglie l'ultimo, e' ancora vivo, ma ha solo 1 crew.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		assertEquals(1, player1.getSpaceShip().getTotalCrew());
		assertInstanceOf(SlaversAnnounceState.class, state.getCardState(player1));
		//P3 accende e pareggia.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Confermo
		assertFalse(player2.getRetired());
		//Tocca a p2 che accende e vince.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//P2 sceglie di prenderlo, perde giorni ma guadagna crediti.
		int x = state.getPlanche().getPlayerPosition(player2);
		int y = player2.getCredits();
		message = new TakeRewardMessage(true);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(x - card.getDays(), state.getPlanche().getPlayerPosition(player2));
		assertEquals(y + card.getCredits(), player2.getCredits());
		assertNull(state.getCardState(player1));
		assertEquals(1, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
		ServerMessage message = null;

		assertEquals(4, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
		//p2 prova a partire, non puo'
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//p1 non accende, ha perso.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertInstanceOf(SlaversLoseState.class, state.getCardState(player1));
		//P1 rimuove uno nella cabina, poi si disconnette e salta il resto della punizione.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 2, 3));
		message.setDescriptor(p1desc);
		state.validate(message);
		model.disconnect(player1);
		assertEquals(3, player1.getSpaceShip().getTotalCrew());
		assertInstanceOf(SlaversAnnounceState.class, state.getCardState(player1));
		//P3 accende e pareggia.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p3desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Confermo
		assertFalse(player2.getRetired());
		//Tocca a p2 che accende e vince.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 4, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 2), new ShipCoords(GameModeType.TEST, 3, 3));
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		//P2 si disconnette, bruciando il reward e non subendo penalita';
		int x = state.getPlanche().getPlayerPosition(player2);
		int y = player2.getCredits();
		model.disconnect(player2);
		assertEquals(x, state.getPlanche().getPlayerPosition(player2));
		assertEquals(y, player2.getCredits());
		assertNull(state.getCardState(player1));
		assertEquals(3, player1.getSpaceShip().getTotalCrew());
		assertEquals(2, player2.getSpaceShip().getTotalCrew());
		assertEquals(2, player3.getSpaceShip().getTotalCrew());
	}

}
