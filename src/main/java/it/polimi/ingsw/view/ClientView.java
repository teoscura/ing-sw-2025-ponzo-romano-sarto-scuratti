package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientStateVisitor;

/**
 * Interface representing a client side view.
 */
public interface ClientView extends ClientStateVisitor {

	// -------------------------------------------------------------
	// Model display methods.
	// -------------------------------------------------------------

	/**
	 * Shows a starting title screen display.
	 * 
	 * @param state {@link TitleScreenState} TitleScreenState being bound to the view.
	 */
	void show(TitleScreenState state);

	/**
	 * Shows a connection setup state.
	 * 
	 * @param state {@link ConnectingState} ConnectingState being bound to the view.
	 */
	void show(ConnectingState state);

	/**
	 * Displays a text message on the view.
	 * 
	 * @param message Message to display.
	 */
	void showTextMessage(String message);

	// -------------------------------------------------------------
	// Model and Controller synchronization.
	// -------------------------------------------------------------
	
	/**
	 * Sets the latest client side model state.
	 * 
	 * @param state {@link ClientState} State to be set.
	 */
	void setClientState(ClientState state);

	/**
	 * Enables the view to send messages to a remote game server.
	 * 
	 * @param state {@link ConnectedState} ConnectedState being bound to the view.
	 */
	void connect(ConnectedState state);
	
	/**
	 * Disconnects the view from any remote game server.
	 */
	void disconnect();

}
