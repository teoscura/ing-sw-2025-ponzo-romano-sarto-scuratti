package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.player.SpaceShip;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptyComponentTest {

	private EmptyComponent empty;

	@BeforeEach
	void setUp() {
		empty = new EmptyComponent();
		assertEquals(157, empty.getID());
		assertArrayEquals(new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, empty.getConnectors());
		assertEquals(ComponentRotation.U000, empty.getRotation());
	}

	@Test
	void check(){

	}

	@Test
	void verify() {

	}

	@Test
	void GetConnectors() {
		ConnectorType[] expected = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY};
		assertArrayEquals(expected, empty.getConnectors());
	}

	@Test
	void OnCreation() {
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		EmptyComponent emptyWithCoords = new EmptyComponent(coords);

		assertDoesNotThrow(() -> ship.addComponent(emptyWithCoords, coords));
		assertEquals(emptyWithCoords, ship.getComponent(coords));
	}

	@Test
	void OnDelete() {
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		EmptyComponent emptyWithCoords = new EmptyComponent(coords);

		assertDoesNotThrow(() -> ship.addComponent(emptyWithCoords, coords));
		assertEquals(emptyWithCoords, ship.getComponent(coords));

		assertDoesNotThrow(() -> emptyWithCoords.onDelete(ship));
	}

}