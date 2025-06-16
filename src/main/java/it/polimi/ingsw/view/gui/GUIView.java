package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.factories.*;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
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
		if(message==null) return;
		System.out.println(message.getClass().getSimpleName());
		return;
		//state.sendMessage(message);
	}

    @Override
    public void show(TitleScreenState state) {
        Platform.runLater(() -> {
			Player player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
			BaseComponent c;
			ComponentFactory f2 = new ComponentFactory();
			c = f2.getComponent(14);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
			c = f2.getComponent(126);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
			c = f2.getComponent(132);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
			c = f2.getComponent(128);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
			c = f2.getComponent(118);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
			c = f2.getComponent(30);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
			((StorageComponent)c).putIn(ShipmentType.YELLOW);
			((StorageComponent)c).putIn(ShipmentType.BLUE);
			((StorageComponent)c).putIn(ShipmentType.GREEN);
			var node = PlacedShipTreeFactory.createPlacedShip(this, player2.getSpaceShip().getClientSpaceShip());
			root.getChildren().add(node);

			ConstructionTile ct1 = new ConstructionTile(this, 101, true, false);
			root.getChildren().addAll(ct1);
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
			StackPane.setAlignment(ct1, Pos.TOP_CENTER);
			// root.setBackground(new Background(new BackgroundImage(new Image("title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            // this.root.getChildren().clear();
            // var node = TitleScreenTreeFactory.createTitleScreen(state);
            // this.root.getChildren().add(node);
            // StackPane.setAlignment(node, Pos.CENTER);
        });
    }

    @Override
    public void show(ConnectingState state) {
        Platform.runLater(() -> {
            root.setBackground(new Background(new BackgroundImage(new Image("title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
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
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
            if(state.getPlayerList().getFirst().getShip().getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();

		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
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
