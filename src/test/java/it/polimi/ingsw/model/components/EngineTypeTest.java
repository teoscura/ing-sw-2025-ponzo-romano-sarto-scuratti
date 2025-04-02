package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.EngineType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTypeTest {

	@Test
	void getMaxPower() {
		EngineType maxpower_engine_type1 = EngineType.SINGLE;
		EngineType maxpower_engine_type2 = EngineType.DOUBLE;
		assertEquals(1 , maxpower_engine_type1.getMaxPower());
		assertEquals(2 , maxpower_engine_type2.getMaxPower());
	}

	@Test
	void getPowerable() {
		EngineType powerable_engine_type1 = EngineType.SINGLE;
		EngineType powerable_engine_type2 = EngineType.DOUBLE;
        assertFalse(powerable_engine_type1.getPowerable());
		assertTrue(powerable_engine_type2.getPowerable());
	}

}