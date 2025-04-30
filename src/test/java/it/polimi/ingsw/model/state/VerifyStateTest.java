package it.polimi.ingsw.model.state;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerifyStateTest {

    private DummyModelInstance model;
    private VoyageState state;
    private Planche planche;
    private iCards deck;
    private Player player1;
    private ClientDescriptor p1desc;
    private Player player2;
    private ClientDescriptor p2desc;
    private Player player3;
    private ClientDescriptor p3desc;

    @BeforeEach
    void setUp() {
        BaseComponent c;
        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();
        ComponentFactory f3 = new ComponentFactory();

        //player stabile
        player1 = new Player(GameModeType.LVL2, "player1", PlayerColor.RED);
        c = f1.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f1.getComponent(79);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f1.getComponent(26);
        c.rotate(ComponentRotation.U180);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 1));
        c = f1.getComponent(25);
        c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 2));
        p1desc = new ClientDescriptor("player1", null);
        p1desc.bindPlayer(player1);

        //player ciambella
        player2 = new Player(GameModeType.LVL2, "player2", PlayerColor.BLUE);
        c = f2.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f2.getComponent(78);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f2.getComponent(56);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 1));
        c = f2.getComponent(58);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 1));
        c = f2.getComponent(43);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 2));
        c = f2.getComponent(59);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 3));
        c = f2.getComponent(23);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 3));
        p2desc = new ClientDescriptor("player2", null);
        p2desc.bindPlayer(player2);

        //player manubrio
        player3 = new Player(GameModeType.LVL2, "player3", PlayerColor.GREEN);
        c = f3.getComponent(41);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 1));
        c = f3.getComponent(79);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 3, 3));
        c = f3.getComponent(26);
        c.rotate(ComponentRotation.U180);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 1));
        c = f3.getComponent(25);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 2, 2));
        c = f3.getComponent(57);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 4, 2)); //componente da colpire per separare la nave a metà
        c = f3.getComponent(19);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 2));
        c = f3.getComponent(22);
        c.rotate(ComponentRotation.U270);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 1));
        c = f3.getComponent(70);
        c.rotate(ComponentRotation.U090);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.LVL2, 5, 3));
        p3desc = new ClientDescriptor("player3", null);
        p3desc.bindPlayer(player3);

    }

    @Test
    public void test() {

    }

}