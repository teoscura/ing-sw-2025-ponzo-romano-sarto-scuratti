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
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUIView extends Application implements ClientView {


    private StackPane root;
    private ClientState client_state;
    private ConnectedState state;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Galaxy Trucker");
        root = new StackPane();
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
        new ClientController(this);
    }

	public void sendMessage(ServerMessage message) {
		state.sendMessage(message);
	}


    @Override
    public void show(TitleScreenState state) {
        Platform.runLater(() -> {
            root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            this.root.getChildren().clear();
            var node = TitleScreenTreeFactory.createTitleScreen(state);
            this.root.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
        });
    }

    @Override
    public void show(ConnectingState state) {
        Platform.runLater(() -> {
            root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            this.root.getChildren().clear();
            var node = ConnectionSetupTreeFactory.createConnectionScreen(state);
            this.root.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
        });
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
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
            if(state.getPlayerList().getFirst().getShip().getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("galaxy_trucker_imgs/title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
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
