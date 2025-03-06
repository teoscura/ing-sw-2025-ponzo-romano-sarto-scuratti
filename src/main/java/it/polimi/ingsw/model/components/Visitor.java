package it.polimi.ingsw.model.components;

import it.polimi.ingsw.model.player.iSpaceShip;

public class Visitor implements iVisitor {
    
    @Override
    public void updateSpaceShip(iSpaceShip s){
        //TODO
    }

    @Override
    public void visit(CabinComponent c){
        //TODO
    }

    @Override
    public void visit(AlienCabinComponent c){
        //TODO
    }

    @Override
    public void visit(EngineComponent c){
        //TODO
    }

    @Override
    public void visit(AlienLifeSupportComponent c){
        //TODO
    }

    @Override
    public void visit(CannonComponent c){
        //TODO
    }

    @Override
    public void visit(StorageComponent c){
        //TODO
    }

    @Override
    public void visit(BatteryComponent c){
        //TODO
    }

    @Override
    public void visit(EmptyComponent c){
        //TODO
    }
}
