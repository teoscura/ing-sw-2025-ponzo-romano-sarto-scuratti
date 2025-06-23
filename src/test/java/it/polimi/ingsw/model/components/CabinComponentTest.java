package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CabinComponentTest {

	ShipCoords coords2;
	private ShipCoords coords;
	private CabinComponent component_both;
	private CabinComponent component_human;
	private SpaceShip ship;


	@BeforeEach
	void setUp() {
		coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		coords2 = new ShipCoords(GameModeType.LVL2, 4, 4);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL};
		component_both = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		component_human = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
	}

	@Test
	void check() {
	}

	@Test
	void getCrewType() {
		assertEquals(AlienType.HUMAN, component_human.getCrewType());
		ship.addComponent(component_human, coords);
	}

	@Test
	void setCrew() {
		ship.addComponent(component_human, coords);
		assertThrows(NegativeArgumentException.class, () -> component_both.setCrew(ship, -1, AlienType.HUMAN));
		assertThrows(IllegalArgumentException.class, () -> component_both.setCrew(ship, 1, AlienType.BOTH));
		assertThrows(ArgumentTooBigException.class, () -> component_both.setCrew(ship, 3, AlienType.HUMAN));
		assertEquals(2, component_both.getCrew());

		assertThrows(UnsupportedAlienCabinException.class, () -> component_human.setCrew(ship, 1, AlienType.BROWN));
		component_both.setCrew(ship, 2, AlienType.HUMAN);
		assertEquals(2, component_both.getCrew());
	}

}