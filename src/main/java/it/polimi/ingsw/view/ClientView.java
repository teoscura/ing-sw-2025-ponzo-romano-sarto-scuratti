package it.polimi.ingsw.view;

import it.polimi.ingsw.model.client.state.*;

public interface ClientView {
    public void show(ClientWaitingRoomState state);
    public void show(ClientConstructionState state);
    public void show(ClientVerifyState state);
    public void show(ClientVoyageState state);
    public void show(ClientEndgameState state);
}
