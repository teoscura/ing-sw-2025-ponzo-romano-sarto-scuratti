package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ClientControllerState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.ClientView;

/**
 * Represents the client side controller in charge of managing all logic regarding connection and view interfacing.
 */
public class ClientController {

	private final ClientView view;
	private ClientControllerState state;
	private boolean closed = false;

	/**
	 * Constructs a {@link ClientController} object tied to a {@link it.polimi.ingsw.view.ClientView}.
	 * @param view {@link it.polimi.ingsw.view.ClientView} View tied to the controller.
	 */
	public ClientController(ClientView view) {
		this.view = view;
		this.setState(new TitleScreenState(this, view));
	}

	public ClientControllerState getState() {
		return this.state;
	}

	/**
	 * Set a new {@link ClientControllerState state} and run the initializing procedure.
	 * 
	 * @param next {@link ClientControllerState} State to be set and initialized.
	 */
	public void setState(ClientControllerState next) {
		this.state = next;
		this.state.init();
	}

	/**
	 * Close this controller and the program with it.
	 */
	public void close() {
		this.closed = true;
	}

	public boolean getClosed() {
		return this.closed;
	}

	/**
	 * Reset the controller back to the {@link TitleScreenState}.
	 */
	public void reset() {
		this.state.onClose();
		this.setState(new TitleScreenState(this, view));
	}

}
