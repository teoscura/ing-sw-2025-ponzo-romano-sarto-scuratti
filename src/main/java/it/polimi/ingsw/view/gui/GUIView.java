package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.player.PlayerColor;
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
	private ClientState cstate;
	private GUIController controller;
	private PlayerColor selected_color;
	private String username;

	public GUIView(Stage stage) {
		this.stage = stage;
		this.state = null;
	}

	@Override
	public void show(TitleScreenState state) {
		TitleScreenController controller = new TitleScreenController(state, this);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/TitleScreenView.fxml"));
		loader.setControllerFactory(f -> controller);
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e) {
		}
		stage.setScene(scene);
		stage.setResizable(false);
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
		stage.setResizable(false);
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
				stage.setResizable(false);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setClientState(ClientState state) {
		this.cstate = state;
	}

	@Override
	public void connect(ConnectedState state) {
		username = state.getUsername();
		this.state = state;
	}

	@Override
	public void disconnect() {
		this.state = null;
	}

	public void sendMessage(ServerMessage message) {
		if (this.state == null) {
			this.showTextMessage("Attempted to send a message while state was null!");
			return;
		}
		this.state.sendMessage(message);
	}

	@Override
	public void showTextMessage(String message){
	}

	@Override
	public void show(ClientSetupState state) {
		Platform.runLater(() -> {
			if(state.getClass()==cstate.getClass()){
				controller.update(state);
				return;
			}
			Scene tmp = null;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/SetupStateView.fxml"));
			loader.setControllerFactory(f -> new SetupStateController(state, this));
			try {
				tmp = new Scene(loader.load());
			} catch (IOException e) {
				e.printStackTrace();
			}
			stage.setScene(tmp);
			stage.setResizable(false);
			stage.show();
		});

	}

	@Override
	public void show(ClientWaitingRoomState state) {

		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);

		System.out.println("method called");
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/WaitingRoomView.fxml"));
				loader.setControllerFactory(f -> new WaitingRoomController(state, this));
				Scene scene = new Scene(loader.load());
				stage.setScene(scene);
				stage.setResizable(false);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void show(ClientConstructionState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);

		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/ConstructionStateView.fxml"));
				
				loader.setControllerFactory(f -> new ConstructionController(state, this));
				
				Scene scene = new Scene(loader.load());
				stage.setScene(scene);
				stage.show();
			}
			catch (IOException e) {}

		});
	}

	@Override
	public void show(ClientVerifyState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
	}

	@Override
	public void show(ClientVoyageState state) {
		if (this.selected_color == PlayerColor.NONE)
			this.selected_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
	}

	@Override
	public void show(ClientEndgameState state) {

	}

	public PlayerColor getSelectedColor() {
		return selected_color;
	}
}
