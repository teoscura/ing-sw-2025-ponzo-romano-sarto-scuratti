package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class BaseComponentTest {

	//private BaseComponent Base;

	@Test
	void getConnectors(){
		ConnectorType[] connectors = new ConnectorType[4];
		BaseComponent test_component = new StructuralComponent(1, connectors, ComponentRotation.U000);
		assertNotNull(test_component.getConnectors());
	}

	@Test
	void getRotation() {
			ConnectorType[] connectors = new ConnectorType[4];
			BaseComponent test_component_up = new StructuralComponent(1, connectors, ComponentRotation.U000);
			assertNotNull(test_component_up.getRotation());
			assertEquals(0, test_component_up.getRotation().getShift());
			BaseComponent test_component_right = new StructuralComponent(1, connectors, ComponentRotation.U090);
			assertEquals(1, test_component_right.getRotation().getShift());
			BaseComponent test_component_down = new StructuralComponent(1, connectors, ComponentRotation.U180);
			assertEquals(2, test_component_down.getRotation().getShift());
			BaseComponent test_component_left = new StructuralComponent(1, connectors, ComponentRotation.U270);
			assertEquals(3, test_component_left.getRotation().getShift());
	}

	@Test
	void verify() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 3);
		ShipCoords coords_up = new ShipCoords(GameModeType.LVL2, 4, 2);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		BaseComponent verify_component_empty = new StructuralComponent(1, connectors, ComponentRotation.U000);
		BaseComponent verify_test_component = new StructuralComponent(1, connectors, ComponentRotation.U000,coords);
		BaseComponent verify_test_component2 = new StructuralComponent(1, connectors, ComponentRotation.U000, coords_up);
		SpaceShip test_ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		test_ship.addComponent(verify_test_component, coords);
		test_ship.addComponent(verify_test_component2, coords_up);
		NullPointerException e = assertThrows(NullPointerException.class, () -> {verify_component_empty.verify(test_ship);});
		assertEquals("Coords are not set", e.getMessage());
		assertTrue(verify_test_component.verify(test_ship));
	}

	@Test
	void getConnector() {
		ConnectorType[] connector_test_connectors = new ConnectorType[4];
		connector_test_connectors[0] = ConnectorType.EMPTY;
		connector_test_connectors[1] = ConnectorType.UNIVERSAL;
		connector_test_connectors[2] = ConnectorType.SINGLE_CONNECTOR;
		connector_test_connectors[3] = ConnectorType.DOUBLE_CONNECTOR;
		BaseComponent connector_test_component1 = new StructuralComponent(1, connector_test_connectors, ComponentRotation.U000);
		assertEquals(ConnectorType.EMPTY, connector_test_component1.getConnector(ComponentRotation.U000));
		assertEquals(ConnectorType.UNIVERSAL, connector_test_component1.getConnector(ComponentRotation.U090));
		assertEquals(ConnectorType.SINGLE_CONNECTOR, connector_test_component1.getConnector(ComponentRotation.U180));
		assertEquals(ConnectorType.DOUBLE_CONNECTOR, connector_test_component1.getConnector(ComponentRotation.U270));
		BaseComponent connector_test_component2 = new StructuralComponent(1, connector_test_connectors, ComponentRotation.U090);
		assertEquals(ConnectorType.DOUBLE_CONNECTOR, connector_test_component2.getConnector(ComponentRotation.U000));
		BaseComponent connector_test_component3 = new StructuralComponent(1, connector_test_connectors, ComponentRotation.U180);
		assertEquals(ConnectorType.SINGLE_CONNECTOR, connector_test_component3.getConnector(ComponentRotation.U000));
		BaseComponent connector_test_component4 = new StructuralComponent(1, connector_test_connectors, ComponentRotation.U270);
		assertEquals(ConnectorType.UNIVERSAL, connector_test_component4.getConnector(ComponentRotation.U000));
	}

	@Test
	void getCoords() {
		ConnectorType[] connectors = new ConnectorType[4];
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 5);
		BaseComponent coords_test_component = new StructuralComponent(1, connectors, ComponentRotation.U000, coords);
		assertNotNull(coords_test_component.getCoords());
		ShipCoords up_coords = new ShipCoords(GameModeType.LVL2, 3, 4);
		ShipCoords down_coords = new ShipCoords(GameModeType.LVL2, 3, 6);
		ShipCoords right_coords = new ShipCoords(GameModeType.LVL2, 4, 5);
		ShipCoords left_coords = new ShipCoords(GameModeType.LVL2, 2, 5);
		assertEquals(up_coords.x, coords_test_component.getCoords().up().x);
		assertEquals(down_coords.x, coords_test_component.getCoords().down().x);
		assertEquals(right_coords.x, coords_test_component.getCoords().right().x);
		assertEquals(left_coords.x, coords_test_component.getCoords().left().x);
		assertEquals(up_coords.y, coords_test_component.getCoords().up().y);
		assertEquals(down_coords.y, coords_test_component.getCoords().down().y);
		assertEquals(right_coords.y, coords_test_component.getCoords().right().y);
		assertEquals(left_coords.y, coords_test_component.getCoords().left().y);

	}

	@Test
	void powerable(){
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		BaseComponent test_component = new StructuralComponent(1, connectors, ComponentRotation.U000);
		assertFalse(test_component.powerable());
	}

	@Test
	void check() {
	}

	@Test
	void getConnectedComponents(){
		SpaceShip ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 2, 1);
		ShipCoords up_coords = new ShipCoords(GameModeType.LVL2, 2, 0);
		ShipCoords right_coords = new ShipCoords(GameModeType.LVL2, 3, 1);
		ShipCoords down_coords = new ShipCoords(GameModeType.LVL2, 2, 2);
		ShipCoords left_coords = new ShipCoords(GameModeType.LVL2, 1, 1);
		StructuralComponent central_component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.SINGLE_CONNECTOR, ConnectorType.DOUBLE_CONNECTOR, ConnectorType.EMPTY}, ComponentRotation.U000, coords);
		StructuralComponent up_component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.SINGLE_CONNECTOR, ConnectorType.EMPTY}, ComponentRotation.U000, up_coords);
		StructuralComponent right_component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.DOUBLE_CONNECTOR}, ComponentRotation.U000, right_coords);
		StructuralComponent down_component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.DOUBLE_CONNECTOR, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, down_coords);
		StructuralComponent left_component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.SINGLE_CONNECTOR, ConnectorType.DOUBLE_CONNECTOR, ConnectorType.EMPTY}, ComponentRotation.U000, left_coords);
		ship.addComponent(central_component, coords);
		ship.addComponent(up_component, up_coords);
		ship.addComponent(right_component, right_coords);
		ship.addComponent(down_component, down_coords);
		ship.addComponent(left_component, left_coords);
		iBaseComponent results[] = central_component.getConnectedComponents(ship);
		assertEquals(up_component, results[0]);
		assertEquals(ship.getEmpty(), results[1]);
		assertEquals(down_component, results[2]);
		assertEquals(ship.getEmpty(), results[3]);
		StructuralComponent non_empty = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.LVL2, 4, 4));
		StructuralComponent empty = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, new ShipCoords(GameModeType.LVL2, 5, 4));
		ship.addComponent(non_empty, new ShipCoords(GameModeType.LVL2, 4, 4));
		ship.addComponent(empty, new ShipCoords(GameModeType.LVL2, 5, 4));
		iBaseComponent result[] = non_empty.getConnectedComponents(ship);
		assertEquals(ship.getEmpty(), result[1]);
	}
}