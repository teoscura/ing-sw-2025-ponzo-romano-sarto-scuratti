package it.polimi.ingsw.model.adventure_cards.visitors;

import it.polimi.ingsw.model.adventure_cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.visitors.iVisitor;

public class LargeMeteorVisitor implements iVisitor{

    private ProjectileDirection d;
    private boolean found_cannon = false;

    public LargeMeteorVisitor(ProjectileDirection d){
        this.d = d;
    }

    public boolean getFoundCannon(){
        return this.found_cannon;
    }

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
        if(c.getCurrentPower()==0) return;
        if(c.getRotation().getShift()!=d.getOpposite().getShift()) return;
        this.found_cannon = true;
    }

    @Override
    public void visit(StorageComponent c) {
        return;
    }

    @Override
    public void visit(BatteryComponent c) {
        return;
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
