package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.ReserveComponentMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.TakeComponentMessage;
import it.polimi.ingsw.message.server.ToggleHourglassMessage;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;

import java.time.Duration;
import java.time.Instant;

/**
 * Factory that exposes a static method to create all Nodes needed to display a {@link it.polimi.ingsw.model.client.state.ClientConstructionState}.
 */
public class ConstructionSidePaneTreeFactory {

	private static void toggleHourglass(GUIView view) {
		ToggleHourglassMessage message = new ToggleHourglassMessage();
		view.sendMessage(message);
	}

	public static void showCards(ClientConstructionState state, Node root) {
		VBox card_list = new VBox(0);
		int k = 0;
		for (int id : state.getConstructionCards()) {
			if (k % 5 == 0) card_list.getChildren().addLast(new HBox());
			ImageView t = new ImageView("galaxy_trucker_imgs/cards/GT-card-" + id + ".jpg");
			t.setPreserveRatio(true);
			t.setPreserveRatio(true);
			t.setFitHeight(250);
			((HBox) card_list.getChildren().getLast()).getChildren().add(t);
			k++;
		}
		card_list.setAlignment(Pos.CENTER);
		Popup popup = new Popup();
		popup.getContent().add(card_list);
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.show(root.getScene().getWindow());
	}

	public static void updateHourglassAnimation(ClientConstructionState state, Button hourglass) {
		Instant end = state.getLastToggle().plus(Duration.ofSeconds(90));
		AnimationTimer hourglass_timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				Duration time_left = Duration.between(Instant.now(), end);
				hourglass.setText(("[Left: " + state.getTogglesLeft()) + "/" + state.getTogglesTotal() + "] " + (time_left.toSeconds()) + "s left!");
				if (Instant.now().isAfter(end)) {
					stop();
					hourglass.setText(("[Left: " + state.getTogglesLeft()) + "/" + state.getTogglesTotal() + "] Toggle ⧗");
				}
			}
		};
		hourglass_timer.start();
	}


	static public Node createSidePane(GUIView view, ClientConstructionState state, PlayerColor color, Node root) {
		ClientConstructionPlayer you = state.getPlayerList().stream().filter(p -> p.getColor() == color).findAny().orElse(state.getPlayerList().getFirst());
		PlayerColor yourcolor = state.getPlayerList().stream().filter(s -> s.getUsername().equals(view.getUsername())).map(ClientConstructionPlayer::getColor).findFirst().orElse(PlayerColor.NONE);
		StackPane sp = new StackPane();
		sp.setMaxWidth(333);
		Color panelColor;
		// make the stackpane the same color as yourcolor
		switch (yourcolor) {
			case RED:
				panelColor = new Color(143 / 255f, 0.0, 0.0, 0.7);
				break;
			case BLUE:
				panelColor = new Color(0.0, 0.0, 143 / 255f, 0.7);
				break;
			case GREEN:
				panelColor = new Color(0.0, 143 / 255f, 0.0, 0.7);
				break;
			case YELLOW:
				panelColor = new Color(143 / 255f, 143 / 255f, 0.0, 0.7);
				break;
			default:
				panelColor = new Color(169 / 255f, 169 / 255f, 169 / 255f, 0.7);
				break;
		}
		var rect = new Rectangle(333, 10000, panelColor);
		rect.getStyleClass().add("ui-rectangle");
		sp.getChildren().add(rect);

		VBox res = new VBox(10);
		res.setId("constr-pane-base");
		res.getChildren().add(createMainConstructionTileTree(view, you, state.getTilesLeft()));
		res.getChildren().add(createReservedConstructionTileTree(view, you));
		res.getChildren().add(createDiscardedConstructionTileTree(view, state));
		if (state.getType().getLevel() > 1) res.getChildren().add((createLevelTwoAddons(view, state, root)));
		Button confirm = new Button("Finish building!");
		confirm.getStyleClass().add("button-construction");
		confirm.setId("constr-confirm-button");
		confirm.setOnMouseClicked(event -> {
			view.sendMessage(new SendContinueMessage());
		});
		res.getChildren().add(confirm);
		res.getChildren().add(createColorSwitchTree(view, state, color));
		res.setAlignment(Pos.CENTER);
		res.setPrefHeight(10000);
		res.setMaxWidth(333);

		Label awaiting_lab = new Label("Awaiting: ");
		res.getChildren().add(awaiting_lab);
		res.getChildren().add(createAwaitingList(state));

		sp.getChildren().add(res);
		return sp;
	}

	static public Node createAwaitingList(ClientConstructionState state) {
		var awaiting_list = state.getPlayerList().stream().filter(p -> !p.isFinished()).map(p -> p.getColor()).toList();
		HBox awaiting = new HBox(8);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.getStyleClass().add("verify-list");
		for (var e : awaiting_list) {
			awaiting.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/" + e.toString() + ".png"));
		}
		awaiting.setId("constr-awaiting-list");
		awaiting.setMaxWidth(333);
		return awaiting;
	}

	static public Node createMainConstructionTileTree(GUIView view, ClientConstructionPlayer p, int left) {
		VBox resv = new VBox();
		resv.setMaxHeight(120);
		resv.setMaxWidth(240);
		StackPane sp = new StackPane();
		sp.setMaxHeight(120);
		sp.setMaxWidth(240);
		Rectangle rect = new Rectangle(150, 150);
		rect.setFill(new Color(186 / 255f, 186 / 255f, 186 / 255f, 0.4));
		rect.getStyleClass().add("ui-rectangle");
		sp.getChildren().add(rect);
		if (p.getCurrent() == -1) {

			Button pickb = new Button("Take Component: [" + left + " LEFT]");
			pickb.getStyleClass().add("button-construction");
			pickb.setOnAction(event -> {
				view.sendMessage(new TakeComponentMessage());
			});
			pickb.setId("constr-pick-button");
			sp.setAlignment(Pos.CENTER);
			resv.setAlignment(Pos.CENTER);
			resv.getChildren().add(sp);
			resv.getChildren().add(pickb);
		} else {

			sp.getChildren().add(new ConstructionTile(view, p.getCurrent(), false, true, 0.9));
			Button res = new Button("Reserve Component");
			res.getStyleClass().add("button-construction");
			res.setOnAction(event -> {
				view.sendMessage(new ReserveComponentMessage());
			});
			res.setId("constr-reserve-button");

			sp.setAlignment(Pos.CENTER);
			resv.setAlignment(Pos.CENTER);
			resv.getChildren().add(sp);
			resv.getChildren().add(res);
		}
		resv.setId("constr-tile-pane");
		return resv;
	}

	static public Node createReservedConstructionTileTree(GUIView view, ClientConstructionPlayer p) {
		HBox res = new HBox(10);
		res.setAlignment(Pos.CENTER);
		for (int id : p.getReserved()) {
			res.getChildren().add(new ConstructionTile(view, id, false, false, 0.5));
		}
		res.setId("constr-reserved-pane");
		res.getStyleClass().add("ui-rectangle");
		return res;
	}

	static public Node createDiscardedConstructionTileTree(GUIView view, ClientConstructionState state) {
		ListView<ConstructionTile> res = new ListView<>();
		res.setMaxHeight(130);
		res.setOrientation(Orientation.HORIZONTAL);
		res.setId("constr-discarded-list");
		ObservableList<ConstructionTile> list = FXCollections.observableArrayList();
		for (int id : state.getDiscardedTiles()) {
			list.add(new ConstructionTile(view, id, true, false, 0.8));
		}
		res.getStyleClass().add("discarded-list-view");
		res.setItems(list);
		return res;
	}

	static public Node createLevelTwoAddons(GUIView view, ClientConstructionState state, Node root) {
		HBox res = new HBox(10);
		res.setAlignment(Pos.CENTER);
		res.setId("constr-leveltwo-addons");
		Button cards = new Button("Peek cards");
		cards.getStyleClass().add("button-construction");
		cards.setOnAction(e -> showCards(state, root));
		cards.setId("constr-peek-cards");
		Button toggle = new Button("Toggle ⧗");
		toggle.getStyleClass().add("button-construction");
		toggle.setOnAction(e -> toggleHourglass(view));
		toggle.setId("constr-toggle-hourglass");
		res.getChildren().addAll(cards, toggle);
		updateHourglassAnimation(state, toggle); //will be moved once update is selective
		return res;
	}

	static public Node createColorSwitchTree(GUIView view, ClientConstructionState state, PlayerColor color) {
		HBox res = new HBox(20);
		Label lab = new Label("View: ");
		lab.setFont(new Font(18));
		res.getChildren().add(lab);
		res.setId("constr-color-switch");
		for (var p : state.getPlayerList()) {
			if (p.getColor() == color) continue;
			ImageView v = new ImageView("galaxy_trucker_imgs/piece/" + p.getColor() + ".png");
			v.setOnMouseClicked(event -> {
				view.selectColor(p.getColor());
			});
			res.getChildren().add(v);
		}
		res.setAlignment(Pos.CENTER);
		return res;
	}


}
