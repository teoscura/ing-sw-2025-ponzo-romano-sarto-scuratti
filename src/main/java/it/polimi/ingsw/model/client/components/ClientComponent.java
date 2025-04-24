package it.polimi.ingsw.model.client.components;

import java.io.Serializable;

import it.polimi.ingsw.view.ClientView;

public interface ClientComponent extends Serializable {
	void showComponent(ClientView view);
}
