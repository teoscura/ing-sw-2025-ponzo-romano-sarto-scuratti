package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.player.SpaceShip;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ShieldComponentTest {


	private ShieldComponent shield;
	//private StructuralComponent ship;

	@BeforeEach
	void setUp() {
		ConnectorType[] connectors = new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY};
		shield = new ShieldComponent(1, connectors, ComponentRotation.U000);
		//ship = new StructuralComponent(1, connectors, ComponentRotation.U000);
	}

	@Test
	void check(){

	}

	@Test
	void TurnOn() {
		assertFalse(shield.getPowered());
		shield.turnOn();
		assertTrue(shield.getPowered());
	}
	@Test
	void TurnOff() {
		shield.turnOn();
		shield.turnOff();
		assertFalse(shield.getPowered());
	}

	@Test
	void Powerable() {
		assertTrue(shield.powerable());
	}

	@Test
	void OnCreation() {
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		ShieldComponent shieldWithCoords = new ShieldComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, coords);

		assertDoesNotThrow(() -> ship.addComponent(shieldWithCoords, coords));
		assertEquals(shieldWithCoords, ship.getComponent(coords));
		assertThrows(NotUniqueException.class, () -> shieldWithCoords.onCreation(ship, coords));

	}
	@Test
	void OnDelete() {
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		ShieldComponent shieldWithCoords = new ShieldComponent(1,
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

		ShieldComponent shieldNE = new ShieldComponent(1, connectors, ComponentRotation.U000);
		ShieldComponent shieldSE = new ShieldComponent(1, connectors, ComponentRotation.U090);
		ShieldComponent shieldSW = new ShieldComponent(1, connectors, ComponentRotation.U180);
		ShieldComponent shieldNW = new ShieldComponent(1, connectors, ComponentRotation.U270);

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