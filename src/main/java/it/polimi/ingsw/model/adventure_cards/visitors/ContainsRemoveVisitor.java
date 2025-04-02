package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.components.AlienLifeSupportComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.CannonComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.EngineComponent;
import it.polimi.ingsw.model.components.ShieldComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.StructuralComponent;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class ContainsRemoveVisitor implements iVisitor {

    @Override
    public void visit(CabinComponent c) {
        return;
    }

    @Override
    public void visit(EngineComponent c) {
        return;
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        return;
    }

    @Override
    public void visit(CannonComponent c) {
        return;
    }

    @Override
    public void visit(StorageComponent c) {
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) break;
            if(c.howMany(t)>0){
                c.takeOut(t);
                return;
            }
        }
    }

    @Override
    public void visit(BatteryComponent c) {
        if(c.getContains()>0){
            c.takeOne();
            return;
        }
        throw new ContainerEmptyException();
    }

    @Override
    public void visit(ShieldComponent c) {
        return;
    }

    @Override
    public void visit(EmptyComponent c) {
        return;
    }

    @Override
    public void visit(StructuralComponent c) {
        return;
    }

    @Override
    public void visit(StartingCabinComponent c) {
        return;
    }
    
}
