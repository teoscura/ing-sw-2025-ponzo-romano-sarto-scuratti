package it.polimi.ingsw.model.client.state;

public class DummyStateVisitor implements ClientStateVisitor {

    private int i = 0;

    public int visited(){
        return i;
    }

    @Override
    public void show(ClientLobbySelectState state) {
        i++;
    }

    @Override
    public void show(ClientSetupState state) {
        i++;
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        i++;
    }

    @Override
    public void show(ClientConstructionState state) {
        i++;
    }

    @Override
    public void show(ClientVerifyState state) {
        i++;
    }

    @Override
    public void show(ClientVoyageState state) {
        i++;
    }

    @Override
    public void show(ClientEndgameState state) {
        i++;
    }
    
}
