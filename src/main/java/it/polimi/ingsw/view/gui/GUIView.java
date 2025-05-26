package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.IOException;

public class GUIView implements ClientView {

	private final Stage stage;

	public GUIView(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void show(TitleScreenState state) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/TitleScreenView.fxml"));
		loader.setControllerFactory(f -> new TitleScreenController(state, this));
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
		}
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void show(ConnectingState state) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/ConnectingStateView.fxml"));
		loader.setControllerFactory(f -> new ConnectingStateController(state, this));
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
		}
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void show(ClientLobbySelectState state) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/LobbyStateView.fxml"));
		loader.setControllerFactory(f -> new ClientLobbyStateController(state, this));
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {}
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public ClientState getClientState() {
		return null;
	}

	@Override
	public void setClientState(ClientState state) {

	}

	@Override
	public void connect(ConnectedState state) {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public void setInput(ServerMessage input) {

	}

	@Override
	public ServerMessage takeInput() {
		return null;
	}

	@Override
	public void showTextMessage(String message) {
		Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
		errorAlert.setHeaderText("Info");
		errorAlert.setContentText(message);
		errorAlert.showAndWait();


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
}
