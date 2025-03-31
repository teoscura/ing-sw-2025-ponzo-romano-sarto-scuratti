package it.polimi.ingsw.model.components.visitors;

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

public class EnergyVisitor implements iVisitor {
    
    //If on turns on, if off turns off.
    private boolean positive;
    private boolean found_battery;
    private boolean powerable;
    private boolean has_battery;

    public EnergyVisitor(boolean on){
        this.positive = on;
    }

    @Override
    public void visit(CabinComponent c){
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    @Override
    public void visit(EngineComponent c){
        this.powerable = true;
        this.found_battery = false;
        if(this.positive){
            c.turnOn();
            return;
        }
        c.turnOff();
    }

    @Override
    public void visit(AlienLifeSupportComponent c){
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    @Override
    public void visit(CannonComponent c){
        this.powerable = true;
        this.found_battery = false;
        if(this.positive){
            c.turnOn();
            return;
        }
        c.turnOff();
    }

    @Override
    public void visit(StorageComponent c){
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    @Override
    public void visit(BatteryComponent c){
        this.powerable = false;
        this.found_battery = true;
        if(c.getCapacity()>0) this.has_battery = true;
        if(positive && c.getCapacity()>0) c.takeOne();
    }

    @Override
    public void visit(ShieldComponent c){
        this.powerable = true;
        this.found_battery = false;
        if(this.positive){
            c.turnOn();
            return;
        }
        c.turnOff();
    }

    @Override
    public void visit(EmptyComponent c){
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    @Override
    public void visit(StructuralComponent c) {
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    @Override
    public void visit(StartingCabinComponent c) {
        this.powerable = false;
        this.found_battery = false;
        return;
    }

    public boolean getFoundBatteryComponent(){
        return this.found_battery;
    }

    public boolean getPowerable(){
        return this.powerable;
    }

    public boolean hasBattery(){
        return this.has_battery;
    }

    public void toggle(){
        if(this.positive) positive = false;
        else positive = true;
    }
    
}
