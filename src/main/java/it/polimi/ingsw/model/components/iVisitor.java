package it.polimi.ingsw.model.components;

interface iVisitor {
    
    abstract public void updateSpaceShip(iSpaceShip s);

    abstract public void visit(CabinComponent c);

    abstract public void visit(EngineComponent c);

    abstract public void visit(AlienLifeSupportComponent c);

    abstract public void visit(CannonComponent c);

    abstract public void visit(StorageComponent c);

    abstract public void visit(BatteryComponent c);

    abstract public void visit(ShieldComponent c);

    abstract public void visit(EmptyComponent c);
}

