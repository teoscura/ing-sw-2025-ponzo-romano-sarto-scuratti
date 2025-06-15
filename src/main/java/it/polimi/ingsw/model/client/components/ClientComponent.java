package it.polimi.ingsw.model.client.components;

import java.io.Serializable;

/**
 * Interface representing a client side {@link it.polimi.ingsw.model.components.BaseComponent} , that can display its info.
 */
public interface ClientComponent extends Serializable {
	/**
	 * Display the component to a {@link ClientComponentVisitor}.
	 * 
	 * @param visitor {@link ClientComponentVisitor} Visitor displaying the component.
	 */
	void showComponent(ClientComponentVisitor visitor);
}
