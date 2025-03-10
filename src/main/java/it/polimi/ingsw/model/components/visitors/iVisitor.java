package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.*;

public interface iVisitor {

    abstract public void visit(CabinComponent c);

    abstract public void visit(EngineComponent c);

    abstract public void visit(AlienLifeSupportComponent c);

    abstract public void visit(CannonComponent c);

    abstract public void visit(StorageComponent c);

    abstract public void visit(BatteryComponent c);

    abstract public void visit(ShieldComponent c);

    abstract public void visit(EmptyComponent c);
}

