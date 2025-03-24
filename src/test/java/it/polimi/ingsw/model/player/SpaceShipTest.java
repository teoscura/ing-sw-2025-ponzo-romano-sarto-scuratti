package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.rulesets.GameModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceShipTest {

    private SpaceShip ship;

    @BeforeEach
    void setUp() {
        ship = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
    }

    @Test
    void getType() {
    }

    @Test
    void getCredits() {
    }

    @Test
    void takeCredits() {
    }

    @Test
    void giveCredits() {
    }

    @Test
    void updateCrew() {
    }

    @Test
    void getCrew() {
    }

    @Test
    void getColor() {
    }

    @Test
    void verify() {
    }

    @Test
    void verifyAndClean() {
    }

    @Test
    void addComponent() {
    }

    @Test
    void removeComponent() {
    }

    @Test
    void updateShip() {
    }

    @Test
    void resetPower() {
    }

    @Test
    void turnOn() {
    }

    @Test
    void getComponent() {
    }

    @Test
    void getCannonPower() {
    }

    @Test
    void getEnginePower() {
    }

    @Test
    void getEnergyPower() {
    }

    @Test
    void getShieldedDirections() {
    }

    @Test
    void getHeight() {
    }

    @Test
    void getWidth() {
    }

    @Test
    void getEmpty() {
    }

    @Test
    void addStorageCoords() {
    }

    @Test
    void delStorageCoords() {
    }

    @Test
    void addCabinCoords() {
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        ShipCoords coords = new ShipCoords(GameModeType.LVL2, 3, 3);
        CabinComponent cabin = new CabinComponent(connectors, ComponentRotation.U000, coords);
        ship.addComponent(cabin, coords);

    }

    @Test
    void delCabinCoords() {
    }

    @Test
    void addBatteryCoords() {
    }

    @Test
    void delBatteryCoords() {
    }

    @Test
    void addPowerableCoords() {
    }

    @Test
    void delPowerableCoords() {
    }

    @Test
    void getTotalCrew() {
    }

    @Test
    void setCenterCabin() {
    }

    @Test
    void getCenterCabin() {
    }

    @Test
    void findConnectedCabins() {
    }

    @Test
    void countExposedConnectors() {
    }

    @Test
    void handleMeteorite() {
    }

    @Test
    void handleShot() {
    }
}