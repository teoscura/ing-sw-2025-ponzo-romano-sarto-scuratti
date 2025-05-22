package it.polimi.ingsw.model.client.state;

public interface ClientStateVisitor {
	void show(ClientLobbySelectState state);

	void show(ClientSetupState state);

	void show(ClientWaitingRoomState state);

	void show(ClientConstructionState state);

	void show(ClientVerifyState state);

	void show(ClientVoyageState state);

	void show(ClientEndgameState state);
}
