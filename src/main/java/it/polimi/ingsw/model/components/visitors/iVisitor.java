package it.polimi.ingsw.model.components.visitors;

import it.polimi.ingsw.model.components.AlienLifeSupportComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.CannonComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.EngineComponent;
import it.polimi.ingsw.model.components.ShieldComponent;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.player.iSpaceShip;

public interface iVisitor {
    
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

