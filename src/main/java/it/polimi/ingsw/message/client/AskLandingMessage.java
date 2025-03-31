package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;
import it.polimi.ingsw.model.adventure_cards.utils.Planet;

public class AskLandingMessage extends ClientMessage{

    private final boolean[] avail_landings;
    private final int days;

    public AskLandingMessage(int days){
        this.avail_landings = new boolean[]{false};
        this.days = days;
    }

    public AskLandingMessage(Planet[] planets, int days){
        boolean[] tmp = new boolean[planets.length];
        for(int i=0;i<planets.length;i++){
            tmp[i] = planets[i].getVisited();
        }
        this.avail_landings = tmp;
        this.days = days;
    }

    @Override
    public void recieve(iClientController client) {
        client.askLanding(avail_landings, days);
    }

}
