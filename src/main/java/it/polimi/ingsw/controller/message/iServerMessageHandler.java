package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.message.server.*;

public interface iServerMessageHandler {

    public void onMessage(GainCreditsMessage m);
    public void onMessage(UserDisconnectMessage m);
    public void onMessage(ConstructionStartMessage m);
    public void onMessage(CorrectionStartMessage m);
    public void onMessage(ShipEvaluationMessage m);
    public void onMessage(SendPlayerStateMessage m);
    public void onMessage(TurnMessage m);
}
