package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.ClientControllerState;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.TitleScreenTreeFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GuiMain extends Application implements ClientView {

    private StackPane root;
    private ClientState client_state;
    private ClientControllerState state;
    private ClientController c;

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Galaxy Trucker");
        root = new StackPane();
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.c = new ClientController(this);
    }

    @Override
    public void show(TitleScreenState state) {
        this.root.getChildren().clear();
        this.root.getChildren().add(TitleScreenTreeFactory.createTitleScreen(root, state));
    }

    @Override
    public void show(ConnectingState state) {
    }

    @Override
    public void show(ClientLobbySelectState state) {
        this.root.getChildren().clear();

    }

    @Override
    public void show(ClientSetupState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void show(ClientConstructionState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void show(ClientVerifyState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void show(ClientVoyageState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void show(ClientEndgameState state) {
        this.root.getChildren().clear();
    }

    @Override
    public void showTextMessage(String message) {
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
