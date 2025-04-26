package it.polimi.ingsw.model.player;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.cards.OpenSpaceCard;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.state.DummyVoyageState;

public class UpdateSpaceShipTest {

    //TODO: gestione nuova cabina centrale prima di verify
    private Player nocomponents;
    private Player dummy2;
    private Player dummy3;
    private Player dummy4;
    private Player dummy5;

	@BeforeEach
	void setUp() {
        ComponentFactory f = new ComponentFactory();
        iBaseComponent c = null;

        nocomponents =  new Player(GameModeType.TEST, "bingus", PlayerColor.RED);

        dummy2 =  new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(126);
        c.rotate(ComponentRotation.U000); 
        dummy2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
        c = f.getComponent(5);
        c.rotate(ComponentRotation.U000); 
        dummy2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));

        dummy3 =  new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(53);
        c.rotate(ComponentRotation.U000); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f.getComponent(38);
        c.rotate(ComponentRotation.U270); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
        c = f.getComponent(36);
        c.rotate(ComponentRotation.U180); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
        c = f.getComponent(137);
        c.rotate(ComponentRotation.U000);
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 4)); 
        c = f.getComponent(49);
        c.rotate(ComponentRotation.U000); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));


        dummy4 =  new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(96);
        c.rotate(ComponentRotation.U000); 
        dummy4.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f.getComponent(2);
        c.rotate(ComponentRotation.U000); 
        dummy4.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));


        dummy5 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(14);
        c.rotate(ComponentRotation.U090); 
        dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        c = f.getComponent(75);
        c.rotate(ComponentRotation.U000); 
        dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f.getComponent(98);
        c.rotate(ComponentRotation.U000); 
        dummy5.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
        
    }

    @Test
    void noComponentsUpdate(){
        CrewRemoveVisitor v = new CrewRemoveVisitor(nocomponents.getSpaceShip());
        System.out.println(nocomponents.getSpaceShip().getTotalCrew());
        assertTrue(2==nocomponents.getSpaceShip().getTotalCrew());
        nocomponents.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
        System.out.println(nocomponents.getSpaceShip().getTotalCrew());
        assertTrue(1==nocomponents.getSpaceShip().getTotalCrew());
        nocomponents.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
        System.out.println(nocomponents.getSpaceShip().getTotalCrew());
        assertTrue(0==nocomponents.getSpaceShip().getTotalCrew());
        assertTrue(4 == nocomponents.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy2Update(){
        assertTrue(2 == dummy2.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy2.getSpaceShip().getCannonPower());
        dummy2.getSpaceShip().turnOn(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 3, 3));
        assertTrue(2 == dummy2.getSpaceShip().getCannonPower());
        dummy2.getSpaceShip().resetPower();
        assertTrue(0 == dummy2.getSpaceShip().getCannonPower());
        CrewRemoveVisitor v = new CrewRemoveVisitor(dummy2.getSpaceShip());
        dummy2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
        assertTrue(1 == dummy2.getSpaceShip().getTotalCrew());
    }

    @Test
    void dummy3Update(){
        assertTrue(8 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());

        ((CabinComponent) dummy3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3))).setCrew(dummy3.getSpaceShip(), 1, AlienType.BROWN);
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 5, 3));
        assertTrue(5 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 4));
        assertTrue(4 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy2.getSpaceShip().getCannonPower());
    }

    @Test
    void dummy3Update2(){
        assertTrue(8 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        ((CabinComponent) dummy3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3))).setCrew(dummy3.getSpaceShip(), 1, AlienType.BROWN);
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        System.out.println(dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 4));
        System.out.println(dummy3.getSpaceShip().getTotalCrew());
        assertTrue(6 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 5, 3));
        assertTrue(4 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy2.getSpaceShip().getCannonPower());
    }

    @Test
    void dummy3Update3(){
        //Removed life support, but alien can still live.
        ComponentFactory f = new ComponentFactory();
        iBaseComponent c = null;
        c = f.getComponent(140);
        c.rotate(ComponentRotation.U000); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));

        assertTrue(8 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        ((CabinComponent) dummy3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3))).setCrew(dummy3.getSpaceShip(), 1, AlienType.BROWN);
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        System.out.println(dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 4));
        System.out.println(dummy3.getSpaceShip().getTotalCrew());
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 5, 3));
        System.out.println(dummy3.getSpaceShip().getTotalCrew());
        assertTrue(5 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy2.getSpaceShip().getCannonPower());
    }

    @Test
    void dummy4Update(){
        
    }

}
