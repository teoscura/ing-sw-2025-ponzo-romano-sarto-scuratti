package it.polimi.ingsw.view.gui;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.factories.*;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import it.polimi.ingsw.view.gui.utils.GameEntryNode;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIView extends Application implements ClientView {

    private StackPane root;
    private ClientState client_state;
	private ClientState prev_client_state;
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
		state.sendMessage(message);
	}

    @Override
    public void show(TitleScreenState state) {
		Platform.runLater(() -> {
			root.setBackground(new Background(new BackgroundImage(new Image("title1.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			this.root.getChildren().clear();
            var node = TitleScreenTreeFactory.createTitleScreen(state);
            this.root.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
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
			var node = WaitingTreeFactory.createWaitingScreen(state, this);
			this.root.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			var player = state.getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(state.getPlayerList().getFirst());

			var prev = (VBox) root.getScene().lookup("#constr-pane-base");
			if(prev!=null){
				var prevp = ((ClientConstructionState)prev_client_state).getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(((ClientConstructionState)prev_client_state).getPlayerList().getFirst());				
				if(prevp.getCurrent()!=player.getCurrent()){
					System.out.println("Replacing tile!");
					int indx =	prev.getChildren().indexOf(root.getScene().lookup("#constr-tile-pane"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createMainConstructionTileTree(this, player, state.getTilesLeft()));
				}
				if(!prevp.getReserved().equals(player.getReserved())){
					System.out.println("Replacing tiles!");
					int indx =	prev.getChildren().indexOf(root.getScene().lookup("#constr-tile-pane"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createReservedConstructionTileTree(this, player));
				}
				if(!prevp.getShip().equals(player.getShip())){
					System.out.println("Replacing Ship!");
					int indx =	root.getChildren().indexOf(root.getScene().lookup("#ship"));
					root.getChildren().remove(indx);
					var node = PlacedShipTreeFactory.createPlacedShip(this, player.getShip());
					root.getChildren().add(indx, node);
					StackPane.setMargin(node, new Insets(0, 60, 0, 0));
					StackPane.setAlignment(node, Pos.CENTER_RIGHT);
				}
				if(!((ClientConstructionState)prev_client_state).getDiscardedTiles().equals(state.getDiscardedTiles())){
					System.out.println("Replacing Discarded!");
					int indx =	prev.getChildren().indexOf(root.getScene().lookup("#constr-discarded-list"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createDiscardedConstructionTileTree(this, state));
				}
				System.out.println("Replacing addons!");
				int indx = prev.getChildren().indexOf(root.getScene().lookup("#constr-leveltwo-addons"));
				if(indx!=-1){
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createLevelTwoAddons(this, state, root));
				}
				System.out.println("Replacing Colors!");
				indx = prev.getChildren().indexOf(root.getScene().lookup("#constr-color-switch"));
				if(indx!=-1){
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createColorSwitchTree(this, state, view_color));
				}
				return;
			} else {
				this.root.getChildren().clear();
				var x = ConstructionSidePaneTreeFactory.createSidePane(this, state, view_color, root);
				this.root.getChildren().add(x);
				var node = PlacedShipTreeFactory.createPlacedShip(this, player.getShip());
				this.root.getChildren().add(node);
				StackPane.setAlignment(x, Pos.CENTER_LEFT);
				StackPane.setMargin(x, new Insets(0, 0, 0, 60));
				StackPane.setMargin(node, new Insets(0, 60, 0, 0));
				StackPane.setAlignment(node, Pos.CENTER_RIGHT);
			}
		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
            if(state.getPlayerList().getFirst().getShip().getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			var player = state.getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(state.getPlayerList().getFirst());

			var prev = root.getScene().lookup("#verify-pane-base");
			if(prev!=null){
				//Update selective.

				return;
			} else {

			}

			this.root.getChildren().clear();
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
            if(state.getType().getLevel()==2) root.setBackground(new Background(new BackgroundImage(new Image("title2.jpg"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
			var player = state.getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(state.getPlayerList().getFirst());

			var prev = root.getScene().lookup("#voyage-card-state-pane");
			if(prev!=null){
				//Update selective.

				return;
			} else {

			}

			this.root.getChildren().clear();
			VoyageSidePaneTreeFactory v = new VoyageSidePaneTreeFactory(this);
			var x = v.createSidePane(state, view_color);
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
	public void show(ClientEndgameState state) {
		Platform.runLater(() -> {
			this.root.getChildren().clear();

		});
	}

	@Override
	public void showTextMessage(String message) {
        //TODO notifiche.
	}

	@Override
	public void setClientState(ClientState state) {
		this.prev_client_state = this.client_state;
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
		this.root.getChildren().clear();
		this.client_state.sendToView(this);
	}


}
