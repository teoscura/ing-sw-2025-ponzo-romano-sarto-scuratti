package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlienLifeSupportComponentTest {

	@Test
	void check() {
	}

	@Test
	void getType() {
		ConnectorType[] connectors = new ConnectorType[4];
		AlienLifeSupportComponent test_alien = new AlienLifeSupportComponent(1, connectors, ComponentRotation.U000,
				AlienType.BROWN);
		assertEquals(AlienType.BROWN, test_alien.getType());
	}
}