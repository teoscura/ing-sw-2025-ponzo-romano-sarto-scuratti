package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.StorageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTypeTest {

	@Test
	void getSpecial() {
		StorageType special_test_type1 = StorageType.DOUBLESPECIAL;
		StorageType special_test_type2 = StorageType.DOUBLENORMAL;
		assertTrue(special_test_type1.getSpecial());
		assertFalse(special_test_type2.getSpecial());
	}

	@Test
	void getCapacity() {
		StorageType capacity_test_type1 = StorageType.TRIPLENORMAL;
		StorageType capacity_test_type2 = StorageType.SINGLESPECIAL;
		assertEquals(3, capacity_test_type1.getCapacity());
		assertEquals(1, capacity_test_type2.getCapacity());
	}
}