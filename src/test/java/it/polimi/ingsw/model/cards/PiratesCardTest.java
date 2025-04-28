package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.NewCenterMessage;
import it.polimi.ingsw.message.server.RemoveCrewMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.SlaversAnnounceState;
import it.polimi.ingsw.model.cards.state.SlaversLoseState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;

public class PiratesCardTest {

    private DummyModelInstance model;
	private DummyVoyageState state;
	private TestFlightCards cards;
	private Planche planche;
	private PiratesCard card;

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;

	ArrayList<Player> order, players;
	
	@BeforeEach
	void setUp() throws IOException {
		iBaseComponent c = null;
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
		p1desc = new ClientDescriptor(player1.getUsername(), null);
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
		p2desc = new ClientDescriptor(player2.getUsername(), null);
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
		p3desc = new ClientDescriptor(player3.getUsername(), null);
		p3desc.bindPlayer(player3);

		LevelOneCardFactory factory = new LevelOneCardFactory();
		order = new ArrayList<>(Arrays.asList(player1, player2, player3));
		players = new ArrayList<>(Arrays.asList(player1, player2, player3));
		model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		card = (PiratesCard) factory.getCard(3);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
		ArrayList<Projectile> s = card.getShots().getProjectiles();
		Projectile pr = s.get(0);
		s.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 1));
		pr = s.get(1);
		s.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = s.get(2);
		s.set(2, new Projectile(pr.getDirection(), pr.getDimension(), 7));
	}

	@Test
	void behaviourUnshielded() throws ForbiddenCallException {
	    ServerMessage message = null;
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		planche.printOrder();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//First misses, loses nothing
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Second hits front cannon.
		int x = player1.getScore();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertTrue(x-1 == player1.getScore());
		//Third hits center.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Should remove 
		x = player1.getScore();
		message = new NewCenterMessage(new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		assertTrue(x-3 == player1.getScore());
		//P2 accende e vince
		x = player2.getCredits();
		int pos = planche.getPlayerPosition(player2);
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
		message = new TakeRewardMessage(true);
		message.setDescriptor(p2desc);
		state.validate(message);
		planche.printOrder();
		assertTrue(player2.getCredits()== x + this.card.getCredits());
		assertTrue(pos-this.card.getDays()==planche.getPlayerPosition(player2));
		assertTrue(card.getExhausted());
		assertNull(state.getCardState(player1));
	}

	@Test
	void behaviour2() throws ForbiddenCallException {
		ServerMessage message = null;
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		planche.printOrder();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//First misses, loses nothing
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Second hits front cannon.
		int x = player1.getScore();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertTrue(x-1 == player1.getScore());
		//Third hits center.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//dude disconnects instead of setting cabin.
		state.disconnect(player1);
		assertTrue(player1.getRetired());
		//P2 accende e vince
		x = player2.getCredits();
		int pos = planche.getPlayerPosition(player2);
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
		message = new TakeRewardMessage(true);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertTrue(player2.getCredits()== x + this.card.getCredits());
		assertTrue(pos-this.card.getDays()==planche.getPlayerPosition(player2));
		assertTrue(card.getExhausted());
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilienceDuringShots() throws ForbiddenCallException {
		ServerMessage message = null;
		for (Player p : this.order) {
			System.out.println(p.getUsername() + " - e:" + p.getSpaceShip().getEnginePower() + " - cr:" + p.getSpaceShip().getTotalCrew() + " - c:" + p.getSpaceShip().getCannonPower());
		}
		planche.printOrder();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//First misses, loses nothing
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Second hits front cannon.
		int x = player1.getScore();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertTrue(x-1 == player1.getScore());
		state.disconnect(player1);
		assertTrue(!player1.getRetired());
		//P2 accende e vince
		x = player2.getCredits();
		int pos = planche.getPlayerPosition(player2);
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
		message = new TakeRewardMessage(true);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertTrue(player2.getCredits()== x + this.card.getCredits());
		assertTrue(pos-this.card.getDays()==planche.getPlayerPosition(player2));
		assertTrue(card.getExhausted());
		assertNull(state.getCardState(player1));
	}
}
