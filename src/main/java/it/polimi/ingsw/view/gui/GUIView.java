package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIView implements ClientView {

	private final Stage stage;

	public GUIView(Stage stage) {
		this.stage = stage;
	}

	/*public void setup() throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/TitleScreenView.fxml")));
		StartingMenuController controller = new StartingMenuController();
		controller.setStage(stage);
		stage.setScene(scene);
		stage.show();
	}*/


	@Override
	public void show(TitleScreenState state) {
		Scene scene = null;
		try {
			scene = new Scene(FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/TitleScreenView.fxml")));
		}
		catch (IOException e) {}

		TitleScreenController controller = new TitleScreenController(state, this);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void show(ConnectingState state) {

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

	}

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
}
