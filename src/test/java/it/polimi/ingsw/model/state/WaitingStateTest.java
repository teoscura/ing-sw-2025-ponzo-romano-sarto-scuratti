package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitingStateTest {

	private DummyModelInstance model;
	private WaitingState waiting_state;
	private ClientDescriptor p1desc;
	private ClientDescriptor p2desc;
	private ClientDescriptor p3desc;

	@BeforeEach
	void setUp() {
		model = new DummyModelInstance(1, GameModeType.TEST, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));
		waiting_state = new WaitingState(model, GameModeType.TEST, PlayerCount.THREE);
		model.setState(waiting_state);
		p1desc = new ClientDescriptor("Gigio1", new DummyConnection());
		p2desc = new ClientDescriptor("Gigio2", new DummyConnection());
		p3desc = new ClientDescriptor("Gigio3", new DummyConnection());
	}

	@Test
	void behaviour() throws ForbiddenCallException {
		assertFalse(model.hasStarted());
		//player1 connects
		waiting_state.connect(p1desc);
		//player1 attempts connection again
		waiting_state.connect(p1desc);
		//player 2 attempts to diconnect before being connected
		waiting_state.disconnect(p2desc);
		//player 2 connects
		model.connect(p2desc);
		model.connect(p3desc);
		assertInstanceOf(TestFlightConstructionState.class, model.getState());
		//Did everyone generate with a player.
		assertNotNull(p1desc.getPlayer());
		assertEquals(p1desc.getPlayer().getColor(), PlayerColor.RED);
		assertEquals(p1desc.getUsername(), p1desc.getPlayer().getUsername());
		assertNotNull(p2desc.getPlayer());
		assertEquals(p2desc.getPlayer().getColor(), PlayerColor.BLUE);
		assertEquals(p2desc.getUsername(), p2desc.getPlayer().getUsername());
		assertNotNull(p3desc.getPlayer());
		assertEquals(p3desc.getPlayer().getColor(), PlayerColor.GREEN);
		assertEquals(p3desc.getUsername(), p3desc.getPlayer().getUsername());
		assertTrue(model.hasStarted());
	}
}