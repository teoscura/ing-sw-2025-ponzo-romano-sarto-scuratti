package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.CannonType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class CannonComponentTest {
	private CannonComponent cannon1;
	private CannonComponent cannon_wrong;
	private CannonComponent cannon_double;

	@BeforeEach
	void setUp() {

		ConnectorType[] connectors = new ConnectorType[4];
		connectors[0] = ConnectorType.EMPTY;
		connectors[1] = ConnectorType.UNIVERSAL;
		connectors[2] = ConnectorType.UNIVERSAL;
		connectors[3] = ConnectorType.UNIVERSAL;

		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 2, 3);

		cannon1 = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.SINGLE, coords);
		cannon_wrong = new CannonComponent(1, connectors, ComponentRotation.U090, CannonType.SINGLE, coords);
		cannon_double = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.DOUBLE, coords);

	}

	@Test
	void check() {
	}

	@Test
	void verify() {
		ConnectorType[] connectors2 = new ConnectorType[4];
		connectors2[0] = ConnectorType.EMPTY;
		connectors2[1] = ConnectorType.UNIVERSAL;
		connectors2[2] = ConnectorType.UNIVERSAL;
		connectors2[3] = ConnectorType.UNIVERSAL;
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 2, 3);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 2, 4);
		ShipCoords coords_up = new ShipCoords(GameModeType.LVL2, 2, 2);
		StructuralComponent component = new StructuralComponent(1, connectors2, ComponentRotation.U000, coords_up);
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ship.addComponent(component, coords_up);
		ship.addComponent(cannon1, coords);
		assertFalse(cannon1.verify(ship));
		ship.addComponent(cannon_wrong, coords2);
		assertTrue(cannon_wrong.verify(ship));
	}

	@Test
	void turnOnOff() {
		assertEquals(0, cannon_double.getCurrentPower());
		cannon_double.turnOn();
		assertEquals(2, cannon_double.getCurrentPower());
		assertThrows(AlreadyPoweredException.class, () -> {
			cannon_double.turnOn();
		});
		assertEquals(2, cannon_double.getCurrentPower());
		cannon_double.turnOff();
		assertEquals(0, cannon_double.getCurrentPower());

		assertEquals(0.5, cannon_wrong.getCurrentPower());
		assertThrows(UnpowerableException.class, () -> {
			cannon_wrong.turnOn();
		});
		assertEquals(0.5, cannon_wrong.getCurrentPower());

	}

	@Test
	void getCurrentPower() {

		assertEquals(1, cannon1.getCurrentPower());
		assertEquals(0.5, cannon_wrong.getCurrentPower());
		assertEquals(0, cannon_double.getCurrentPower());

	}
}