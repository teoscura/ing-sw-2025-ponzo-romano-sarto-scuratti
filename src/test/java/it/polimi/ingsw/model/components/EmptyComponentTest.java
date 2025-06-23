package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.client.components.ClientEmptyComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyComponentTest {

	private EmptyComponent empty;

	@BeforeEach
	void setUp() {
		empty = new EmptyComponent();
		assertEquals(157, empty.getID());
		assertArrayEquals(new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, empty.getConnectors());
		assertEquals(ComponentRotation.U000, empty.getRotation());
	}

	@Test
	void verify() {
		assertTrue(empty.verify(null));
	}

	@Test
	void GetConnectors() {
		ConnectorType[] expected = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY};
		assertArrayEquals(expected, empty.getConnectors());
	}

	@Test
	void onCreationDelete(){
		empty.onCreation(null, null);
		empty.onDelete(null);
	}

	@Test
	void clientComponent(){
		assertInstanceOf(ClientEmptyComponent.class, empty.getClientComponent());
	}


}