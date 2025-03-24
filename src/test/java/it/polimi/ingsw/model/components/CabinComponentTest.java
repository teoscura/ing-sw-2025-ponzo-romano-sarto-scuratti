package it.polimi.ingsw.model.components;

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
	private CabinComponent component_both;
	private CabinComponent component_brown;
	private CabinComponent component_purple;
	private CabinComponent component_human;
	private SpaceShip ship;


	@BeforeEach
	void setUp() {
		coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL};
		component_both = new CabinComponent(connectors, ComponentRotation.U000, coords);
		component_brown = new CabinComponent(connectors, ComponentRotation.U000,coords);
		component_purple = new CabinComponent(connectors, ComponentRotation.U000, coords);
		component_human = new CabinComponent(connectors, ComponentRotation.U000, coords);
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

		component_brown.setCrew(ship, 1, AlienType.BROWN);
		assertEquals(1, component_brown.getCrew());
	}

	@Test
	void getCrewType() {
		assertEquals(AlienType.HUMAN, component_human.getCrewType());
		ConnectorType[] connectors2 = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL};
		AlienLifeSupportComponent brown_support = new AlienLifeSupportComponent(connectors2, ComponentRotation.U000, AlienType.BROWN);
		CabinVisitor v = new CabinVisitor();
		//assertEquals(AlienType.BROWN, brown_support.check(v));
		assertEquals(AlienType.BROWN, component_brown.getCrewType());
		assertEquals(AlienType.PURPLE, component_purple.getCrewType());
		assertEquals(AlienType.BOTH, component_both.getCrewType());
	}

	@Test
	void setCrew() {
		NegativeArgumentException e1 = assertThrows(NegativeArgumentException.class, () -> component_both.setCrew(ship, 0, AlienType.HUMAN));
		IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> component_both.setCrew(ship, 1, AlienType.BOTH));
		ship.addComponent(component_human, coords);
		UnsupportedAlienCabinException e3 = assertThrows(UnsupportedAlienCabinException.class, () -> component_human.setCrew(ship, 1, AlienType.BROWN));
	}
}