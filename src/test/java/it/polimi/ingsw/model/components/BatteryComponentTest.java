package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.BatteryType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryComponentTest {

	private BatteryComponent component2;
	private BatteryComponent component3;

	@BeforeEach
	void setup() {
		ConnectorType[] connectors = { ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL };
		component2 = new BatteryComponent(connectors, ComponentRotation.U000, BatteryType.DOUBLE);
		component3 = new BatteryComponent(connectors, ComponentRotation.U000, BatteryType.TRIPLE);

	}

	@Test
	void check() {
	}

	@Test
	void getContains() {// already tested with takeAndPutOne()

	}

	@Test
	void getCapacity() {
		assertEquals(2, component2.getCapacity());
		assertEquals(3, component3.getCapacity());

	}

	@Test
	void takeAndPutOne() {
		assertEquals(2, component2.getContains());
		component2.takeOne();
		assertEquals(1, component2.getContains());
		component2.takeOne();
		assertEquals(0, component2.getContains());
		component2.putOne();
		assertEquals(1, component2.getContains());
		component2.putOne();
		assertEquals(2, component2.getContains());

		assertEquals(3, component3.getContains());
		component3.takeOne();
		assertEquals(2, component3.getContains());
		component3.takeOne();
		assertEquals(1, component3.getContains());
		component3.takeOne();
		assertEquals(0, component3.getContains());
		component3.putOne();
		assertEquals(1, component3.getContains());
		component3.putOne();
		assertEquals(2, component3.getContains());
		component3.putOne();
		assertEquals(3, component3.getContains());

	}

	@SuppressWarnings("unused")
	@Test
	void containerFull() {
		ContainerFullException e2 = assertThrows(ContainerFullException.class, () -> {
			component2.putOne();
		});
		ContainerFullException e3 = assertThrows(ContainerFullException.class, () -> {
			component3.putOne();
		});
		assertEquals(2, component2.getContains());
		assertEquals(3, component3.getContains());
	}

	@SuppressWarnings("unused")
	@Test
	void containerEmpty() {
		component2.takeOne();
		component2.takeOne();
		ContainerEmptyException e2 = assertThrows(ContainerEmptyException.class, () -> {
			component2.takeOne();
		});

		component3.takeOne();
		component3.takeOne();
		component3.takeOne();
		ContainerEmptyException e3 = assertThrows(ContainerEmptyException.class, () -> {
			component3.takeOne();
		});

		assertEquals(0, component2.getContains());
		assertEquals(0, component3.getContains());
	}

}