package it.polimi.ingsw.net;

public class RMIHandler extends ConnectionHandler {

    @Override
    public void notify_observers() {
        for(iConnectionObserver o : this.observers){
            o.observe(this);
        }
    }


    //XXX: any message is passed to the server controller, which handles the message.
    //RMIhandler simply serves as a listener for any client activity, the controller
    //acts on any logic to be associated with it.
}
