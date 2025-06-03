package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class GUIView implements ClientView {

	private final Stage stage;
	private ConnectedState state;

	public GUIView(Stage stage) {
		this.stage = stage;
		this.state = null;
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
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/LobbyStateView.fxml"));
				loader.setControllerFactory(f -> new ClientLobbyStateController(state, this));
				Scene scene;
				scene = new Scene(loader.load());
				scene.getStylesheets().add(getClass().getResource("/it/polimi/ingsw/style.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setClientState(ClientState state) {

	}

	@Override
	public void connect(ConnectedState state) {
		this.state = state;
	}

	@Override
	public void disconnect() {
		this.state = null;
	}

	public void sendMessage(ServerMessage message){
		if(this.state == null){
			this.showTextMessage("Attempted to send a message while state was null!");
			return;
		}
		this.state.sendMessage(message);
	}

	@Override
	public void showTextMessage(String message) {
//		Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
//		errorAlert.setHeaderText("Info");
//		errorAlert.setContentText(message);
//		errorAlert.showAndWait();
		
	}

	@Override
	public void show(ClientSetupState state) {
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/SetupStateView.fxml"));
				loader.setControllerFactory(f -> new SetupStateController(state, this));
				Scene scene = new Scene(loader.load());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void show(ClientWaitingRoomState state) {
		System.out.println("method called");
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/WaitingRoomView.fxml"));
				loader.setControllerFactory(f -> new WaitingRoomController(state, this));
				Scene scene = new Scene(loader.load());
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
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
