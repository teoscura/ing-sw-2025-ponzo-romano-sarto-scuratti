package it.polimi.ingsw.view.gui;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GUIView extends Application implements ClientView {

    private StackPane root;
    private ClientState client_state;
    private ConnectedState state;
	private PlayerColor view_color;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Galaxy Trucker");
        root = new StackPane();
        Scene scene = new Scene(root, 1368, 768);
		scene.getStylesheets().add("styles.css");
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
			Player player2; BaseComponent c;
			ComponentFactory f2 = new ComponentFactory();
			player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
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
			ClientSpaceShip ship = player2.getSpaceShip().getClientSpaceShip();
			var discarded = new ArrayList<Integer>(){{add(1);add(3);add(14);add(99);add(100);}};
			var construction = new ArrayList<Integer>(){{add(1);add(3);add(14);}};
			var playerlist = new ArrayList<ClientConstructionPlayer>(){{
				add(new ClientConstructionPlayer("Gigio", PlayerColor.RED, ship, -1, new ArrayList<Integer>(){{add(3);}}, false, false));
				add(new ClientConstructionPlayer("Gigio2", PlayerColor.BLUE, ship, 120, new ArrayList<Integer>(){{add(3);}}, false, false));
				add(new ClientConstructionPlayer("Gigio3", PlayerColor.GREEN, ship, 120, new ArrayList<Integer>(){{add(3);}}, false, false));
			}};
			ClientConstructionState test = new ClientConstructionState(GameModeType.TEST, playerlist, construction, discarded, 123, 3, 2, Duration.ofSeconds(123), Instant.now());
			this.view_color = PlayerColor.RED;
			var x = ConstructionSidePaneTreeFactory.createSidePane(this, test, view_color);
			this.root.getChildren().add(x);
			var node = PlacedShipTreeFactory.createPlacedShip(this, ship);
			this.root.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
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
			this.view_color = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.RED);

		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
            if(state.getPlayerList().getFirst().getShip().getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
			this.view_color = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.RED);

		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
			this.view_color = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.RED);

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

	// Player player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
	// BaseComponent c;
	// ComponentFactory f2 = new ComponentFactory();
	// c = f2.getComponent(14);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
	// c = f2.getComponent(126);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
	// c = f2.getComponent(132);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
	// c = f2.getComponent(128);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
	// c = f2.getComponent(118);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
	// c = f2.getComponent(30);
	// c.rotate(ComponentRotation.U000);
	// player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
	// ((StorageComponent)c).putIn(ShipmentType.YELLOW);
	// ((StorageComponent)c).putIn(ShipmentType.BLUE);
	// ((StorageComponent)c).putIn(ShipmentType.GREEN);
	// var node = PlacedShipTreeFactory.createPlacedShip(this, player2.getSpaceShip().getClientSpaceShip());
	// root.getChildren().add(node);

}
