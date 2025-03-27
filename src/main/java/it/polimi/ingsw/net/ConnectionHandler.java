package it.polimi.ingsw.net;

import java.util.ArrayList;

import it.polimi.ingsw.controller.exceptions.ObserverAlreadyAttachedException;
import it.polimi.ingsw.controller.exceptions.ObserverNotAttachedException;

public abstract class ConnectionHandler implements iConnectionHandler {
    
    protected ArrayList<iConnectionObserver> observers;

    public ConnectionHandler(){
        this.observers = new ArrayList<iConnectionObserver>();
    }

    @Override
    public abstract void notify_observers();

    @Override
    public void attach(iConnectionObserver o) throws ObserverAlreadyAttachedException {
        if(this.observers.contains(o)) throw new ObserverAlreadyAttachedException();
        this.observers.add(o);   
    }

    @Override
    public void detach(iConnectionObserver o) throws ObserverNotAttachedException {
        if(this.observers.contains(o)) throw new ObserverNotAttachedException();
        this.observers.remove(o);  
    }

}
