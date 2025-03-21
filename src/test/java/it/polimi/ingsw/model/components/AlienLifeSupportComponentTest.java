package it.polimi.ingsw.model.components;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;

import static org.junit.jupiter.api.Assertions.*;

class AlienLifeSupportComponentTest {

	@Test
	void check() {
	}

	@Test
	void getType() {
		ConnectorType[] connectors = new ConnectorType[4];
		AlienLifeSupportComponent test_alien = new AlienLifeSupportComponent(connectors, ComponentRotation.U000,
				AlienType.BROWN);
		assertEquals(AlienType.BROWN, test_alien.getType());
	}
}