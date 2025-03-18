package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.ShipType;
import org.junit.jupiter.api.Test;

import java.sql.Struct;

import static org.junit.jupiter.api.Assertions.*;

class BaseComponentTest {

	private BaseComponent Base;

	@Test
	void getConnectors(){
		assertNotNull(Base.getConnectors());
	}

	@Test
	void getRotation() {
			ConnectorType[] connectors = new ConnectorType[4];
			assertNotNull(Base.getRotation());
			BaseComponent base_up = new StructuralComponent(connectors, ComponentRotation.U000);
			assertEquals(0, base_up.getRotation().getShift());
			BaseComponent base_right = new StructuralComponent(connectors, ComponentRotation.U090);
			assertEquals(1, base_right.getRotation().getShift());
			BaseComponent base_down = new StructuralComponent(connectors, ComponentRotation.U180);
			assertEquals(2, base_down.getRotation().getShift());
			BaseComponent base_left = new StructuralComponent(connectors, ComponentRotation.U270);
			assertEquals(3, base_left.getRotation().getShift());
	}

	@Test
	void verify() {

	}

	@Test
	void getConnector() {
		ConnectorType[] connector_test_connectors = new ConnectorType[4];
		connector_test_connectors[0] = ConnectorType.EMPTY;
		connector_test_connectors[1] = ConnectorType.UNIVERSAL;
		connector_test_connectors[2] = ConnectorType.SINGLE_CONNECTOR;
		connector_test_connectors[3] = ConnectorType.DOUBLE_CONNECTOR;
		BaseComponent connector_test_component = new StructuralComponent(connector_test_connectors, ComponentRotation.U000);
		assertEquals(ConnectorType.EMPTY, connector_test_component.getConnector(ComponentRotation.U000));
		assertEquals(ConnectorType.UNIVERSAL, connector_test_component.getConnector(ComponentRotation.U090));
		assertEquals(ConnectorType.SINGLE_CONNECTOR, connector_test_component.getConnector(ComponentRotation.U180));
		assertEquals(ConnectorType.DOUBLE_CONNECTOR, connector_test_component.getConnector(ComponentRotation.U270));
		BaseComponent connector_test_component2 = new StructuralComponent(connector_test_connectors, ComponentRotation.U090);
		assertEquals(ConnectorType.UNIVERSAL, connector_test_component2.getConnector(ComponentRotation.U000)); //rotation of component is backwards or unintuitive
	}

	@Test
	void getCoords() {
		ConnectorType[] connectors = new ConnectorType[4];
		ShipCoords coords = new ShipCoords(ShipType.LVL2, 3, 5);
		BaseComponent coords_test_component = new StructuralComponent(connectors, ComponentRotation.U000, coords);
		assertNotNull(coords_test_component.getCoords());
		ShipCoords up_coords = new ShipCoords(ShipType.LVL2, 3, 4);
		ShipCoords down_coords = new ShipCoords(ShipType.LVL2, 3, 6);
		ShipCoords right_coords = new ShipCoords(ShipType.LVL2, 4, 5);
		ShipCoords left_coords = new ShipCoords(ShipType.LVL2, 2, 5);
		assertEquals(up_coords, coords_test_component.getCoords().up());
		assertEquals(down_coords, coords_test_component.getCoords().down());
		assertEquals(right_coords, coords_test_component.getCoords().right());
		assertEquals(left_coords, coords_test_component.getCoords().left());
	}

	@Test
	void powerable(){
		assertFalse(Base.powerable());
	}

	@Test
	void check() {
	}
}