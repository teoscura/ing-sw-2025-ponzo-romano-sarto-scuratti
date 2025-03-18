package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldComponentTest {

	private ShieldComponent shield;

	@Test
	void getConnectors() { //controlla tutte le connessioni intorno

	}

	@Test
	void getRotation(){

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
	void check(){

	}

	@Test
	void turnOn(){
		shield.turnOn();
		assertTrue(shield.getPowered());
	}

	@Test
	void turnOff() {
		shield.turnOff();
		assertFalse(shield.getPowered());
	}

	@Test
	void getPowered() {
		shield.turnOff();
		assertFalse(shield.getPowered());
		shield.turnOn();
		assertTrue(shield.getPowered());
	}

	@Test
	void getShield() {
		/*assertEquals(ShieldType.NONE, shield.getShield());
		shield.turnOn();
		assertEquals(ShieldType.NE, shield.getShield());*/
	}
}