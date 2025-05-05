package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.StructuralComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StardustCardTest {

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	private DummyModelInstance model;
	private DummyVoyageState state;
	private Planche planche;
	private StardustCard card;
	private CardState cstate;

	@BeforeEach
	void setUp() {
		player1 = new Player(GameModeType.TEST, "Player1", PlayerColor.RED);
		p1desc = new ClientDescriptor(player1.getUsername(), new DummyConnection());
		p1desc.bindPlayer(player1);
		player2 = new Player(GameModeType.TEST, "Player2", PlayerColor.BLUE);
		p2desc = new ClientDescriptor(player2.getUsername(), new DummyConnection());
		p2desc.bindPlayer(player2);
		player3 = new Player(GameModeType.TEST, "Player3", PlayerColor.GREEN);
		p3desc = new ClientDescriptor(player3.getUsername(), new DummyConnection());
		p3desc.bindPlayer(player3);

		StructuralComponent smooth1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 3, 1));
		StructuralComponent smooth2 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 4, 2));
		StructuralComponent smooth3 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 3, 3));
		StructuralComponent smooth4 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 2, 2));

		StructuralComponent full1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 3, 1));
		StructuralComponent full2 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 4, 2));
		StructuralComponent full3 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 3, 3));
		StructuralComponent full4 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.TEST, 2, 2));

		player2.getSpaceShip().addComponent(smooth1, new ShipCoords(GameModeType.TEST, 3, 1));
		player2.getSpaceShip().addComponent(smooth2, new ShipCoords(GameModeType.TEST, 4, 2));
		player2.getSpaceShip().addComponent(smooth3, new ShipCoords(GameModeType.TEST, 3, 3));
		player2.getSpaceShip().addComponent(smooth4, new ShipCoords(GameModeType.TEST, 2, 2));

		player3.getSpaceShip().addComponent(full1, new ShipCoords(GameModeType.TEST, 3, 1));
		player3.getSpaceShip().addComponent(full2, new ShipCoords(GameModeType.TEST, 4, 2));
		player3.getSpaceShip().addComponent(full3, new ShipCoords(GameModeType.TEST, 3, 3));
		player3.getSpaceShip().addComponent(full4, new ShipCoords(GameModeType.TEST, 2, 2));

		ArrayList<Player> order = new ArrayList<>(Arrays.asList(player1, player2, player3));
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3));
		
		model = new DummyModelInstance(1, GameModeType.LVL2, PlayerCount.THREE);
		model.setController(new DummyController(model.getID(), model));
		
		TestFlightCards cards = new TestFlightCards();
		planche = new Planche(GameModeType.LVL2, order);
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		model.setState(state);
		cstate = this.state.getCardState(player1);

		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (StardustCard) factory.getCard(4);
		player1.getSpaceShip().updateShip();
		player2.getSpaceShip().updateShip();
		player3.getSpaceShip().updateShip();
		cstate = card.getState(state);
		this.state.setCard(card);
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		assertEquals(30, planche.getPlayerPosition(player1));
		assertEquals(27, planche.getPlayerPosition(player2));
		assertEquals(25, planche.getPlayerPosition(player3));
		state.setCardState(cstate);
		//p1 confirms
		SendContinueMessage message1 = new SendContinueMessage();
		message1.setDescriptor(p1desc);
		state.validate(message1);
		//p1 tries to send again, is interrupted
		SendContinueMessage message1_wrong = new SendContinueMessage();
		message1_wrong.setDescriptor(p1desc);
		state.validate(message1_wrong);
		//player 2 disconnects
		model.disconnect(player2);
		//unexpected message sent
		TakeRewardMessage message3_wrong = new TakeRewardMessage(false);
		message3_wrong.setDescriptor(p3desc);
		assertThrows(ForbiddenCallException.class, () -> cstate.validate(message3_wrong));
		//player 3 sends confirm, card continues
		SendContinueMessage message3 = new SendContinueMessage();
		message3.setDescriptor(p3desc);
		state.validate(message3);
		assertNull(state.getCardState(player1));
		assertEquals(25, planche.getPlayerPosition(player1));
		assertEquals(27, planche.getPlayerPosition(player2));
		assertEquals(13, planche.getPlayerPosition(player3));
	}
}