package it.polimi.ingsw.model.client.state;

import java.io.Serializable;

public interface ClientState extends Serializable {
	void sendToView(ClientStateVisitor visitor);
}
