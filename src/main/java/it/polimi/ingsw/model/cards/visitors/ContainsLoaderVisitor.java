package it.polimi.ingsw.model.cards.visitors;

import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.ContainerFullException;
import it.polimi.ingsw.model.components.exceptions.ContainerNotSpecialException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class ContainsLoaderVisitor implements iVisitor {

    private final ShipmentType cargo;

    public ContainsLoaderVisitor(ShipmentType cargo){
        if(cargo.getValue()<=0) throw new IllegalArgumentException();
        this.cargo = cargo;
    }

    @Override
    public void visit(CabinComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(EngineComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(AlienLifeSupportComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(CannonComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StorageComponent c) {
        try{
            c.putIn(cargo);
        } catch (ContainerFullException e){
            throw new ContainerFullException();
        } catch (ContainerNotSpecialException e){
            throw new ContainerNotSpecialException();
        }
    }

    @Override
    public void visit(BatteryComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(ShieldComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(EmptyComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StructuralComponent c) {
        throw new IllegalTargetException();
    }

    @Override
    public void visit(StartingCabinComponent c) {
        throw new IllegalTargetException();
    }

}
