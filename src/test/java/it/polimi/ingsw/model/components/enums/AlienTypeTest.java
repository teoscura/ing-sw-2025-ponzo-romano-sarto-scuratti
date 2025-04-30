package it.polimi.ingsw.model.components.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class AlienTypeTest {

	private AlienType human_cabin;
	private AlienType brown_cabin;
	private AlienType purple_cabin;
	private AlienType both_cabin;
	private AlienType none_cabin;

	
	@BeforeEach
	void setup(){
		human_cabin = AlienType.HUMAN;
		brown_cabin = AlienType.BROWN;
		purple_cabin = AlienType.PURPLE;
		both_cabin = AlienType.BOTH;
		none_cabin = AlienType.NONE;
	}


	@Test
	void getMaxCapacity() {
		assertEquals(2, human_cabin.getMaxCapacity());
		assertEquals(1, brown_cabin.getMaxCapacity());
		assertEquals(1, purple_cabin.getMaxCapacity());
		assertEquals(1, both_cabin.getMaxCapacity());
	}

	@Test
	void getLifeSupportExists() {
		assertFalse(human_cabin.getLifeSupportExists());
		assertTrue(brown_cabin.getLifeSupportExists());
		assertTrue(purple_cabin.getLifeSupportExists());
		assertFalse(both_cabin.getLifeSupportExists());
	}

	@Test
	void getArraypos() {
		assertEquals(0, human_cabin.getArraypos());
		assertEquals(1, brown_cabin.getArraypos());
		assertEquals(2, purple_cabin.getArraypos());
		assertEquals(-1, both_cabin.getArraypos());
	}

	@Test
	void compatible(){
		assertFalse(none_cabin.compatible(AlienType.PURPLE));
		assertFalse(none_cabin.compatible(AlienType.BROWN));
		assertFalse(none_cabin.compatible(AlienType.NONE));
		assertFalse(none_cabin.compatible(AlienType.BOTH));
		assertFalse(none_cabin.compatible(AlienType.HUMAN));

		assertFalse(human_cabin.compatible(AlienType.BROWN));
		assertFalse(human_cabin.compatible(AlienType.PURPLE));

		assertTrue(brown_cabin.compatible(AlienType.BROWN));
		assertTrue(purple_cabin.compatible(AlienType.PURPLE));
		assertTrue(both_cabin.compatible(AlienType.BROWN));
		assertTrue(both_cabin.compatible(AlienType.HUMAN));
		assertTrue(both_cabin.compatible(AlienType.PURPLE));

		assertTrue(brown_cabin.compatible(AlienType.HUMAN));
		assertTrue(purple_cabin.compatible(AlienType.HUMAN));
		assertFalse(brown_cabin.compatible(AlienType.PURPLE));
		assertFalse(purple_cabin.compatible(AlienType.BROWN));
	}
}