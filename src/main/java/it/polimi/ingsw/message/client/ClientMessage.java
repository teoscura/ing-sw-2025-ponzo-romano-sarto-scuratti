package it.polimi.ingsw.message.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.Message;

/**
 * Message to be received on the client.
 */
public abstract class ClientMessage implements Message {

	/**
	 * Executes the associated logic on the {@link ConnectedState state} instance provided.
	 *  
	 * @param client {@link ConnectedState} State receiving the message.
	 */
	public abstract void receive(ConnectedState client);
}
