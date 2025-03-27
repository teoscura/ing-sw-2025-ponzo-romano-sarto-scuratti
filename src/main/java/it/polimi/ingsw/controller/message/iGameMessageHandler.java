package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.message.player.*;

public interface iGameMessageHandler {
    public void onMessage(PlayerComponentDiscardMessage m);
    public void onMessage(PlayerComponentPlaceMessage m);
    public void onMessage(PlayerComponentTakeMessage m);
    public void onMessage(PlayerContinueMessage m);
    public void onMessage(PlayerCrewSelectMessage m);
    public void onMessage(PlayerDisconnectMessage m);
    public void onMessage(PlayerGiveUpMessage m);
    public void onMessage(PlayerJoinMessage m);
    public void onMessage(PlayerRetrieveCargoMessage m);
    public void onMessage(PlayerSelectNewCabinMessage m);
    public void onMessage(PlayerTurnOnMessage m);
}
