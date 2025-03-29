package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.model.player.exceptions.NegativeCreditsException;
import it.polimi.ingsw.model.rulesets.GameModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip ship;

    @BeforeEach
    void setUp() {
        ship = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
    }

    @Test
    void getType() {
        SpaceShip ship_lv1 = new SpaceShip(GameModeType.TEST, PlayerColor.RED);
        assertEquals(GameModeType.TEST, ship_lv1.getType());
        assertEquals(GameModeType.LVL2, ship.getType());
    }

    @Test
    void getCredits() {
        assertEquals(0, ship.getCredits());
        ship.giveCredits(5);
        assertEquals(5, ship.getCredits());
    }

    @Test
    void takeCredits() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> ship.takeCredits(0));
        assertEquals("Cannot take negative credits.", e1.getMessage());
        NegativeCreditsException e2 = assertThrows(NegativeCreditsException.class, () -> ship.takeCredits(1));
        ship.giveCredits(5);
        ship.takeCredits(3);
        assertEquals(2, ship.getCredits());
    }

    @Test
    void giveCredits() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> ship.giveCredits(0));
        assertEquals("Cannot earn negative credits.", e1.getMessage());
        ship.giveCredits(5);
        assertEquals(5, ship.getCredits());
    }

    @Test
    void updateCrew() {
        int[] expected_crew = {0,0,0};
        assertArrayEquals(expected_crew, ship.getCrew());
        ship.updateCrew(2, AlienType.HUMAN);
        ship.updateCrew(1, AlienType.PURPLE);
        ship.updateCrew(1, AlienType.BROWN);
        assertArrayEquals(new int[]{2,1,1}, ship.getCrew());
    }

    @Test
    void getCrew() {
        int[] expected_crew = {0,0,0};
        assertArrayEquals(expected_crew, ship.getCrew());
    }

    @Test
    void getColor() {
        assertEquals(PlayerColor.RED, ship.getColor());
        SpaceShip blue_ship = new SpaceShip(GameModeType.LVL2, PlayerColor.BLUE);
        SpaceShip green_ship = new SpaceShip(GameModeType.LVL2, PlayerColor.GREEN);
        SpaceShip yellow_ship = new SpaceShip(GameModeType.LVL2, PlayerColor.YELLOW);
        assertEquals(PlayerColor.BLUE, blue_ship.getColor());
        assertEquals(PlayerColor.GREEN, green_ship.getColor());
        assertEquals(PlayerColor.YELLOW, yellow_ship.getColor());
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
    void verifyAndClean() {
        ShipCoords coords1 = new ShipCoords(GameModeType.LVL2, 5, 2);
        StructuralComponent component1 = new StructuralComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.SINGLE_CONNECTOR, ConnectorType.EMPTY, ConnectorType.SINGLE_CONNECTOR},
                ComponentRotation.U000, coords1);
        ship.addComponent(component1, coords1);
        VerifyResult[][] check_results = ship.verify();
        assertEquals(VerifyResult.NOT_LINKED, check_results[2][5]);
        assertEquals(component1, ship.getComponent(coords1));
        ship.verifyAndClean();
        assertEquals(ship.getEmpty(), ship.getComponent(coords1));
    }

    @Test
    void addComponent() {
        ShipCoords coords = new ShipCoords(GameModeType.LVL2,4, 4);
        iBaseComponent component = new StructuralComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, coords);
        assertDoesNotThrow(() -> {ship.addComponent(component, coords);});
        assertEquals(component, ship.getComponent(coords));

        assertThrows(NullPointerException.class, () -> {ship.addComponent(null, coords);});
        assertThrows(NullPointerException.class, () -> {ship.addComponent(component, null);});

        ShipCoords Invalidcoords1 = new ShipCoords(GameModeType.LVL2,-1, 4);
        ShipCoords Invalidcoords2 = new ShipCoords(GameModeType.LVL2,4, -1);
        assertThrows(OutOfBoundsException.class, () -> {ship.addComponent(component, Invalidcoords1);});
        assertThrows(OutOfBoundsException.class, () -> {ship.addComponent(component, Invalidcoords2);});

        ShipCoords ForbiddenCoords = new ShipCoords(GameModeType.LVL2,0, 0);
        assertThrows(IllegalComponentAdd.class, () -> {ship.addComponent(component, ForbiddenCoords);});

        ShipCoords newcoords = new ShipCoords(GameModeType.LVL2,5, 4);

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
        assertEquals(ship.getEmpty(), ship.getComponent(coords2));
    }

    @Test
    void updateShip() {
        ShipCoords engineCoords = new ShipCoords(GameModeType.LVL2, 0, 4);
        EngineComponent engine = new EngineComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, EngineType.SINGLE, engineCoords);
        ship.addComponent(engine, engineCoords);

        ShipCoords batteryCoords = new ShipCoords(GameModeType.LVL2, 1, 4);
        BatteryComponent battery = new BatteryComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, BatteryType.DOUBLE, batteryCoords);
        ship.addComponent(battery, batteryCoords);

        ShipCoords cannnonCoords = new ShipCoords(GameModeType.LVL2, 2, 4);
        CannonComponent cannon = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, CannonType.SINGLE, cannnonCoords);
        ship.addComponent(cannon, cannnonCoords);

        ShipCoords storageCoords = new ShipCoords(GameModeType.LVL2, 4, 4);
        StorageComponent storage = new StorageComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, StorageType.TRIPLENORMAL, storageCoords);
        ship.addComponent(storage, storageCoords);

        ShipCoords shieldCoords = new ShipCoords(GameModeType.LVL2, 5, 4);
        ShieldComponent shield = new ShieldComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, shieldCoords);
        ship.addComponent(shield, shieldCoords);

        ShipCoords cabinCoords = new ShipCoords(GameModeType.LVL2, 6, 4);
        CabinComponent cabin = new CabinComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, cabinCoords);
        ship.addComponent(cabin, cabinCoords);

        ShipCoords cabinAdiacentCoords = new ShipCoords(GameModeType.LVL2, 6, 3);
        AlienLifeSupportComponent cabinAdiacent = new AlienLifeSupportComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, AlienType.BROWN);
        ship.addComponent(cabinAdiacent, cabinAdiacentCoords);

        cabin.setCrew(ship, 1, AlienType.BROWN);
       // cabin.setCrew(ship, 2, AlienType.PURPLE);


        // storage.putIn(ShipmentType.RED);
        storage.putIn(ShipmentType.BLUE);
        storage.putIn(ShipmentType.GREEN);
        storage.putIn(ShipmentType.YELLOW);

        ship.updateShip();

        assertNotNull(ship.getCrew());

        assertEquals(1, ship.getCrew()[1]); //brown

        assertEquals(2, ship.getEnergyPower()); //double

        assertEquals(1, ship.getCannonPower());  //single

        assertEquals(3, ship.getEnginePower());  //single

        //assertNotNull(ship.getShieldedDirections());

        assertEquals(1, ship.getStorageContainers()[0]);
        assertEquals(1, ship.getStorageContainers()[1]);
        assertEquals(1, ship.getStorageContainers()[2]);

        assertEquals(1, ship.getCannonPower());
        assertEquals(3, ship.getEnginePower());
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
        ShipCoords TestCoords = new ShipCoords(GameModeType.LVL2, 5, 4);
        assertThrows(NullPointerException.class, () -> ship.turnOn(null, BatteryCoords));
        assertThrows(NullPointerException.class, () -> ship.turnOn(TargetCoords, null));

        assertThrows(IllegalTargetException.class, () -> ship.turnOn(TargetCoords, BatteryCoords));

        ship.addPowerableCoords(TargetCoords);
        assertThrows(IllegalTargetException.class, () -> ship.turnOn(TargetCoords, BatteryCoords));

        ship.addComponent(new BatteryComponent(2, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, BatteryType.DOUBLE ,BatteryCoords), BatteryCoords);
        assertDoesNotThrow(() -> ship.turnOn(TargetCoords, BatteryCoords));
    }

    @Test
    void getComponent() {
        ShipCoords illegal_coords1 = new ShipCoords(GameModeType.LVL2, -1, 4);
        ShipCoords illegal_coords2 = new ShipCoords(GameModeType.LVL2, 3, 8);
        ShipCoords coords = new ShipCoords(GameModeType.LVL2, 4, 2);
        NullPointerException e = assertThrows(NullPointerException.class, () -> ship.getComponent(null));
        OutOfBoundsException e1 = assertThrows(OutOfBoundsException.class, () -> ship.getComponent(illegal_coords1));
        OutOfBoundsException e2 = assertThrows(OutOfBoundsException.class, () -> ship.getComponent(illegal_coords2));
        StructuralComponent component = new StructuralComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL},
                ComponentRotation.U000, coords);
        ship.addComponent(component, coords);

    }

    @Test
    void getCannonPower() {

    }

    @Test
    void getEnginePower() {

    }

    @Test
    void getEnergyPower(){
        ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        assertEquals(0, ship.getEnergyPower());
        BatteryComponent battery = new BatteryComponent(1, connectors, ComponentRotation.U000, BatteryType.TRIPLE, coords);
        ship.addComponent(battery, coords);
        assertEquals(3 , ship.getEnergyPower());
    }

    @Test
    void getShieldedDirections() {
    }

    @Test
    void getHeight() {
        assertEquals(5, ship.getHeight());
        SpaceShip test_ship = new SpaceShip(GameModeType.TEST, PlayerColor.RED);
        assertEquals(5, test_ship.getHeight());
    }

    @Test
    void getWidth() {
        assertEquals(7, ship.getWidth());
        SpaceShip test_ship = new SpaceShip(GameModeType.TEST, PlayerColor.RED);
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
        assertEquals(0, ship.getTotalCrew());
        CabinComponent human_cabin = new CabinComponent(1, connectors, ComponentRotation.U000, coords);
        ship.addComponent(human_cabin, coords);
        human_cabin.setCrew(ship, 2, AlienType.HUMAN);
        ship.updateShip();
        assertEquals(2, ship.getTotalCrew());
    }

    @Test
    void setCenterCabin() {
        ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
        IllegalTargetException e = assertThrows(IllegalTargetException.class, () -> ship.setCenterCabin(coords));
    }

    @Test
    void getCenterCabin() {
        ShipCoords expected = new ShipCoords(GameModeType.LVL2, 3, 2);
        ShipCoords actual = ship.getCenterCabin();
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
        assertTrue(results.contains(ship.getCenterCabin()));
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
    }

    @Test
    void handleShot(){

    }

    @Test
    void retireGetRetired(){
        assertFalse(ship.getRetired());
        ship.retire();
        assertTrue(ship.getRetired());
        AlreadyPoweredException e = assertThrows(AlreadyPoweredException.class, () -> ship.retire());
    }
}