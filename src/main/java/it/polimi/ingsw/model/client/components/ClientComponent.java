package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.view.ClientView;

import java.io.Serializable;

public interface ClientComponent extends Serializable {
	void showComponent(ClientView view);
}
