package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.DummyVoyageState;
import it.polimi.ingsw.model.state.ResumeWaitingState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ModelInstanceTest {

	private static ModelInstance getModelInstance() {
		ModelInstance model = new ModelInstance(0, new DummyController(0), GameModeType.TEST, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));
		Player player1 = new Player(GameModeType.TEST, "bibo1", PlayerColor.RED);
		ClientDescriptor p1desc = new ClientDescriptor("bibo1", new DummyConnection());
		p1desc.bindPlayer(player1);
		Player player2 = new Player(GameModeType.TEST, "bibo2", PlayerColor.BLUE);
		ClientDescriptor p2desc = new ClientDescriptor("bibo2", new DummyConnection());
		p2desc.bindPlayer(player2);
		Player player3 = new Player(GameModeType.TEST, "bibo3", PlayerColor.GREEN);
		ClientDescriptor p3desc = new ClientDescriptor("bibo3", new DummyConnection());
		p3desc.bindPlayer(player3);
		ArrayList<Player> order = new ArrayList<>(Arrays.asList(player1, player2, player3));
		Planche planche = new Planche(GameModeType.TEST, order);
		iCards deck = new TestFlightCards();
		DummyVoyageState state = new DummyVoyageState(model, GameModeType.TEST, PlayerCount.THREE, order, deck, planche);
		model.setState(state);
		return model;
	}

	@Test
	void resumeFromSerial() throws ForbiddenCallException {
		ModelInstance model = getModelInstance();
		assertInstanceOf(DummyVoyageState.class, model.getState());
		model.afterSerialRestart();
		assertInstanceOf(ResumeWaitingState.class, model.getState());
		//Now that we're restarting, lets create new descriptors for the same usernames.
		ClientDescriptor p1descagain = new ClientDescriptor("bibo1", new DummyConnection());
		ClientDescriptor p2descagain = new ClientDescriptor("bibo2", new DummyConnection());
		ClientDescriptor p3descagain = new ClientDescriptor("bibo3", new DummyConnection());
		model.connect(p1descagain);
		model.connect(p2descagain);
		model.disconnect(p1descagain);
		model.connect(p3descagain);
		model.connect(p1descagain);
		assertInstanceOf(DummyVoyageState.class, model.getState());
	}

}
