package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

	private SpaceShip ship;

	@BeforeEach
	void setUp() {
		ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
	}

	@Test
	void getCrew() {
		int[] expected_crew = {2, 0, 0};
		assertArrayEquals(expected_crew, ship.getCrew());
	}

	@Test
	void verify() {
		VerifyResult[][] check_results = new VerifyResult[ship.getHeight()][ship.getWidth()];
		check_results = ship.verify();
		assertEquals(VerifyResult.GOOD, check_results[2][3]);
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 4, 2);
		StructuralComponent component1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.SINGLE_CONNECTOR, ConnectorType.EMPTY, ConnectorType.SINGLE_CONNECTOR},
				ComponentRotation.U000, coords1);
		ship.addComponent(component1, coords1);
		check_results = ship.verify();
		assertEquals(VerifyResult.GOOD, check_results[2][4]);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 5, 2);
		StructuralComponent component2 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.DOUBLE_CONNECTOR},
				ComponentRotation.U000, coords2);
		ship.addComponent(component2, coords2);
		check_results = ship.verify();
		assertEquals(VerifyResult.NOT_LINKED, check_results[3][3]);
		assertEquals(VerifyResult.BROKEN, check_results[2][4]);
		assertEquals(VerifyResult.NOT_LINKED, check_results[2][5]);
	}

	@Test
	void addComponent() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 4);
		iBaseComponent component = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, coords);
		assertDoesNotThrow(() -> {
			ship.addComponent(component, coords);
		});
		assertEquals(component, ship.getComponent(coords));

		assertThrows(NullPointerException.class, () -> {
			ship.addComponent(null, coords);
		});
		assertThrows(NullPointerException.class, () -> {
			ship.addComponent(component, null);
		});

		ShipCoords ForbiddenCoords = new ShipCoords(GameModeType.LVL2, 0, 0);
		assertThrows(IllegalComponentAdd.class, () -> {
			ship.addComponent(component, ForbiddenCoords);
		});

		ShipCoords newcoords = new ShipCoords(GameModeType.LVL2, 5, 4);

		iBaseComponent newcomponent1 = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, newcoords);
		assertDoesNotThrow(() -> ship.addComponent(newcomponent1, newcoords));
		assertEquals(newcomponent1, ship.getComponent(newcoords));
		iBaseComponent newcomponent2 = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, newcoords);
		assertThrows(IllegalComponentAdd.class, () -> ship.addComponent(newcomponent2, newcoords));
	}

	@Test
	void removeComponent() {
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 4, 2);
		StructuralComponent component1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL},
				ComponentRotation.U000, coords1);
		ship.addComponent(component1, coords1);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 5, 2);
		StructuralComponent component2 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL},
				ComponentRotation.U000, coords2);
		ship.addComponent(component2, coords2);
		ship.removeComponent(coords1);
		assertEquals(ship.getEmpty(), ship.getComponent(coords1));
	}

	@Test
	void resetPower() {
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		ShipCoords cannon_coords = new ShipCoords(GameModeType.LVL2, 4, 2);
		ShipCoords engine_coords = new ShipCoords(GameModeType.LVL2, 5, 2);
		CannonComponent cannon = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.DOUBLE, cannon_coords);
		ship.addComponent(cannon, cannon_coords);
		EngineComponent engine = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE, engine_coords);
		ship.addComponent(engine, engine_coords);
		assertEquals(0, ship.getEnginePower());
		assertEquals(0, ship.getCannonPower());
		cannon.turnOn();
		engine.turnOn();
		ship.updateShip();
		assertEquals(2, ship.getEnginePower());
		assertEquals(2, ship.getCannonPower());
		ship.resetPower();
		assertEquals(0, ship.getEnginePower());
		assertEquals(0, ship.getCannonPower());
	}

	@Test
	void turnOn() { //to finish
		ShipCoords TargetCoords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ShipCoords BatteryCoords = new ShipCoords(GameModeType.LVL2, 4, 4);
		//ShipCoords TestCoords = new ShipCoords(GameModeType.LVL2, 5, 4);
		assertThrows(NullPointerException.class, () -> ship.turnOn(null, BatteryCoords));
		assertThrows(NullPointerException.class, () -> ship.turnOn(TargetCoords, null));

		assertThrows(IllegalTargetException.class, () -> ship.turnOn(TargetCoords, BatteryCoords));

		ship.addPowerableCoords(TargetCoords);
		assertThrows(IllegalTargetException.class, () -> ship.turnOn(TargetCoords, BatteryCoords));

		ship.addComponent(new BatteryComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, BatteryType.DOUBLE, BatteryCoords), BatteryCoords);
		assertDoesNotThrow(() -> ship.turnOn(TargetCoords, BatteryCoords));
	}

	@Test
	void getComponent() {
		assertTrue(null!=ship.getComponent(new ShipCoords(GameModeType.TEST, 3, 2)));
		assertTrue(ship.getEmpty()==ship.getComponent(new ShipCoords(GameModeType.TEST, 3, 3)));
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 3, 3);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 4, 3);
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		EngineComponent single_engine = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.SINGLE, coords1);
		EngineComponent double_engine = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE, coords2);
		ship.addComponent(single_engine, coords1);
		ship.addComponent(double_engine, coords2);
		assertTrue(single_engine==ship.getComponent(coords1));
		assertTrue(double_engine==ship.getComponent(coords2));
	}

	@Test
	void getCannonPower() {
		assertEquals(0, ship.getCannonPower());
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 2, 2);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 4, 2);
		ShipCoords coords3 = new ShipCoords(GameModeType.LVL2, 3, 1);
		CannonComponent single_cannon = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.SINGLE, coords1);
		CannonComponent double_cannon = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.DOUBLE, coords2);
		BatteryComponent bat = new BatteryComponent(1, connectors, ComponentRotation.U000, BatteryType.TRIPLE);
		ship.addComponent(single_cannon, coords1);
		ship.addComponent(double_cannon, coords2);
		ship.addComponent(bat, coords3);
		assertEquals(1, ship.getCannonPower());
		ship.turnOn(coords2, coords3);
		System.out.println(((CannonComponent)ship.getComponent(coords2)).getCurrentPower());
		assertEquals(3, ship.getCannonPower());
	}

	@Test
	void getEnginePower() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL};
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 3, 3);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 4, 3);
		ShipCoords coords3 = new ShipCoords(GameModeType.LVL2, 3, 1);
		EngineComponent single_engine = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.SINGLE, coords1);
		EngineComponent double_engine = new EngineComponent(1, connectors, ComponentRotation.U000, EngineType.DOUBLE, coords2);
		BatteryComponent bat = new BatteryComponent(1, connectors, ComponentRotation.U000, BatteryType.TRIPLE);
		ship.addComponent(single_engine, coords1);
		ship.addComponent(double_engine, coords2);
		ship.addComponent(bat, coords3);
		assertEquals(1, ship.getEnginePower());
		ship.turnOn(coords2, coords3);
		assertEquals(3, ship.getEnginePower());
	}

	@Test
	void getEnergyPower() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		assertEquals(0, ship.getEnergyPower());
		BatteryComponent battery = new BatteryComponent(1, connectors, ComponentRotation.U000, BatteryType.TRIPLE, coords);
		ship.addComponent(battery, coords);
		assertEquals(3, ship.getEnergyPower());
	}

	@Test
	void getShieldedDirections() {
		ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 3, 3);
		ShipCoords coords2 = new ShipCoords(GameModeType.LVL2, 4, 3);
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShieldComponent shield1 = new ShieldComponent(1, connectors, ComponentRotation.U000, coords1);
		ShieldComponent shield2 = new ShieldComponent(1, connectors, ComponentRotation.U180, coords2);
		assertArrayEquals(new boolean[]{false, false, false, false}, ship.getShieldedDirections());
		ship.addComponent(shield1, coords1);
		ship.addComponent(shield2, coords2);
		shield1.turnOn();
		ship.updateShip();
		assertArrayEquals(new boolean[]{true, true, false, false}, ship.getShieldedDirections());
		shield2.turnOn();
		ship.updateShip();
		assertArrayEquals(new boolean[]{true, true, true, true}, ship.getShieldedDirections());
	}

	@Test
	void getHeight() {
		assertEquals(5, ship.getHeight());
		SpaceShip test_ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		assertEquals(5, test_ship.getHeight());
	}

	@Test
	void getWidth() {
		assertEquals(7, ship.getWidth());
		SpaceShip test_ship = new SpaceShip(GameModeType.LVL2, new Player(GameModeType.LVL2, "tizio", PlayerColor.RED));
		assertEquals(7, test_ship.getWidth());
	}

	@Test
	void getEmpty() {
	}

	@Test
	void addStorageCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ship.addStorageCoords(coords);
		NotUniqueException e = assertThrows(NotUniqueException.class, () -> ship.addStorageCoords(coords));
		assertEquals("Coords are already present in storage coords", e.getMessage());
	}

	@Test
	void delStorageCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		NotPresentException e = assertThrows(NotPresentException.class, () -> ship.delStorageCoords(coords));
		assertEquals("Coords arent present in storage coords", e.getMessage());
		ship.addStorageCoords(coords);
		assertDoesNotThrow(() -> ship.delStorageCoords(coords));
	}

	@Test
	void addCabinCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ship.addCabinCoords(coords);
		NotUniqueException e = assertThrows(NotUniqueException.class, () -> ship.addCabinCoords(coords));
		assertEquals("Coords are already present in cabin coords", e.getMessage());
	}

	@Test
	void delCabinCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		NotPresentException e = assertThrows(NotPresentException.class, () -> ship.delCabinCoords(coords));
		assertEquals("Coords arent present in cabin coords", e.getMessage());
		ship.addCabinCoords(coords);
		assertDoesNotThrow(() -> ship.delCabinCoords(coords));
	}

	@Test
	void addBatteryCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ship.addBatteryCoords(coords);
		NotUniqueException e = assertThrows(NotUniqueException.class, () -> ship.addBatteryCoords(coords));
		assertEquals("Coords are already present in battery coords", e.getMessage());
	}

	@Test
	void delBatteryCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		NotPresentException e = assertThrows(NotPresentException.class, () -> ship.delBatteryCoords(coords));
		assertEquals("Coords arent present in battery coords", e.getMessage());
		ship.addBatteryCoords(coords);
		assertDoesNotThrow(() -> ship.delBatteryCoords(coords));
	}

	@Test
	void addPowerableCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		ship.addPowerableCoords(coords);
		NotUniqueException e = assertThrows(NotUniqueException.class, () -> ship.addPowerableCoords(coords));
		assertEquals("Coords are already present in powerable coords", e.getMessage());
	}

	@Test
	void delPowerableCoords() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		NotPresentException e = assertThrows(NotPresentException.class, () -> ship.delPowerableCoords(coords));
		assertEquals("Coords arent present in powerable coords", e.getMessage());
		ship.addPowerableCoords(coords);
		assertDoesNotThrow(() -> ship.delPowerableCoords(coords));
	}

	@Test
	void getTotalCrew() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		assertEquals(2, ship.getTotalCrew());
		CabinComponent human_cabin = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		ship.addComponent(human_cabin, coords);
		human_cabin.setCrew(ship, 2, AlienType.HUMAN);
		ship.updateShip();
		assertEquals(4, ship.getTotalCrew());
	}

	@Test
	void setCenter() {
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		assertThrows(IllegalTargetException.class, () -> ship.setCenter(coords));
	}

	@Test
	void getCenter() {
		ShipCoords expected = new ShipCoords(GameModeType.LVL2, 3, 2);
		ShipCoords actual = ship.getCenter();
		assertEquals(expected.x, actual.x);
		assertEquals(expected.y, actual.y);
	}

	@Test
	void findConnectedCabins() {
		ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
		CabinComponent cabin1 = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		ship.addComponent(cabin1, coords);
		ArrayList<ShipCoords> results = ship.findConnectedCabins();
		assertTrue(results.contains(coords));
		assertTrue(results.contains(ship.getCenter()));
	}

	@Test
	void countExposedConnectors() {
		assertEquals(4, ship.countExposedConnectors());
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY};
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 1);
		StructuralComponent component1 = new StructuralComponent(1, connectors, ComponentRotation.U000, coords);
		ship.addComponent(component1, coords);
		assertEquals(3, ship.countExposedConnectors());
	}

	@Test
	void handleMeteorite() {
		Projectile missed_meteor = new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL, 2);
		assertFalse(ship.handleMeteorite(missed_meteor));
		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY};
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 2, 2);
		CannonComponent cannon = new CannonComponent(1, connectors, ComponentRotation.U000, CannonType.SINGLE, coords);
		ship.addComponent(cannon, coords);
		Projectile meteor = new Projectile(ProjectileDirection.U090, ProjectileDimension.BIG, 7);
		ship.handleMeteorite(meteor);
		assertEquals(ship.getEmpty(), ship.getComponent(coords));
	}

	@Test
	void handleShot() {
		Projectile missed_shot = new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL, 2);
		assertFalse(ship.handleShot(missed_shot));

		ConnectorType[] connectors = {ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY};
		ShipCoords coords = new ShipCoords(GameModeType.LVL2, 2, 2);
		CabinComponent cabin = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
		ship.addComponent(cabin, coords);

		Projectile shot = new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL, 7);
		boolean hitCenter = ship.handleShot(shot);

		assertEquals(ship.getEmpty(), ship.getComponent(coords));

		assertEquals(coords.equals(ship.getCenter()), hitCenter);
	}

}