package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.EngineType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.player.iSpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineComponentTest {

	private EngineComponent engine1;
	private EngineComponent engine2;

	@BeforeEach
	void setUp() {
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY};
		engine1 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.SINGLE);
		engine2 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE);
	}

	@Test
	void verify() {
		//TODO
	}

	@Test
	void check() {
	}

	@Test
	void TurnOn() {
		assertThrows(UnpowerableException.class, () -> engine1.turnOn());

		assertFalse(engine2.isPowered());
		engine2.turnOn();
		assertTrue(engine2.isPowered());
		assertThrows(AlreadyPoweredException.class, () -> engine2.turnOn());
	}

	@Test
	void turnOff() {
		engine2.turnOn();
		assertTrue(engine2.isPowered());

		engine2.turnOff();
		assertFalse(engine2.isPowered());
	}

	@Test
	void testPowerable() {
		assertTrue(engine2.powerable());
		assertFalse(engine1.isPowered());
	}

	@Test
	void getCurrentPower() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		EngineComponent currentpower_engine_component1 = new EngineComponent(1, connectors, ComponentRotation.U180, EngineType.SINGLE);
		EngineComponent currentpower_engine_component2 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.SINGLE);
		EngineComponent currentpower_engine_component3 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE);
		currentpower_engine_component3.turnOn();
		assertEquals(1, currentpower_engine_component2.getCurrentPower());
		assertEquals(0, currentpower_engine_component1.getCurrentPower());
		assertEquals(2, currentpower_engine_component3.getCurrentPower());
	}

}