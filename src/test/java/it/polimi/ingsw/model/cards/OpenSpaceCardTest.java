package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
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
import it.polimi.ingsw.model.cards.state.CardState;
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

class OpenSpaceCardTest {

	Player dummy3;
	Player dummy4;
	Player dummy5;
	private DummyModelInstance model;
	private DummyVoyageState state;
	private Planche planche;
	private OpenSpaceCard card;
	private CardState cstate;

	@BeforeEach
	void setUp() {
		ComponentFactory f4 = new ComponentFactory();
		ComponentFactory f5 = new ComponentFactory();
		BaseComponent c = null;
		dummy3 = new Player(GameModeType.TEST, "player3scarso", PlayerColor.RED);

		dummy4 = new Player(GameModeType.TEST, "player4", PlayerColor.RED);
		c = f4.getComponent(96);
		c.rotate(ComponentRotation.U000);
		dummy4.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f4.getComponent(2);
		c.rotate(ComponentRotation.U000);
		dummy4.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));

		dummy5 = new Player(GameModeType.TEST, "player5", PlayerColor.RED);
		c = f5.getComponent(14);
		c.rotate(ComponentRotation.U090);
		dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f5.getComponent(75);
		c.rotate(ComponentRotation.U000);
		dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f5.getComponent(98);
		c.rotate(ComponentRotation.U000);
		dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));

		ArrayList<Player> order = new ArrayList<>(Arrays.asList(dummy3, dummy4, dummy5));
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(dummy3, dummy4, dummy5));

		model = new DummyModelInstance(1, GameModeType.LVL2, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));

		TestFlightCards cards = new TestFlightCards();
		planche = new Planche(GameModeType.LVL2, order);
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		model.setState(state);

		LevelTwoCardFactory factory = new LevelTwoCardFactory();
		card = (OpenSpaceCard) factory.getCard(106);
		state.setCard(card);

		dummy3.getSpaceShip().updateShip();
		dummy4.getSpaceShip().updateShip();
		dummy5.getSpaceShip().updateShip();
		cstate = card.getState(state);
		state.setCardState(cstate);
	}

	@Test
	void apply() throws ForbiddenCallException {
		int initialPos4 = state.getPlanche().getPlayerPosition(dummy4);
		int initialPos5 = state.getPlanche().getPlayerPosition(dummy5);
		System.out.println("initialPos4: " + initialPos4);
		System.out.println("initialPos5: " + initialPos5);
		ServerMessage turnOnDummy4 = new TurnOnMessage(
				new ShipCoords(GameModeType.TEST, 3, 3),
				new ShipCoords(GameModeType.TEST, 2, 2)
		);
		turnOnDummy4.setDescriptor(new ClientDescriptor(dummy4.getUsername(), new DummyConnection()) {{
			bindPlayer(dummy4);
		}});
		state.validate(turnOnDummy4);
		ServerMessage turnOnDummy5 = new TurnOnMessage(
				new ShipCoords(GameModeType.TEST, 4, 3),  // se metto 3 3 da il giusto errore cercando di accendere un motore singolo
				new ShipCoords(GameModeType.TEST, 2, 2)
		);
		turnOnDummy5.setDescriptor(new ClientDescriptor(dummy5.getUsername(), new DummyConnection()) {{
			bindPlayer(dummy5);
		}});
		state.validate(turnOnDummy5);

		ServerMessage continueDummy3 = new SendContinueMessage();
		continueDummy3.setDescriptor(new ClientDescriptor(dummy3.getUsername(), new DummyConnection()) {{
			bindPlayer(dummy3);
		}});
		state.validate(continueDummy3);

		ServerMessage continueDummy4 = new SendContinueMessage();
		continueDummy4.setDescriptor(new ClientDescriptor(dummy4.getUsername(), new DummyConnection()) {{
			bindPlayer(dummy4);
		}});
		state.validate(continueDummy4);

		ServerMessage continueDummy5 = new SendContinueMessage();
		continueDummy5.setDescriptor(new ClientDescriptor(dummy5.getUsername(), new DummyConnection()) {{
			bindPlayer(dummy5);
		}});
		state.validate(continueDummy5);

		assertTrue(dummy3.getRetired());

		assertFalse(dummy4.getRetired());
		assertTrue(dummy4.getSpaceShip().getEnginePower() > 0);

		assertFalse(dummy5.getRetired());
		assertEquals(3, dummy5.getSpaceShip().getEnginePower());

		int pos4 = state.getPlanche().getPlayerPosition(dummy4);
		int pos5 = state.getPlanche().getPlayerPosition(dummy5);
		System.out.println("Pos4: " + pos4);
		System.out.println("Pos5: " + pos5);

		assertTrue(pos4 > initialPos4);
		assertTrue(pos5 > initialPos5);
	}

}