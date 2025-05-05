package it.polimi.ingsw.model.board;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.state.DummyVoyageState;

class PlancheTest {
	
	private DummyModelInstance model;
	private DummyVoyageState state;

	@Test
	void lappedFrontTest(){
		Player p1 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
		Player p2 = new Player(GameModeType.TEST, "sbingus", PlayerColor.BLUE);
		ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{p1,p2}));
		Planche planche = new Planche(GameModeType.TEST, order);
		model = new DummyModelInstance(1, GameModeType.TEST, PlayerCount.TWO);
		model.setController(new DummyController(model.getID(), model));
		state = new DummyVoyageState(model, GameModeType.TEST, PlayerCount.TWO, order, new TestFlightCards(), planche);
		
		assertTrue(2==(planche.getPlayerPosition(p1)-planche.getPlayerPosition(p2)));
		planche.movePlayer(state, p1, +31);
		assertTrue(p2.getRetired());
	}

	@Test
	void lappedBackTest(){
		Player p1 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
		Player p2 = new Player(GameModeType.TEST, "sbingus", PlayerColor.BLUE);
		ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{p1,p2}));
		Planche planche = new Planche(GameModeType.TEST, order);
		model = new DummyModelInstance(1, GameModeType.TEST, PlayerCount.TWO);
		model.setController(new DummyController(model.getID(), model));
		state = new DummyVoyageState(model, GameModeType.TEST, PlayerCount.TWO, order, new TestFlightCards(), planche);
		
		assertTrue(2==(planche.getPlayerPosition(p1)-planche.getPlayerPosition(p2)));
		planche.movePlayer(state, p1, +10);
		planche.movePlayer(state, p2, +10);
		planche.movePlayer(state, p2, -20);
		assertTrue(p2.getRetired());
	}

}