package it.polimi.ingsw.model.client.state;

import java.io.Serializable;

import it.polimi.ingsw.view.ClientView;

public interface ClientState extends Serializable {
	void sendToView(ClientView view);
}
