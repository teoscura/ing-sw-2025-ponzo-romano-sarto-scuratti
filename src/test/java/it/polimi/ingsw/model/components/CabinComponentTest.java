package it.polimi.ingsw.model.components;

import it.polimi.ingsw.exceptions.ArgumentTooBigException;
import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CabinVisitor;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;
import javafx.scene.control.Alert.AlertType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinComponentTest {

	private ShipCoords coords;
	ShipCoords coords2;
	private CabinComponent component_both;
	private CabinComponent component_brown;
	private CabinComponent component_purple;
	private CabinComponent component_human;
	private SpaceShip ship;


	@BeforeEach
	void setUp() {
		coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		coords2 = new ShipCoords(GameModeType.LVL2, 4, 4);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL};
		component_both = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		component_brown = new CabinComponent(1, connectors, ComponentRotation.U000,coords);
		component_purple = new CabinComponent(1, connectors, ComponentRotation.U000, coords2);
		component_human = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		ship = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
	}

	@Test
	void check() {
	}

	@Test
	void getCrew() {
		assertEquals(0, component_both.getCrew());
		assertEquals(0, component_brown.getCrew());
		assertEquals(0, component_purple.getCrew());
		assertEquals(0, component_human.getCrew());

		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ship.addComponent(component_human, coords);
		component_human.setCrew(ship, 2, AlienType.HUMAN);
		assertEquals(2, component_human.getCrew());

		/*component_brown.setCrew(ship, 1, AlienType.BROWN);
		assertEquals(1, component_brown.getCrew());*/
	}

	@Test
	void getCrewType() {
		assertEquals(AlienType.HUMAN, component_human.getCrewType());
		ship.addComponent(component_human, coords);
		component_brown.setCrew(ship, 1, AlienType.BROWN);
		assertEquals(AlienType.BROWN, component_brown.getCrewType());

		ship.addComponent(component_purple, coords2);
		component_purple.setCrew(ship, 1, AlienType.PURPLE);
		assertEquals(AlienType.PURPLE, component_purple.getCrewType());
	}

	@Test
	void setCrew() {
		ship.addComponent(component_human, coords);
		NegativeArgumentException e1 = assertThrows(NegativeArgumentException.class, () -> component_both.setCrew(ship, 0, AlienType.HUMAN));
		IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> component_both.setCrew(ship, 1, AlienType.BOTH));
		ArgumentTooBigException e3 = assertThrows(ArgumentTooBigException.class, () -> component_both.setCrew(ship, 3, AlienType.HUMAN));
		assertEquals(0, component_both.getCrew());

		UnsupportedAlienCabinException e4 = assertThrows(UnsupportedAlienCabinException.class, () -> component_human.setCrew(ship, 1, AlienType.BROWN));
		component_both.setCrew(ship, 2, AlienType.HUMAN);
		assertEquals(2, component_both.getCrew());
	}
}