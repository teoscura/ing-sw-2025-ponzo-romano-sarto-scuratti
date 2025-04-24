package it.polimi.ingsw.model.client.card;

import java.io.Serializable;

import it.polimi.ingsw.view.ClientView;

public interface ClientCardState extends Serializable {
	void showCardState(ClientView view);
}
