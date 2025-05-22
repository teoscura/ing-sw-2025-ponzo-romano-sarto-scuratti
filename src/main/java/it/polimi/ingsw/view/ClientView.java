package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;

public interface ClientView extends ClientStateVisitor {
	
	//User info setup.
	void show(TitleScreenState state);

	void show(ConnectingState state);

	//State sync
	void setClientState(ClientState state);

	ClientState getClientState();

	//Client input
	void connect(ConnectedState state);

	void disconnect();

	void setInput(ServerMessage input);

	ServerMessage takeInput();

	//Misc and debug
	void showTextMessage(String message);
	
}
