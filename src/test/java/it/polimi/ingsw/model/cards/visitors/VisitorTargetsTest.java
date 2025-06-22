package it.polimi.ingsw.model.cards.visitors;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.SpaceShip;

public class VisitorTargetsTest {

    public ArrayList<BaseComponent> list;
    public AlienLifeSupportComponent alsc;
    public BatteryComponent bc;
    public CannonComponent cc;
    public CabinComponent cbc;
    public StartingCabinComponent scbc;
    public EngineComponent enc;
    public StorageComponent stc;
    public StructuralComponent sc;
    public EmptyComponent epc;
    public ShieldComponent shc;
    public SpaceShip tmp;

    @BeforeEach
    public void setup(){
        ConnectorType[] conns = new ConnectorType[]{ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL,ConnectorType.UNIVERSAL};
        ComponentRotation rotation = ComponentRotation.U000;
        alsc = new AlienLifeSupportComponent(1, conns, rotation, AlienType.BROWN);
        bc = new BatteryComponent(1, conns, rotation, BatteryType.DOUBLE);
        cc = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, rotation, CannonType.DOUBLE);
        cbc = new CabinComponent(1, conns, rotation);
        scbc = new StartingCabinComponent(1, conns, rotation, PlayerColor.RED);
        enc = new EngineComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL}, rotation, EngineType.DOUBLE);
        stc = new StorageComponent(1, conns, rotation, StorageType.DOUBLENORMAL);
        sc = new StructuralComponent(1, conns, rotation);
        epc = new EmptyComponent();
        shc = new ShieldComponent(1, conns, rotation);
        list = new ArrayList<>();
        list.add(alsc);
        list.add(bc);
        list.add(cc);
        list.add(cbc);
        list.add(scbc);
        list.add(enc);
        list.add(stc);
        list.add(sc);
        list.add(epc);
        list.add(shc);
        Player p1 = new Player(GameModeType.TEST, "a", PlayerColor.RED);
        tmp = p1.getSpaceShip(); 
    }  

    @Test
    public void targets(){
        ContainerMoveValidationVisitor vcmv = new ContainerMoveValidationVisitor(ShipmentType.BLUE);
        for(var c : list){
            if(c instanceof StorageComponent) continue;
            assertThrows(IllegalTargetException.class, ()->c.check(vcmv));
        }
        ContainsLoaderVisitor vclv = new ContainsLoaderVisitor(tmp, ShipmentType.BLUE);
        for(var c : list){
            if(c instanceof StorageComponent) continue;
            assertThrows(IllegalTargetException.class, ()->c.check(vclv));
        }
        ContainsRemoveVisitor vcrv = new ContainsRemoveVisitor(tmp, ShipmentType.BLUE);
        for(var c : list){
            if(c instanceof BatteryComponent) continue;
            if(c instanceof StorageComponent) continue;
            assertThrows(IllegalTargetException.class, ()->c.check(vcrv));
        }
        CrewRemoveVisitor vcr = new CrewRemoveVisitor(tmp);
        for(var c : list){
            if(c instanceof CabinComponent) continue;
            if(c instanceof StartingCabinComponent) continue;
            assertThrows(IllegalTargetException.class, ()->c.check(vcr));
        }
        LargeMeteorVisitor vlm = new LargeMeteorVisitor(ProjectileDirection.U000);
        for(var c : list){
            c.check(vlm);
        }
    }

}
