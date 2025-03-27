package it.polimi.ingsw.net;

import it.polimi.ingsw.controller.exceptions.ObserverAlreadyAttachedException;
import it.polimi.ingsw.controller.exceptions.ObserverNotAttachedException;

public interface iConnectionHandler {
    public void notify_observers();
    public void attach(iConnectionObserver o) throws ObserverAlreadyAttachedException;
    public void detach(iConnectionObserver o) throws ObserverNotAttachedException;
}
