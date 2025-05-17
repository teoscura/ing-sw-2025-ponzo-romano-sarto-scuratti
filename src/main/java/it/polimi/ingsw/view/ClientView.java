package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.state.*;

public interface ClientView {
	//Game states
	void show(TitleScreenState state);

	void show(ConnectingState state);

	void show(ClientLobbySelectState state);

	void show(ClientSetupState state);

	void show(ClientWaitingRoomState state);

	void show(ClientConstructionState state);

	void show(ClientVerifyState state);

	void show(ClientVoyageState state);

	void show(ClientEndgameState state);

	//Card states

	//Misc and debug
	void showTextMessage(String message);

}
