package it.polimi.ingsw.view.gui;


import java.util.ArrayList;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
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
			root.setBackground(new Background(new BackgroundImage(new Image("title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));

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
			c = f2.getComponent(55);
			c.rotate(ComponentRotation.U000);
			player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 4));
			

			ClientVerifyState s = new ClientVerifyState(
				new ArrayList<>(){{
					add(new ClientVerifyPlayer("Gigio1", PlayerColor.RED, player2.getSpaceShip().getClientSpaceShip().getVerifyShip(player2.getSpaceShip().bulkVerify()), true, false, false, false, 1));
					add(new ClientVerifyPlayer("Gigio2", PlayerColor.BLUE, player2.getSpaceShip().getClientSpaceShip().getVerifyShip(player2.getSpaceShip().bulkVerify()), true, true, false, false, 2));
					add(new ClientVerifyPlayer("Gigio3", PlayerColor.GREEN, player2.getSpaceShip().getClientSpaceShip().getVerifyShip(player2.getSpaceShip().bulkVerify()), true, false, false, false, 3));
				}});
	
			this.view_color = PlayerColor.RED;
			this.client_state = s;
			var x = VerifySidePaneTreeFactory.createSidePane(this, s, view_color);
			this.root.getChildren().add(x);
			var node = PlacedShipTreeFactory.createPlacedShip(this, player2.getSpaceShip().getClientSpaceShip());
			this.root.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
			StackPane.setMargin(x, new Insets(0, 0, 0, 60));
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
			//TODO update selective.
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
			var node = WaitingTreeFactory.createWaitingScreen(state, this);
			this.root.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
			//TODO update selective.
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
			var player = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).findFirst().orElse(state.getPlayerList().getFirst());

			var x = ConstructionSidePaneTreeFactory.createSidePane(this, state, view_color);
			this.root.getChildren().add(x);
			var node = PlacedShipTreeFactory.createPlacedShip(this, player.getShip());
			this.root.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setMargin(x, new Insets(0, 0, 0, 60));
			StackPane.setMargin(node, new Insets(0, 60, 0, 0));
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
			//TODO update selective.
            if(state.getPlayerList().getFirst().getShip().getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
			var player = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).findFirst().orElse(state.getPlayerList().getFirst());

			var x = VerifySidePaneTreeFactory.createSidePane(this, state, view_color);
			this.root.getChildren().add(x);
			var node = PlacedShipTreeFactory.createPlacedShip(this, player.getShip());
			this.root.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setMargin(x, new Insets(0, 0, 0, 60));
			StackPane.setMargin(node, new Insets(0, 60, 0, 0));
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
			//TODO update selective.
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
			var player = state.getPlayerList().stream().filter(p->p.getUsername().equals(this.state.getUsername())).findFirst().orElse(state.getPlayerList().getFirst());

			// var x = VoyageSidePaneTreeFactory.createSidePane(this, state, view_color);
			// this.root.getChildren().add(x);
			// var node = PlacedShipTreeFactory.createPlacedShip(this, player.getShip());
			// this.root.getChildren().add(node);
			// StackPane.setAlignment(x, Pos.CENTER_LEFT);
			// StackPane.setMargin(x, new Insets(0, 0, 0, 60));
			// StackPane.setMargin(node, new Insets(0, 60, 0, 0));
			// StackPane.setAlignment(node, Pos.CENTER_RIGHT);
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

	public void selectColor(PlayerColor c){
		if(c==null || c == PlayerColor.NONE) return;
		this.view_color = c;
		this.client_state.sendToView(this);
	}


}
