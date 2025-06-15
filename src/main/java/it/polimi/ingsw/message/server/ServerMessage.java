package it.polimi.ingsw.message.server;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.LobbyController;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.state.GameState;

/**
 * Message to be received on the server.
 */
public abstract class ServerMessage implements Message {

	protected transient ClientDescriptor descriptor = null;

	public ClientDescriptor getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Set the {@link it.polimi.ingsw.controller.server.ClientDescriptor} of the message to the one provided.
	 * 
	 * @param client {@link it.polimi.ingsw.controller.server.ClientDescriptor} Client to be set.
	 */
	public void setDescriptor(ClientDescriptor client) {
		this.descriptor = client;
	}

	/**
	 * Executes the associated logic on the {@link MainServerController server} instance provided.
	 *  
	 * @param server {@link MainServerController} Server handling the message.
	 * @throws ForbiddenCallException If the command executed was not part of the currently allowed actions.
	 */
	public abstract void receive(MainServerController server) throws ForbiddenCallException;

	/**
	 * Executes the associated logic on the {@link it.polimi.ingsw.controller.server.LobbyController lobby} instance provided, can be omitted.
	 *  
	 * @param lobby {@link it.polimi.ingsw.controller.server.LobbyController} Lobby handling the message.
	 * @throws ForbiddenCallException If the command executed was not part of the currently allowed actions.
	 */
	public void receive(LobbyController lobby) throws ForbiddenCallException {
	}

	/**
	 * Executes the associated logic on the {@link it.polimi.ingsw.model.ModelInstance model} instance provided, can be omitted.
	 *  
	 * @param instance {@link it.polimi.ingsw.model.ModelInstance} Model handling the message.
	 * @throws ForbiddenCallException If the command executed was not part of the currently allowed actions.
	 */
	public void receive(ModelInstance instance) throws ForbiddenCallException {
	}

	/**
	 * Executes the associated logic on the {{@link it.polimi.ingsw.model.state.GameState} instance provided, can be omitted.
	 *  
	 * @param state {{@link it.polimi.ingsw.model.state.GameState} Game state handling the message.
	 * @throws ForbiddenCallException If the command executed was not part of the currently allowed actions.
	 */
	public void receive(GameState state) throws ForbiddenCallException {
	}

	/**
	 * Executes the associated logic on the {@link CardState} instance provided, can be omitted.
	 *  
	 * @param state {@link CardState} Card state handling the message.
	 * @throws ForbiddenCallException If the command executed was not part of the currently allowed actions.
	 */
	public void receive(CardState state) throws ForbiddenCallException {
	}


}
