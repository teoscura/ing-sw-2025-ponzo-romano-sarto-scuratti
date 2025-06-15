package it.polimi.ingsw.controller.client.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

/**
 * Abstract class representing a State of the {@link ClientController}.
 */
public abstract class ClientControllerState {

	protected final ClientController controller;
	protected final ClientView view;

	/**
	 * Construct the abstract base of a {@link ClientControllerState} derived object.
	 * 
	 * @param controller {@link ClientController} Controller to which this state is tied to.
	 * @param view {@link ClientView} View to which this state is tied to.
	 */
	public ClientControllerState(ClientController controller, ClientView view) {
		if (view == null || controller == null) throw new NullPointerException();
		this.controller = controller;
		this.view = view;
	}

	public abstract void init();

	public abstract ClientControllerState getNext();

	/**
	 * Handle any cleanup needed by the state.
	 */
	public void onClose() {
	}

	/**
	 * Transition to the corresponding next state.
	 */
	protected void transition() {
		this.controller.setState(this.getNext());
	}

	protected ClientView getView() {
		return this.view;
	}

}
