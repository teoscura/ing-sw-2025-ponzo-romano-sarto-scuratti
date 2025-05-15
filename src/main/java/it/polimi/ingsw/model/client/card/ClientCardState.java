package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.view.ClientView;

import java.io.Serializable;

public interface ClientCardState extends Serializable {
	void showCardState(ClientView view);
}
