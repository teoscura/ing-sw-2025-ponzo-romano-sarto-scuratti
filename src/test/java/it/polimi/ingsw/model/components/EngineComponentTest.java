package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineComponentTest {

	@Test
	void getConnectors() { //not overriden from base
	}

	@Test
	void getRotation() { //not overriden from base
	}

	@Test
	void verify() {
		//TODO
	}

	@Test
	void getConnector() { //not overriden from base
	}

	@Test
	void getCoords() { //not overriden from base
	}

	@Test
	void check() {
	}

	@Test
	void turnOn() {
		EngineComponent turnon_test_component1 = new EngineComponent(null, ComponentRotation.U000, EngineType.SINGLE);
		EngineComponent turnon_test_component2 = new EngineComponent(null, ComponentRotation.U000, EngineType.DOUBLE);
		Exception exception = assertThrows(AlreadyPoweredException.class, () -> turnon_test_component2.turnOn());
	}

	@Test
	void turnOff() {
	}

	@Test
	void getCurrentPower() {
	}
}