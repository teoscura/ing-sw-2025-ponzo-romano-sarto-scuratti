package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.exceptions.CardAlreadyExhaustedException;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

	private static class TestCard extends Card {
		public TestCard(int id, int days) {
			super(id, days);
		}
		@Override
		public iCardResponse apply(iSpaceShip ship, iPlayerResponse response) {
			return null;
		}
	}

	private TestCard card;

	@BeforeEach
	void setUp() {
		card = new TestCard(2, 3);
	}

	@Test
	void getId() {
		assertEquals(2, card.getId());
	}

	@Test
	void multiPhase() {
		assertFalse(card.multiPhase());
	}

	@Test
	void nextPhase() {
	}

	@Test
	void hasMultipleRequirements() {
		assertFalse(card.hasMultipleRequirements());
	}

	@Test
	void getExhausted() {
		assertFalse(card.getExhausted());
	}

	@Test
	void exhaust() {
		card.exhaust();
		assertThrows(CardAlreadyExhaustedException.class, () -> card.exhaust());
	}

}