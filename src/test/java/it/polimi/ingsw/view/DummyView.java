package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;

public class DummyView implements ClientView {

	@Override
	public void show(ClientLobbySelectState state) {
	}

	@Override
	public void show(ClientSetupState state) {
	}

	@Override
	public void show(ClientWaitingRoomState state) {
	}

	@Override
	public void show(ClientConstructionState state) {
	}

	@Override
	public void show(ClientVerifyState state) {
	}

	@Override
	public void show(ClientVoyageState state) {
	}

	@Override
	public void show(ClientEndgameState state) {
	}

	@Override
	public void showTextMessage(String message) {
	}

	@Override
	public void show(TitleScreenState state) {
	}

	@Override
	public void show(ConnectingState state) {
	}

	@Override
	public void connect(ConnectedState state) {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public Object getLock() {
		return new Object();
	}

	@Override
	public boolean inputAvailable() {
		return true;
	}

	@Override
	public void setInput(ServerMessage input) {
	}

	@Override
	public ServerMessage getInput() {
		return null;
	}

	@Override
	public void setClientState(ClientState state) {
	}

	@Override
	public ClientState getClientState() {
		return null;
	}

}
