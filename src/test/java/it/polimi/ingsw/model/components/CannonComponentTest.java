package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;

import static org.junit.jupiter.api.Assertions.*;

class CannonComponentTest {

	@Test
	void getConnectors() {
	}

	@Test
	void getRotation() {
	}

	@Test
	void verify() {
	}

	@Test
	void getConnector() {
	}

	@Test
	void getCoords() {
	}

	@Test
	void check() {
	}

	@Test
	void testVerify() {
	}

	@Test
	void turnOn() {
	}

	@Test
	void turnOff() {
	}

	@Test
	void getCurrentPower() {
		// the default values are wrong
		CannonComponent cannon;
		ConnectorType[] connectors = new ConnectorType[4];
		
		cannon = new CannonComponent(connectors, ComponentRotation.U000, CannonType.SINGLE);
		assertEquals(0, cannon.getCurrentPower());

		cannon = new CannonComponent(connectors, ComponentRotation.U000, CannonType.DOUBLE);
		assertEquals(0, cannon.getCurrentPower());
	}
}