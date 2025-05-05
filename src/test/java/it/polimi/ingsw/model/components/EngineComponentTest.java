package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.EngineType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineComponentTest {

	@Test
	void check() {
	}

	@Test
	void turnOn() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		EngineComponent turnon_test_component1 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.SINGLE);
		EngineComponent turnon_test_component2 = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE);
		turnon_test_component2.turnOn();
		try{
			turnon_test_component2.turnOn();
			fail("AlreadyPoweredException did not occur");
		} catch (AlreadyPoweredException e1){}
		try{
			turnon_test_component1.turnOn();
			fail("UnpoweredException did not occur");
		}catch (UnpowerableException e2){}
	}

	@Test
	void turnOff() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		EngineComponent turnoff_engine_component = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE);
		turnoff_engine_component.turnOn();
		turnoff_engine_component.turnOff();
		try{
			turnoff_engine_component.turnOn();

		}catch(AlreadyPoweredException e){
			fail("Component didn't turn off properly");
		}
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