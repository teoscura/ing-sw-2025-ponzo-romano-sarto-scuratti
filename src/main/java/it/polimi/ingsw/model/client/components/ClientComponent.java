package it.polimi.ingsw.model.client.components;

import java.io.Serializable;

public interface ClientComponent extends Serializable {
	void showComponent(ClientComponentVisitor visitor);
}
