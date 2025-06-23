package it.polimi.ingsw.model.client.components;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.player.ShipCoords;

public class ClientComponentDecoratorTest {
    
    @Test
    public void componentDecorators(){
        ClientBaseComponent base = new ClientBaseComponent(1, ComponentRotation.U090, new ConnectorType[]{ConnectorType.EMPTY,ConnectorType.UNIVERSAL,ConnectorType.DOUBLE_CONNECTOR, ConnectorType.SINGLE_CONNECTOR});
        ClientBatteryComponentDecorator d1 = new ClientBatteryComponentDecorator(base, 1);
        ClientBrokenVerifyComponentDecorator d2 = new ClientBrokenVerifyComponentDecorator(d1);
        ClientCabinComponentDecorator d3 = new ClientCabinComponentDecorator(d2, AlienType.HUMAN, 1, false);
        ClientCannonComponentDecorator d4 = new ClientCannonComponentDecorator(d3, ComponentRotation.U000);
        ClientEngineComponentDecorator d5 = new ClientEngineComponentDecorator(d4, ComponentRotation.U000);
        ClientLifeSupportComponentDecorator d6 = new ClientLifeSupportComponentDecorator(d5, AlienType.BROWN);
        ClientPoweredComponentDecorator d7 = new ClientPoweredComponentDecorator(d6, false);
        ClientShieldComponentDecorator d8 = new ClientShieldComponentDecorator(d7, ShieldType.NW);
        ClientShipmentsComponentDecorator d9 = new ClientShipmentsComponentDecorator(d8, StorageType.DOUBLENORMAL, new int[]{1,1,1,1});

        assertArrayEquals(d9.getShipments(), new int[]{1,1,1,1});
        assertEquals(d9.getType(), StorageType.DOUBLENORMAL);
        assertEquals(d9.getBase(), d8);
        assertEquals(d8.getType(), ShieldType.NW);
        assertEquals(d8.getBase(), d7);
        assertEquals(d7.getPowered(), false);
        assertEquals(d7.getBase(), d6);
        assertEquals(d6.getAlienType(), AlienType.BROWN);
        assertEquals(d6.getBase(), d5);
        assertEquals(d5.getRotation(), ComponentRotation.U000);
        assertEquals(d5.getBase(), d4);
        assertEquals(d4.getRotation(), ComponentRotation.U000);
        assertEquals(d4.getBase(), d3);
        assertEquals(d3.getStarting(), false);
        assertEquals(d3.getCrew(), 1);
        assertEquals(d3.getAlienType(), AlienType.HUMAN);
        assertEquals(d3.getBase(), d2);
        assertEquals(d2.getBase(), d1);
        assertEquals(d1.getBatteries(),1);
        assertEquals(d1.getBase(), base);
        assertEquals(base.getId(),1);
        assertEquals(base.getRotation(), ComponentRotation.U090);
        assertEquals(base.getConnector(ComponentRotation.U000), ConnectorType.SINGLE_CONNECTOR);
        assertEquals(base.getConnector(ComponentRotation.U090), ConnectorType.EMPTY);
        assertEquals(base.getConnector(ComponentRotation.U180), ConnectorType.UNIVERSAL);
        assertEquals(base.getConnector(ComponentRotation.U270), ConnectorType.DOUBLE_CONNECTOR);

        ClientEmptyComponent e = new ClientEmptyComponent();
        DummyComponentVisitor v = new DummyComponentVisitor();
        e.showComponent(v);
        d9.showComponent(v);
        assertEquals(11, v.visited());

        ClientComponent[][] arr = new ClientComponent[5][7];
        for(int i = 0; i<5; i++){
            for(int j=0;j<7;j++){
                arr[i][j] = new ClientEmptyComponent();
            }
        }
        ClientSpaceShip s = new ClientSpaceShip(GameModeType.TEST, arr, new boolean[]{true, false, false, true}, 1.0, 2, new int[]{0,1,1,1,1}, new int[]{3,4,4});
        assertEquals(s.getCannonPower(), 1.0);
        assertEquals(s.getEnginePower(), 2);
        assertArrayEquals(s.getContainers(), new int[]{0,1,1,1,1});
        assertArrayEquals(s.getCrew(), new int[]{3,4,4});
        assertArrayEquals(s.getShielded(), new boolean[]{true, false, false, true});
        assertEquals(s.getType(), GameModeType.TEST);
        assertInstanceOf(ClientEmptyComponent.class, s.getComponent(new ShipCoords(GameModeType.TEST, 0, 0)));

    }

}
