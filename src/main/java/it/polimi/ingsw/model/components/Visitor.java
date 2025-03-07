package it.polimi.ingsw.model.components;

//FIXME cambia quando fil merga player.
//import it.polimi.ingsw.model.player.iSpaceShip;

public class Visitor implements iVisitor {
    
    @Override
    public void updateSpaceShip(iSpaceShip s){
        //TODO
    }

    @Override
    public void visit(CabinComponent c){
        //TODO
    }

    //FIXME removed alien cabin component to merge with base one.

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
    public void visit(ShieldComponent c){
        //TODO
    }

    @Override
    public void visit(EmptyComponent c){
        //TODO
    }
}
