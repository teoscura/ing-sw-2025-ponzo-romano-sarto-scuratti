package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.ShipType;
import it.polimi.ingsw.model.player.SpaceShip;
import org.junit.jupiter.api.Test;

import java.sql.Struct;

import static org.junit.jupiter.api.Assertions.*;

class BaseComponentTest {

	private BaseComponent Base;

	@Test
	void getConnectors(){
		ConnectorType[] connectors = new ConnectorType[4];
		BaseComponent test_component = new StructuralComponent(connectors, ComponentRotation.U000);
		assertNotNull(test_component.getConnectors());
	}

	@Test
	void getRotation() {
			ConnectorType[] connectors = new ConnectorType[4];
			BaseComponent test_component_up = new StructuralComponent(connectors, ComponentRotation.U000);
			assertNotNull(test_component_up.getRotation());
			assertEquals(0, test_component_up.getRotation().getShift());
			BaseComponent test_component_right = new StructuralComponent(connectors, ComponentRotation.U090);
			assertEquals(1, test_component_right.getRotation().getShift());
			BaseComponent test_component_down = new StructuralComponent(connectors, ComponentRotation.U180);
			assertEquals(2, test_component_down.getRotation().getShift());
			BaseComponent test_component_left = new StructuralComponent(connectors, ComponentRotation.U270);
			assertEquals(3, test_component_left.getRotation().getShift());
	}

	@Test
	void verify() {
		//check specific cases once getConnector is fixed
		ShipCoords coords = new ShipCoords(ShipType.LVL2, 7, 7);
		ShipCoords coords_up = new ShipCoords(ShipType.LVL2, 7, 6);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		BaseComponent verify_test_component = new StructuralComponent(connectors, ComponentRotation.U000);
		BaseComponent verify_test_component_up = new StructuralComponent(connectors, ComponentRotation.U000);
		SpaceShip test_ship = new SpaceShip(ShipType.LVL2, PlayerColor.RED);
		test_ship.addComponent(verify_test_component, coords);
		test_ship.addComponent(verify_test_component_up, coords_up);
		assertTrue(verify_test_component.verify(test_ship));
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
		//assert is unable to compare elements, debug showed correct methods
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
		BaseComponent test_component = new StructuralComponent(null, ComponentRotation.U000);
		assertFalse(Base.powerable());
	}

	@Test
	void check() {
	}
}