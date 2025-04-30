package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageComponentTest {
	Player player1;
	ConnectorType[] connectors;
	ShipCoords coords;

	@BeforeEach
	void setUp() {
		player1 = new Player(GameModeType.LVL2, "gigi", PlayerColor.BLUE);
		connectors = new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		coords = new ShipCoords(GameModeType.LVL2, 4, 3);
	}

	@Test
	void verify() {
	}

	@Test
	void check() {
	}

	@Test
	void putIn() {
		StorageComponent putin_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.DOUBLENORMAL, coords);
		try {
			putin_storage_component1.putIn(null);
			fail("NullPointerException did not occur");
		} catch (NullPointerException e) {
		}
		ShipmentType special_shipment = ShipmentType.RED;
		try {
			putin_storage_component1.putIn(special_shipment);
			fail("ContainerNotSpecialException did not occur");
		} catch (ContainerNotSpecialException e) {
		}
		ShipmentType shipment1 = ShipmentType.BLUE;
		ShipmentType shipment2 = ShipmentType.BLUE;
		ShipmentType shipment3 = ShipmentType.BLUE;
		putin_storage_component1.putIn(shipment1);
		putin_storage_component1.putIn(shipment2);
		try {
			putin_storage_component1.putIn(shipment3);
			fail("ContainerFullException did not occur");
		} catch (ContainerFullException e) {
		}
	}

	@Test
	void takeOut() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		StorageComponent takeout_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.DOUBLENORMAL);
		takeout_storage_component1.onCreation(player1.getSpaceShip(),coords);
		ShipmentType shipment1 = ShipmentType.BLUE;
		ShipmentType shipment2 = ShipmentType.GREEN;
		assertThrows(NullPointerException.class, () -> takeout_storage_component1.takeOut(null));
		takeout_storage_component1.putIn(shipment1);
		assertEquals(1, takeout_storage_component1.howMany(shipment1));
		assertTrue(takeout_storage_component1.takeOut(shipment1));
		assertThrows(ContainerEmptyException.class, ()->takeout_storage_component1.takeOut(shipment2));
	}

	@Test
	void howMany() {
		StorageComponent howmany_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.DOUBLENORMAL, coords);
		ShipmentType shipment1 = ShipmentType.BLUE;
		ShipmentType shipment2 = ShipmentType.BLUE;
		howmany_storage_component1.putIn(shipment2);
		howmany_storage_component1.putIn(shipment1);
		assertEquals(2, howmany_storage_component1.howMany(shipment1));
	}

	@Test
	void getFreeSpaces() {
		StorageComponent freespaces_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.TRIPLENORMAL, coords);
		assertEquals(3, freespaces_storage_component1.getFreeSpaces());
		ShipmentType shipment1 = ShipmentType.BLUE;
		freespaces_storage_component1.putIn(shipment1);
		assertEquals(2, freespaces_storage_component1.getFreeSpaces());
	}

	@Test
	void getSpecial() {
		StorageComponent getspecial_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.TRIPLENORMAL, coords);
		StorageComponent getspecial_storage_component2 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.DOUBLESPECIAL, coords);
		assertFalse(getspecial_storage_component1.getSpecial());
		assertTrue(getspecial_storage_component2.getSpecial());
	}

	@Test
	void getCapacity() {
		StorageComponent getcapacity_storage_component1 = new StorageComponent(1, connectors, ComponentRotation.U000, StorageType.TRIPLENORMAL, coords);
		assertEquals(3, getcapacity_storage_component1.getCapacity());
	}
}