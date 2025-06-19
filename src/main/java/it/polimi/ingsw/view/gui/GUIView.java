package it.polimi.ingsw.view.gui;



import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.factories.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUIView extends Application implements ClientView {

    private StackPane root;
	private StackPane gameroot;
	private StackPane bgroot;
	private StackPane notifroot;
	private int bg_type = 1;
    private ClientState client_state;
	private ClientState prev_client_state;
    private ConnectedState state;
	private PlayerColor view_color;
	private String username;

	private VBox notif_box;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Galaxy Trucker");
        root = new StackPane();
        Scene scene = new Scene(root, 1368, 768);
		scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});
        new ClientController(this);
		this.bgroot = new StackPane();
		this.gameroot = new StackPane();
		this.notifroot = new StackPane();
		this.root.getChildren().addAll(bgroot, gameroot, notifroot);
		this.notif_box = new VBox(10);
		notif_box.setMaxWidth(305);
		StackPane.setAlignment(notifroot, Pos.TOP_RIGHT);
		notifroot.setMaxWidth(305);
		notifroot.setMaxHeight(400);
		this.notifroot.getChildren().add(this.notif_box);
		this.notifroot.setMouseTransparent(true);
		StackPane.setAlignment(notif_box, Pos.TOP_RIGHT);
		StackPane.setMargin(notif_box, new Insets(10, 0, 0, 10));
	}

	public void sendMessage(ServerMessage message) {
		state.sendMessage(message);
	}

    @Override
    public void show(TitleScreenState state) {
		Platform.runLater(() -> {
			this.bg_type = 1;
			this.view_color = PlayerColor.NONE;
			this.bgAnimation(1);
			this.gameroot.getChildren().clear();
            var node = TitleScreenTreeFactory.createTitleScreen(state);
			this.gameroot.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
        });
    }

    @Override
    public void show(ConnectingState state) {
        Platform.runLater(() -> {
			this.bg_type = 1;
			this.view_color = PlayerColor.NONE;
           	if(this.bg_type != 1 ) this.bgAnimation(1);
			this.bg_type = 1;
			this.gameroot.getChildren().clear();
            var node = ConnectionSetupTreeFactory.createConnectionScreen(state);
            this.gameroot.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
        });
    }

	@Override
	public void show(ClientLobbySelectState state) {
		Platform.runLater(() -> {
			this.bg_type = 1;
			this.view_color = PlayerColor.NONE;
			if(this.bg_type != 1 ) this.bgAnimation(1);
			this.bg_type = 1;
			this.gameroot.getChildren().clear();
			var node = LobbyStateTreeFactory.createLobbyScreen(state, this);
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientSetupState state) {
		Platform.runLater(() -> {
			this.bg_type = 1;
			this.view_color = PlayerColor.NONE;
			if(this.bg_type != 1 ) this.bgAnimation(1);
			this.bg_type = 1;
			this.gameroot.getChildren().clear();
			var node = SetupTreeFactory.createSetupScreen(state, this);
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientWaitingRoomState state) {
		Platform.runLater(() -> {
			if(state.getType().getLevel()!=this.bg_type) this.bgAnimation(state.getType().getLevel());
			this.bg_type = state.getType().getLevel();
			if(this.view_color==PlayerColor.NONE) 
				this.view_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);

			this.gameroot.getChildren().clear();
			var node = WaitingTreeFactory.createWaitingScreen(state, this);
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void show(ClientConstructionState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()!=this.bg_type) this.bgAnimation(state.getType().getLevel());
			this.bg_type = state.getType().getLevel();
			if(this.view_color==PlayerColor.NONE) 
				this.view_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);

			
			var player = state.getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(state.getPlayerList().getFirst());
			var prev = (VBox) gameroot.getScene().lookup("#constr-pane-base");
			if(prev!=null){
				var prevp = ((ClientConstructionState)prev_client_state).getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(((ClientConstructionState)prev_client_state).getPlayerList().getFirst());				
				if(prevp.getCurrent()!=player.getCurrent()){
					int indx =	prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-tile-pane"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createMainConstructionTileTree(this, player, state.getTilesLeft()));
				}
				if(!prevp.getReserved().equals(player.getReserved())){
					int indx =	prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-reserved-pane"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createReservedConstructionTileTree(this, player));
				}
				if(!prevp.getShip().equals(player.getShip())){
					int indx =	gameroot.getChildren().indexOf(gameroot.getScene().lookup("#ship"));
					gameroot.getChildren().remove(indx);
					var node = PlacedShipTreeFactory.createPlacedShip(this, player.getUsername(), player.getShip(), 0, true, player.isDisconnected());
					gameroot.getChildren().add(indx, node);
					StackPane.setMargin(node, new Insets(0, 60, 0, 0));
					StackPane.setAlignment(node, Pos.CENTER_RIGHT);
				}
				if(!((ClientConstructionState)prev_client_state).getDiscardedTiles().equals(state.getDiscardedTiles())){
					int indx =	prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-discarded-list"));
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createDiscardedConstructionTileTree(this, state));
				}
				int indx = prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-leveltwo-addons"));
				if(indx!=-1){
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createLevelTwoAddons(this, state, gameroot));
				}
				indx = prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-color-switch"));
				if(indx!=-1){
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createColorSwitchTree(this, state, view_color));
				}
				indx = prev.getChildren().indexOf(gameroot.getScene().lookup("#constr-awaiting-list"));
				if(indx!=-1){
					prev.getChildren().remove(indx);
					prev.getChildren().add(indx, ConstructionSidePaneTreeFactory.createAwaitingList(state));
				}
				return;
			} else {
				this.gameroot.getChildren().clear();
				var x = ConstructionSidePaneTreeFactory.createSidePane(this, state, view_color, gameroot);
				this.gameroot.getChildren().add(x);
				var node = PlacedShipTreeFactory.createPlacedShip(this, player.getUsername(), player.getShip(), 0, true, player.isDisconnected());
				this.gameroot.getChildren().add(node);
				StackPane.setAlignment(x, Pos.CENTER_LEFT);
				StackPane.setMargin(x, new Insets(0, 0, 0, 50));
				StackPane.setMargin(node, new Insets(0, 60, 0, 0));
				StackPane.setAlignment(node, Pos.CENTER_RIGHT);
			}
		});
	}

	@Override
	public void show(ClientVerifyState state) {
		Platform.runLater(() -> {
			GameModeType t = state.getType();
            if(t.getLevel()!=this.bg_type) this.bgAnimation(t.getLevel());
			this.bg_type = t.getLevel();

			if(this.view_color==PlayerColor.NONE) 
				this.view_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);

			
			var player = state.getPlayerList().stream().filter(p->p.getColor()==this.view_color).findFirst().orElse(state.getPlayerList().getFirst());
			this.gameroot.getChildren().clear();
			var x = VerifySidePaneTreeFactory.createSidePane(this, state, view_color);
			this.gameroot.getChildren().add(x);
			var node = PlacedShipTreeFactory.createPlacedShip(this, player.getUsername(), player.getShip(), 0, player.startsLosing(), player.isDisconnected());
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setMargin(x, new Insets(0, 0, 0, 60));
			StackPane.setMargin(node, new Insets(0, 60, 0, 0));
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
		});
	}

	@Override
	public void show(ClientVoyageState state) {
		Platform.runLater(() -> {
            if(state.getType().getLevel()!=this.bg_type) this.bgAnimation(state.getType().getLevel());
			this.bg_type = state.getType().getLevel();
			if(this.view_color==PlayerColor.NONE) 
				this.view_color = state.getPlayerList().stream().filter(s -> s.getUsername().equals(username)).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE);
			
			this.gameroot.getChildren().clear();
			VoyageSidePaneTreeFactory v = new VoyageSidePaneTreeFactory(this);
			var x = v.createSidePane(state, view_color);
			this.gameroot.getChildren().add(x);
			var node = PlacedShipTreeFactory.voyageShipPlanche(this, state, view_color);
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(x, Pos.CENTER_LEFT);
			StackPane.setMargin(x, new Insets(0, 0, 0, 60));
			StackPane.setMargin(node, new Insets(0, 60, 0, 0));
			StackPane.setAlignment(node, Pos.CENTER_RIGHT);
		});
	}

	@Override
	public void show(ClientEndgameState state) {
		Platform.runLater(() -> {
			if(this.bg_type!=1) this.bgAnimation(1);
			this.bg_type = 1;
			this.view_color = PlayerColor.NONE;
			this.gameroot.getChildren().clear();
			var node = EndgameTreeFactory.createEnding(this, state);
			this.gameroot.getChildren().add(node);
			StackPane.setAlignment(node, Pos.CENTER);
		});
	}

	@Override
	public void showTextMessage(String message) {
        var notif = new GUINotification(message, 7);
		Platform.runLater(()->{
			this.notif_box.getChildren().addFirst(notif);
			FadeTransition anim = new FadeTransition(Duration.seconds(notif.seconds()), notif);
			anim.setToValue(0.0);
			anim.setFromValue(1.0);
			anim.setInterpolator(Interpolator.EASE_IN);
			anim.setOnFinished(event -> {
				this.notif_box.getChildren().remove(notif);
			});
			anim.play();
		});	
	}

	@Override
	public void setClientState(ClientState state) {
		this.prev_client_state = this.client_state;
		this.client_state = state;
	}

	@Override
	public void connect(ConnectedState state) {
		this.state = state;
		this.username = state.getUsername();
	}

	@Override
	public void disconnect() {
		this.state = null;
		this.username = null;
	}

	public void selectColor(PlayerColor c){
		if(c==null || c == PlayerColor.NONE) return;
		this.view_color = c;
		this.gameroot.getChildren().clear();
		this.client_state.sendToView(this);
	}

	private void bgAnimation(int i){
		this.bgroot.getChildren().clear();
		ImageView bg = new ImageView("title"+i+".png");
		TranslateTransition anim = new TranslateTransition(Duration.seconds(70), bg);
		anim.setInterpolator(Interpolator.LINEAR);
		anim.setFromX(0);
		anim.setToY(-2000);
		anim.setCycleCount(Animation.INDEFINITE);
		anim.play();
		this.bgroot.getChildren().addAll(bg);
	}


}
