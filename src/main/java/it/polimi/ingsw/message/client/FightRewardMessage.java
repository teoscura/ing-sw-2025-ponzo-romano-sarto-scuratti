package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.iClientController;

public class FightRewardMessage extends ClientMessage {

    private final int credits;
    private final int days;

    public FightRewardMessage(int credits, int days){
        this.credits = credits;
        this.days = days;
    }

    @Override
    public void recieve(iClientController client) {
        client.offerReward(credits, days);
    }

}
