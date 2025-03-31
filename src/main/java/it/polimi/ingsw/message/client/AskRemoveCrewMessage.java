package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class AskRemoveCrewMessage extends ClientMessage{

    private int crew_amount;

    public AskRemoveCrewMessage(int crew_amount){
        this.crew_amount = crew_amount;
    }

    @Override
    public void recieve(iClientController client) {
        client.askRemoveCrew(crew_amount);
    }
    
}
