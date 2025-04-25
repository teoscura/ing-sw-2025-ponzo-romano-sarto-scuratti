package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.utils.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanetCardTest {

	PlanetCard card;
	Planet planet1;
	Planet planet2;
	Planet planet3;

	@BeforeEach
	void setUp() {
		planet1 = new Planet(new int[]{2, 1, 0, 0});
		planet2 = new Planet(new int[]{1, 1, 1, 0});
		planet3 = new Planet(new int[]{0, 1, 0, 2});
		Planet[] planets = new Planet[]{planet1, planet2, planet3};
		card = new PlanetCard(1, 2, planets);
	}

	@Test
	void getRequest() {
	}

	@Test
	void getResponse() {
	}

	@Test
	void getAfterResponse() {
	}

	@Test
	void getOrder() {
	}

	@Test
	void apply() {
	}
}