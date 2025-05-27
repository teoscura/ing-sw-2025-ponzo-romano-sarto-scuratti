package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientStateVisitor;

public interface ClientView extends ClientStateVisitor {

	//User info & setup.
	void show(TitleScreenState state);

	void show(ConnectingState state);

	void showTextMessage(String message);
	
	//State sync
	void setClientState(ClientState state);

	//Client input
	void connect(ConnectedState state);

	void disconnect();

	ServerMessage takeInput();

}
