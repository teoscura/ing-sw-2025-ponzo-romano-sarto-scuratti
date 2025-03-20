package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldComponentTest { //temporary, to change after shield component modified

	@Test
	void verify() {
	}

	@Test
	void check(){
	}

	@Test
	void turnOn(){
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShieldComponent turnon_shield = new ShieldComponent(connectors, ComponentRotation.U000, ShieldType.NW);
		turnon_shield.turnOn();
		assertTrue(turnon_shield.getPowered());
		try{
			turnon_shield.turnOn();
			fail("AlreadyPoweredException did not occur");
		} catch (AlreadyPoweredException e){}
	}

	@Test
	void turnOff() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShieldComponent turnoff_shield = new ShieldComponent(connectors, ComponentRotation.U000, ShieldType.NW);
		turnoff_shield.turnOff();
		assertFalse(turnoff_shield.getPowered());
	}

	@Test
	void getPowered() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShieldComponent powered_shield = new ShieldComponent(connectors, ComponentRotation.U000, ShieldType.NW);
		powered_shield.turnOff();
		assertFalse(powered_shield.getPowered());
		powered_shield.turnOn();
		assertTrue(powered_shield.getPowered());
	}

	@Test
	void getShield() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShieldComponent getshield_shield = new ShieldComponent(connectors, ComponentRotation.U000, ShieldType.NW);
		assertEquals(ShieldType.NONE, getshield_shield.getShield());
		getshield_shield.turnOn();
		assertEquals(ShieldType.NW, getshield_shield.getShield());
	}
}