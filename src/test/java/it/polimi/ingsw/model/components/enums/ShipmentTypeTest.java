package it.polimi.ingsw.model.components.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTypeTest {

	@Test
	void getSpecial() {
		assertTrue(ShipmentType.RED.getSpecial());
		assertFalse(ShipmentType.YELLOW.getSpecial());
		assertFalse(ShipmentType.GREEN.getSpecial());
		assertFalse(ShipmentType.BLUE.getSpecial());
		assertFalse(ShipmentType.EMPTY.getSpecial());
	}

	@Test
	void getValue() {
		assertEquals(4, ShipmentType.RED.getValue());
		assertEquals(3, ShipmentType.YELLOW.getValue());
		assertEquals(2, ShipmentType.GREEN.getValue());
		assertEquals(1, ShipmentType.BLUE.getValue());
		assertEquals(0, ShipmentType.EMPTY.getValue());
	}

	@Test
	void getTypes() {
		ShipmentType shipment = ShipmentType.BLUE;
		assertArrayEquals(new ShipmentType[]{ShipmentType.BLUE, ShipmentType.GREEN, ShipmentType.YELLOW, ShipmentType.RED}, shipment.getTypes());
	}
}