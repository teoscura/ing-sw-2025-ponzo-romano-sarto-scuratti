package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.SpaceShip;
import it.polimi.ingsw.model.rulesets.GameModeType;
import javafx.scene.control.Alert.AlertType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinComponentTest {
	private CabinComponent component_both;
	private CabinComponent component_brown;
	private CabinComponent component_purple;
	private CabinComponent component_human;
	private SpaceShip ship;

	@BeforeEach
	void setUp() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL};
		component_both = new CabinComponent(connectors, ComponentRotation.U000);
		component_brown = new CabinComponent(connectors, ComponentRotation.U000);
		component_purple = new CabinComponent(connectors, ComponentRotation.U000);
		component_human = new CabinComponent(connectors, ComponentRotation.U000);
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
		component_human.setCrew(ship, 2, AlienType.HUMAN);
		assertEquals(2, component_human.getCrew());

	}

	@Test
	void getCrewType() { //TODO 
		assertEquals(null, component_both.getCrewType());
		component_both.setCrew(ship, 0, AlienType.BROWN);
		//assertEquals(AlienType.BOTH, component_both.getCrewType());

	}

	@Test
	void setCrew() {
	}

	@Test
	void updateCrewType() { //TODO
	}
}