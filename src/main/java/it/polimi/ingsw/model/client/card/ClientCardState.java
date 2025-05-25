package it.polimi.ingsw.model.client.card;

import java.io.Serializable;

public interface ClientCardState extends Serializable {
	void showCardState(ClientCardStateVisitor visitor);
}
