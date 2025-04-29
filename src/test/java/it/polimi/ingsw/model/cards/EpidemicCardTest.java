package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.BaseComponent;
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

class EpidemicCardTest {
	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	ServerMessage message;
	ServerMessage server_message_wrong;
	private DummyVoyageState state;
	private Planche planche;
	private EpidemicCard card;
	private CardState cstate;
	private DummyModelInstance model;

	@BeforeEach
	void setUp() throws IOException {

		player1 = new Player(GameModeType.TEST, "Player1", PlayerColor.RED);
		player2 = new Player(GameModeType.TEST, "Player2", PlayerColor.BLUE);

		ComponentFactory f = new ComponentFactory();
		BaseComponent c;
		player1 = new Player(GameModeType.TEST, "Player1", PlayerColor.RED);
		c = f.getComponent(53);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f.getComponent(38);
		c.rotate(ComponentRotation.U270);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
		c = f.getComponent(39);
		c.rotate(ComponentRotation.U180);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		c = f.getComponent(137);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 4));
		c = f.getComponent(49);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

		ArrayList<Player> order = new ArrayList<>(Arrays.asList(player1, player2));
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
		model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.TWO);
		TestFlightCards cards = new TestFlightCards();
		planche = new Planche(GameModeType.LVL2, order);
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.TWO, players, cards, planche);
		model.setState(state);

		LevelTwoCardFactory factory = new LevelTwoCardFactory();
		card = (EpidemicCard) factory.getCard(105);
		state.setCard(card);
		cstate = card.getState(state);

	}

	@Test
	void test() throws ForbiddenCallException {
		// Check status before card application
		int[] exp1 = new int[]{8, 0, 0};
		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		state.setCardState(cstate);
		// Check status after card application
		exp1 = new int[]{6, 0, 0};
		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		ShipCoords coords = new ShipCoords(GameModeType.TEST, 4, 3);
		CabinComponent cabin = (CabinComponent) player1.getSpaceShip().getComponent(coords);
		assertEquals(1, cabin.getCrew());
		coords = new ShipCoords(GameModeType.TEST, 5, 3);
		cabin = (CabinComponent) player1.getSpaceShip().getComponent(coords);
		assertEquals(1, cabin.getCrew());
		// Unsupported message (it should fail)
		server_message_wrong = new TakeRewardMessage(false);
		server_message_wrong.setDescriptor(p1desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(server_message_wrong));
		// Send message for both players
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		// try again (it should fail)
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilience() throws ForbiddenCallException {
		// Check status before card application
		int[] exp1 = new int[]{8, 0, 0};
		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		state.setCardState(cstate);
		// Check status after card application
		exp1 = new int[]{6, 0, 0};
		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		ShipCoords coords = new ShipCoords(GameModeType.TEST, 4, 3);
		CabinComponent cabin = (CabinComponent) player1.getSpaceShip().getComponent(coords);
		assertEquals(1, cabin.getCrew());
		coords = new ShipCoords(GameModeType.TEST, 5, 3);
		cabin = (CabinComponent) player1.getSpaceShip().getComponent(coords);
		assertEquals(1, cabin.getCrew());

		// Unsupported message (it should fail)
		server_message_wrong = new TakeRewardMessage(false);
		server_message_wrong.setDescriptor(p1desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(server_message_wrong));

		// Send message for both players
		model.disconnect(player1);
		// try again (it should fail)
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		// Woop
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		assertNull(state.getCardState(player1));
	}
}
