package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import org.junit.jupiter.api.Test;

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
			Base = new ShieldComponent(connectors, ComponentRotation.U000, ShieldType.NONE);
			assertEquals(0, Base.getRotation().getShift());
			Base = new ShieldComponent(connectors, ComponentRotation.U090, ShieldType.NONE);
			assertEquals(1, Base.getRotation().getShift());
			Base = new ShieldComponent(connectors, ComponentRotation.U180, ShieldType.NONE);
			assertEquals(2, Base.getRotation().getShift());
			Base = new StructuralComponent(connectors, ComponentRotation.U270);
			assertEquals(3, Base.getRotation().getShift());
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
		connector_test_component = new StructuralComponent(connector_test_connectors, ComponentRotation.U090);
		assertEquals(ConnectorType.DOUBLE_CONNECTOR, connector_test_component.getConnector(ComponentRotation.U000));
		connector_test_component = new StructuralComponent(connector_test_connectors, ComponentRotation.U180);
		assertEquals(ConnectorType.SINGLE_CONNECTOR, connector_test_component.getConnector(ComponentRotation.U000));
		connector_test_component = new StructuralComponent(connector_test_connectors, ComponentRotation.U270);
		assertEquals(ConnectorType.UNIVERSAL, connector_test_component.getConnector(ComponentRotation.U000));
	}

	@Test
	void getCoords() {
	}

	@Test
	void check() {
	}
}