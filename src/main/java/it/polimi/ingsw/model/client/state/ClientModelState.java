package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.view.ClientView;

public interface ClientModelState {
    public void sendToView(ClientView view);
}
