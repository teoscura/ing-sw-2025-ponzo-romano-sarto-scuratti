package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.factories.ConnectionSetupTreeFactory;
import it.polimi.ingsw.view.gui.factories.LobbyStateTreeFactory;
import it.polimi.ingsw.view.gui.factories.SetupTreeFactory;
import it.polimi.ingsw.view.gui.factories.TitleScreenTreeFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUIView extends Application implements ClientView {

	private StackPane root;
	private ClientState client_state;
	private ConnectedState state;
	private ClientController c;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Galaxy Trucker");
		root = new StackPane();
		Scene scene = new Scene(root, 1600, 900);
		primaryStage.setScene(scene);
		primaryStage.show();
		this.c = new ClientController(this);
	}

	public void sendMessage(ServerMessage message) {
		state.sendMessage(message);
	}

	@Override
	public void show(TitleScreenState state) {
		this.root.getChildren().clear();
		var node = TitleScreenTreeFactory.createTitleScreen(state);
		this.root.getChildren().add(node);
		StackPane.setAlignment(node, Pos.CENTER);
	}

	@Override
	public void show(ConnectingState state) {
		this.root.getChildren().clear();
		var node = ConnectionSetupTreeFactory.createConnectionScreen(state);
		this.root.getChildren().add(node);
		StackPane.setAlignment(node, Pos.CENTER);
	}

	@Override
	public void show(ClientLobbySelectState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();
			var node = LobbyStateTreeFactory.createLobbyScreen(state, this);
			this.root.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientSetupState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();
			var node = SetupTreeFactory.createSetupScreen(state, this);
			this.root.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientWaitingRoomState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientEndgameState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void showTextMessage(String message) {

		StackPane.setAlignment(new Group(), Pos.TOP_RIGHT);
	}

	@Override
	public void setClientState(ClientState state) {
		this.client_state = state;
	}

	@Override
	public void connect(ConnectedState state) {
		this.state = state;
	}

	@Override
	public void disconnect() {
		this.state = null;
	}

}
