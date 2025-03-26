package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.CannonType;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.UnpowerableException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class CannonComponentTest {
	private CannonComponent cannon1;
	private CannonComponent cannon_wrong;
	private CannonComponent cannon2;

	@BeforeEach
	void setUp() {

		ConnectorType[] connectors = new ConnectorType[4];
		connectors[0] = ConnectorType.EMPTY;
		connectors[1] = ConnectorType.EMPTY;
		connectors[2] = ConnectorType.EMPTY;
		connectors[3] = ConnectorType.EMPTY;


		cannon1 = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.SINGLE);
		cannon_wrong = new CannonComponent(1, connectors, ComponentRotation.U090, CannonType.SINGLE);
		cannon2 = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.DOUBLE);

	}

	@Test
	void check() {
	}

	@Test
	void testVerify() {
	}

	// the exceptions are wrong
	@Test
	void turnOnOff() {
		assertEquals(0, cannon2.getCurrentPower());

		assertThrows(UnpowerableException.class, () -> {
			cannon2.turnOn();
		});
		assertEquals(0, cannon2.getCurrentPower());


		assertEquals(0, cannon_wrong.getCurrentPower());
		assertThrows(UnpowerableException.class, () -> {
			cannon_wrong.turnOn();
		});
		assertEquals(0, cannon_wrong.getCurrentPower());

	}


	@Test
	void getCurrentPower() {

		assertEquals(1, cannon1.getCurrentPower());
		assertEquals(0, cannon_wrong.getCurrentPower());
		assertEquals(0, cannon2.getCurrentPower());

	}
}