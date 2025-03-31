package it.polimi.ingsw.model.components.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class ShieldTypeTest {

	private ShieldType north_east;
	private ShieldType north_west;
	private ShieldType south_east;
	private ShieldType south_west;
	private ShieldType none;

	@BeforeEach
	void setup(){
		north_east = ShieldType.NE;
		north_west = ShieldType.NW;
		south_east = ShieldType.SE;
		south_west = ShieldType.SW;
		none = ShieldType.NONE;
	}
		
	

	@Test
	void getNorth() {
		assertTrue(north_east.getNorth());
		assertFalse(south_east.getNorth());
		assertFalse(south_west.getNorth());
		assertTrue(north_west.getNorth());
		assertFalse(none.getNorth());
	}

	@Test
	void getEast() {

		assertTrue(north_east.getEast());
		assertTrue(south_east.getEast());
		assertFalse(south_west.getEast());
		assertFalse(north_west.getEast());
		assertFalse(none.getEast());
	}

	@Test
	void getSouth() {
		assertFalse(north_east.getSouth());
		assertTrue(south_east.getSouth());
		assertTrue(south_west.getSouth());
		assertFalse(north_west.getSouth());
		assertFalse(none.getSouth());
	}

	@Test
	void getWest() {
		assertFalse(north_east.getWest());
		assertFalse(south_east.getWest());
		assertTrue(south_west.getWest());
		assertTrue(north_west.getWest());
		assertFalse(none.getWest());
	}

	@Test
	void getShielded() {
		boolean[] expected_NE = {true, true, false, false};
		boolean[] expected_NW = {true, false, false, true};
		boolean[] expected_SE = {false, true, true, false};
		boolean[] expected_SW = {false, false, true, true};
		boolean[] expected_none = {false, false, false, false};
		assertArrayEquals(expected_NE, north_east.getShielded());
		assertArrayEquals(expected_NW, north_west.getShielded());
		assertArrayEquals(expected_SE, south_east.getShielded());
		assertArrayEquals(expected_SW, south_west.getShielded());
		assertArrayEquals(expected_none, none.getShielded());
	}
}