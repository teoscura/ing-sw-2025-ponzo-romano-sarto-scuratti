package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

public class FreeSpaceVisitor implements iVisitor {
    private boolean spaces_are_free = true;

    @Override
    public void visit(CabinComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(EngineComponent c){
        this.spaces_are_free = false;   
    }

    @Override
    public void visit(AlienLifeSupportComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(CannonComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(StorageComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(BatteryComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(ShieldComponent c){
        this.spaces_are_free = false;
    }

    @Override
    public void visit(EmptyComponent c){
        this.spaces_are_free = true;
    }

    //HACK che schifo questo, da rifare.
    public boolean getSpacesAreFree(){
        return this.spaces_are_free;
    }
}