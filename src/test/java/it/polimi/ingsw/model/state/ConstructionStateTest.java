package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructionStateTest {

	private DummyModelInstance model;
	private Player player1;
	private ClientDescriptor p1desc;
	private Player player2;
	private ClientDescriptor p2desc;

	@BeforeEach
	void setUp() {
		model = new DummyModelInstance(1, GameModeType.TEST, PlayerCount.TWO);
		model.setController(new DummyController(model.getID()));
		player1 = new Player(GameModeType.TEST, "Gigio1", PlayerColor.RED);
		p1desc = new ClientDescriptor("Gigio1", new DummyConnection());
		p1desc.bindPlayer(player1);
		player2 = new Player(GameModeType.TEST, "Gigio2", PlayerColor.BLUE);
		p2desc = new ClientDescriptor("Gigio2", new DummyConnection());
		p2desc.bindPlayer(player2);
	}

	@Test
	void testFlightConstruction() throws ForbiddenCallException {
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
		TestFlightConstructionState state = new TestFlightConstructionState(model, GameModeType.TEST, PlayerCount.TWO, players);
		model.setState(state);
		ServerMessage mess = null;
		mess = new TakeComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		int id = state.getCurrent(player1).getID();
		mess = new TakeComponentMessage();
		mess.setDescriptor(p2desc);
		model.validate(mess);
		mess = new DiscardComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		assertTrue(state.getDiscarded().contains(id));
		mess = new ReserveComponentMessage();
		mess.setDescriptor(p2desc);
		model.validate(mess);
		mess = new TakeDiscardedComponentMessage(id);
		mess.setDescriptor(p2desc);
		model.validate(mess);
		assertEquals(state.getCurrent(player2).getID(), id);
		mess = new TakeComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new SendContinueMessage();
		mess.setDescriptor(p2desc);
		model.validate(mess);
		mess = new SendContinueMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		state.getHoarded(player1);
		state.getOngoingEntry(10);
		assertTrue(state.toSerialize());
		assertEquals(-1, player1.getCredits());
		assertEquals(-2, player2.getCredits());
		assertInstanceOf(VerifyState.class, model.getState());
	}

	@Test
	void LevelTwoConstruction() throws InterruptedException, ForbiddenCallException {
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
		LevelTwoConstructionState state = new LevelTwoConstructionState(model, GameModeType.TEST, PlayerCount.TWO, players, 1);
		model.setState(state);
		ServerMessage mess = null;
		Thread.sleep(1000);
		mess = new TakeComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new ToggleHourglassMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new DiscardComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new TakeDiscardedComponentMessage(10);
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new PutComponentMessage(10, new ShipCoords(GameModeType.TEST, 3, 3), ComponentRotation.U000);
		mess.setDescriptor(p1desc);
		model.validate(mess);
		mess = new SendContinueMessage();
		mess.setDescriptor(p2desc);
		model.validate(mess);
		//Test hourglass mechanics.
		Thread.sleep(1000);
		mess = new ToggleHourglassMessage();
		mess.setDescriptor(p2desc);
		model.validate(mess);
		mess = new DiscardComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		Thread.sleep(1000);
		assertNull(state.getCurrent(player1));
		mess = new TakeComponentMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		assertNull(state.getCurrent(player1));
		mess = new SendContinueMessage();
		mess.setDescriptor(p1desc);
		model.validate(mess);
		assertEquals(0, player1.getCredits());
		assertEquals(0, player2.getCredits());
		assertInstanceOf(VerifyState.class, model.getState());
	}


}
