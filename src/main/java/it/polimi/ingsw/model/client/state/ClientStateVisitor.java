package it.polimi.ingsw.model.client.state;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link ClientState}.
 */
public interface ClientStateVisitor {
	/**
	 * Displays info regarding a {@link ClientLobbySelectState}.
	 * @param state {@link ClientLobbySelectState} State to display.
	 */
	void show(ClientLobbySelectState state);
	/**
	 * Displays info regarding a {@link ClientSetupState}.
	 * @param state {@link ClientSetupState} State to display.
	 */
	void show(ClientSetupState state);
	/**
	 * Displays info regarding a {@link ClientWaitingRoomState}.
	 * @param state {@link ClientWaitingRoomState} State to display.
	 */
	void show(ClientWaitingRoomState state);
	/**
	 * Displays info regarding a {@link ClientConstructionState}.
	 * @param state {@link ClientConstructionState} State to display.
	 */
	void show(ClientConstructionState state);
	/**
	 * Displays info regarding a {@link ClientVerifyState}.
	 * @param state {@link ClientVerifyState} State to display.
	 */
	void show(ClientVerifyState state);
	/**
	 * Displays info regarding a {@link ClientVoyageState}.
	 * @param state {@link ClientVoyageState} State to display.
	 */
	void show(ClientVoyageState state);
	/**
	 * Displays info regarding a {@link ClientEndgameState}.
	 * @param state {@link ClientEndgameState} State to display.
	 */
	void show(ClientEndgameState state);
}
