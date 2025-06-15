package it.polimi.ingsw.model.client.state;

import java.io.Serializable;

/**
 * Interface representing a client side {{@link it.polimi.ingsw.model.state.GameState}, that can display its info.
 */
public interface ClientState extends Serializable {

	/**
	 * Display the game state to a {@link ClientStateVisitor}.
	 * 
	 * @param visitor {@link ClientStateVisitor} Visitor displaying the state.
	 */
	void sendToView(ClientStateVisitor visitor);
}
