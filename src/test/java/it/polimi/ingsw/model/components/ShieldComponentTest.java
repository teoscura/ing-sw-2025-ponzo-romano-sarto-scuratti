package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.visitors.iVisitor;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ShieldComponentTest {


	private ShieldComponent shield;
	private StructuralComponent ship;

	@BeforeEach
	void setUp() {
		ConnectorType[] connectors = new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY};
		shield = new ShieldComponent(connectors, ComponentRotation.U000);
		ship = new StructuralComponent(connectors, ComponentRotation.U000);
	}

	@Test
	void check(){

	}

	@Test
	void testTurnOn() {
		assertFalse(shield.getPowered());
		shield.turnOn();
		assertTrue(shield.getPowered());
	}
	@Test
	void testTurnOff() {
		shield.turnOn();
		shield.turnOff();
		assertFalse(shield.getPowered());
	}

	@Test
	void testPowerable() {
		assertTrue(shield.powerable());
	}

	@Test
	void testOnCreation() {
		iSpaceShip ship = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		ShieldComponent shieldWithCoords = new ShieldComponent(new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, coords);

		assertDoesNotThrow(() -> ship.addComponent(shieldWithCoords, coords));
		assertEquals(shieldWithCoords, ship.getComponent(coords));
		assertThrows(NotUniqueException.class, () -> shieldWithCoords.onCreation(ship));

	}
	@Test
	void testOnDelete() {
		iSpaceShip ship = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		ShieldComponent shieldWithCoords = new ShieldComponent(
				new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY},
				ComponentRotation.U000,
				coords
		);
		assertDoesNotThrow(() -> ship.addComponent(shieldWithCoords, coords));
		assertEquals(shieldWithCoords, ship.getComponent(coords));

		assertDoesNotThrow(() -> shieldWithCoords.onDelete(ship));

		assertDoesNotThrow(() -> ship.addPowerableCoords(coords));
	}

	@Test
	void getShield() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};

		ShieldComponent shieldNE = new ShieldComponent(connectors, ComponentRotation.U000);
		ShieldComponent shieldSE = new ShieldComponent(connectors, ComponentRotation.U090);
		ShieldComponent shieldSW = new ShieldComponent(connectors, ComponentRotation.U180);
		ShieldComponent shieldNW = new ShieldComponent(connectors, ComponentRotation.U270);

		assertEquals(ShieldType.NONE, shieldNE.getShield());
		assertEquals(ShieldType.NONE, shieldSE.getShield());
		assertEquals(ShieldType.NONE, shieldSW.getShield());
		assertEquals(ShieldType.NONE, shieldNW.getShield());

		shieldNE.turnOn();
		shieldSE.turnOn();
		shieldSW.turnOn();
		shieldNW.turnOn();

		assertEquals(ShieldType.NE, shieldNE.getShield());
		assertEquals(ShieldType.SE, shieldSE.getShield());
		assertEquals(ShieldType.SW, shieldSW.getShield());
		assertEquals(ShieldType.NW, shieldNW.getShield());
	}

}