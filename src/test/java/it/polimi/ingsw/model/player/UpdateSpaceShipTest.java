package it.polimi.ingsw.model.player;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.cards.visitors.ContainsRemoveVisitor;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;

public class UpdateSpaceShipTest {

    private Player nocomponents;
    private Player dummy2, dummy3;
    private Player dummy4, dummy5;
    private Player dummy6, nocabin;
    private Player storage;


	@BeforeEach
	void setUp() {
        ComponentFactory f = new ComponentFactory();
        BaseComponent c = null;

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
        
        dummy6 = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(14);
        c.rotate(ComponentRotation.U090); 
        dummy6.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        c = f.getComponent(133);
        c.rotate(ComponentRotation.U000); 
        dummy6.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
        c = f.getComponent(120);
        c.rotate(ComponentRotation.U090); 
        dummy6.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));

        nocabin = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(41);
        c.rotate(ComponentRotation.U000); 
        nocabin.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        c = f.getComponent(39);
        c.rotate(ComponentRotation.U000); 
        nocabin.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1, 2));
        c = f.getComponent(42);
        c.rotate(ComponentRotation.U000); 
        nocabin.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
        c = f.getComponent(43);
        c.rotate(ComponentRotation.U000); 
        nocabin.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
        
        storage = new Player(GameModeType.TEST, "bingus", PlayerColor.RED);
        c = f.getComponent(62);
        c.rotate(ComponentRotation.U000); 
        storage.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        c = f.getComponent(30);
        c.rotate(ComponentRotation.U000); 
        storage.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
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
        //assertTrue( dummy2.getSpaceShip().countExposedConnectors());
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
        //assertTrue( dummy2.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy3Update2(){
        assertTrue(8 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        ((CabinComponent) dummy3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3))).setCrew(dummy3.getSpaceShip(), 1, AlienType.BROWN);
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 4));
        assertTrue(6 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 5, 3));
        assertTrue(4 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        //assertTrue( dummy3.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy3Update3(){
        //Removed life support, but alien can still live.
        ComponentFactory f = new ComponentFactory();
        BaseComponent c = null;
        c = f.getComponent(140);
        c.rotate(ComponentRotation.U000); 
        dummy3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));

        assertTrue(8 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        ((CabinComponent) dummy3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3))).setCrew(dummy3.getSpaceShip(), 1, AlienType.BROWN);
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 4));
        assertTrue(7 == dummy3.getSpaceShip().getTotalCrew());
        dummy3.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 5, 3));
        assertTrue(5 == dummy3.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy3.getSpaceShip().getCannonPower());
        //assertTrue( dummy3.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy4Update(){
        assertTrue(2 == dummy4.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy4.getSpaceShip().getCannonPower());
        assertTrue(0 == dummy4.getSpaceShip().getEnginePower());
        dummy4.getSpaceShip().turnOn(new ShipCoords(GameModeType.TEST, 3, 3), new ShipCoords(GameModeType.TEST, 2, 2));
        assertTrue(2 == dummy4.getSpaceShip().getEnginePower());
        assertTrue(2 == dummy4.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy4.getSpaceShip().getCannonPower());
        //assertTrue( dummy4.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy5Update(){
        assertTrue(2 == dummy5.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy5.getSpaceShip().getCannonPower());
        assertTrue(1 == dummy5.getSpaceShip().getEnginePower());
        dummy5.getSpaceShip().turnOn(new ShipCoords(GameModeType.TEST, 4, 3), new ShipCoords(GameModeType.TEST, 2, 2));
        assertTrue(3 == dummy5.getSpaceShip().getEnginePower());
        assertTrue(2 == dummy5.getSpaceShip().getTotalCrew());
        assertTrue(0 == dummy5.getSpaceShip().getCannonPower());
        //assertTrue( dummy5.getSpaceShip().countExposedConnectors());
    }

    @Test
    void dummy6Update(){
        assertTrue(2 == dummy6.getSpaceShip().getTotalCrew());
        System.out.println(dummy6.getSpaceShip().getCannonPower());
        assertTrue(0.5f == dummy6.getSpaceShip().getCannonPower());
        assertTrue(0 == dummy6.getSpaceShip().getEnginePower());
        dummy6.getSpaceShip().turnOn(new ShipCoords(GameModeType.TEST, 3, 1), new ShipCoords(GameModeType.TEST, 2, 2));
        assertTrue(0 == dummy6.getSpaceShip().getEnginePower());
        assertTrue(2 == dummy6.getSpaceShip().getTotalCrew());
        assertTrue(2.5 == dummy6.getSpaceShip().getCannonPower());
    }

    @Test
    void newCabinUpdateShot() throws ForbiddenCallException{
        assertTrue(10 == nocabin.getSpaceShip().getTotalCrew());
        nocabin.getSpaceShip().handleShot(new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL, 7));
        assertTrue(nocabin.getSpaceShip().getBrokeCenter());
        nocabin.getSpaceShip().setCenter(new ShipCoords(GameModeType.TEST,1,2));
        assertTrue(!nocabin.getSpaceShip().getBrokeCenter());
        assertTrue(4 == nocabin.getSpaceShip().getTotalCrew());
    }

    @Test
    void newCabinUpdateMeteorite() throws ForbiddenCallException{
        assertTrue(10 == nocabin.getSpaceShip().getTotalCrew());
        nocabin.getSpaceShip().handleMeteorite(new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL, 7));
        assertTrue(nocabin.getSpaceShip().getBrokeCenter());
        nocabin.getSpaceShip().setCenter(new ShipCoords(GameModeType.TEST,1,2));
        assertTrue(!nocabin.getSpaceShip().getBrokeCenter());
        assertTrue(4 == nocabin.getSpaceShip().getTotalCrew());
    }

    @Test
    void storage(){
        int[] test = new int[]{0,0,0,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        //2,2 speciale, 4,2 normale triplo.
        ContainsLoaderVisitor vl = new ContainsLoaderVisitor(storage.getSpaceShip(), ShipmentType.RED);
        ContainsRemoveVisitor vr = new ContainsRemoveVisitor(storage.getSpaceShip(), ShipmentType.EMPTY);
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(vl);
        test = new int[]{0,0,0,0,1};
        for(int i = 0; i < 5; i++){
            System.out.print(storage.getSpaceShip().getContains()[i]);
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        vr.changeType(ShipmentType.RED);
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(vr);
        test = new int[]{0,0,0,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        vl = new ContainsLoaderVisitor(storage.getSpaceShip(), ShipmentType.BLUE);
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(vl);
        test = new int[]{0,1,0,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(vl);
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(vl);
        ContainsLoaderVisitor vlt = new ContainsLoaderVisitor(storage.getSpaceShip(), ShipmentType.RED);
        assertThrows(ContainerNotSpecialException.class,()->storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(vlt));
        test = new int[]{0,3,0,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        vl = new ContainsLoaderVisitor(storage.getSpaceShip(), ShipmentType.GREEN);
        storage.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(vl);
        test = new int[]{0,3,1,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
        storage.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 2));
        test = new int[]{0,1,0,0,0};
        for(int i = 0; i < 5; i++){
            assertTrue(test[i]==storage.getSpaceShip().getContains()[i]);
        }
    }

}
