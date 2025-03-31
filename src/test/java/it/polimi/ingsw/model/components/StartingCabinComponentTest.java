package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartingCabinComponentTest {

    @Test
    void check() {
    }

    @Test
    void getCrew() {
    }

    @Test
    void getCrewType() {
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        StartingCabinComponent cabin = new StartingCabinComponent(1, connectors, ComponentRotation.U000, PlayerColor.RED);
        assertEquals(AlienType.HUMAN, cabin.getCrewType());
    }

    @Test
    void getColor() {
        ConnectorType[] connectors = {ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL};
        StartingCabinComponent red_cabin = new StartingCabinComponent(1, connectors, ComponentRotation.U000, PlayerColor.RED);
        StartingCabinComponent blue_cabin = new StartingCabinComponent(1, connectors, ComponentRotation.U000, PlayerColor.BLUE);
        StartingCabinComponent green_cabin = new StartingCabinComponent(1, connectors, ComponentRotation.U000, PlayerColor.GREEN);
        StartingCabinComponent yellow_cabin = new StartingCabinComponent(1, connectors, ComponentRotation.U000, PlayerColor.YELLOW);
        assertEquals(PlayerColor.RED, red_cabin.getColor());
        assertEquals(PlayerColor.BLUE, blue_cabin.getColor());
        assertEquals(PlayerColor.GREEN, green_cabin.getColor());
        assertEquals(PlayerColor.YELLOW, yellow_cabin.getColor());
    }

    @Test
    void setCrew() {
    }

    @Test
    void onCreation() {
    }

    @Test
    void onDelete() {
    }
}