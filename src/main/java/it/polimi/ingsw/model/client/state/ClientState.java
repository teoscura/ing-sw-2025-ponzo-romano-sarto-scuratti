package it.polimi.ingsw.model.client.state;

import it.polimi.ingsw.view.ClientView;

import java.io.Serializable;

public interface ClientState extends Serializable {
	void sendToView(ClientView view);
}
