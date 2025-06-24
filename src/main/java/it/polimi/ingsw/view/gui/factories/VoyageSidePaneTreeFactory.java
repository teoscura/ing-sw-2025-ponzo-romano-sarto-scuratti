package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.SelectLandingMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.BatteryPiece;
import it.polimi.ingsw.view.gui.tiles.piece.CargoPiece;
import it.polimi.ingsw.view.gui.tiles.piece.SelectBlobPiece;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Factory that exposes a static method to create all Nodes needed to display a {@link it.polimi.ingsw.model.client.state.ClientVoyageState}.
 */
public class VoyageSidePaneTreeFactory implements ClientCardStateVisitor {

	private final GUIView view;
	private VBox cstatetree = null;

	public VoyageSidePaneTreeFactory(GUIView view) {
		this.view = view;
	}

	public Node createSidePane(ClientVoyageState state, PlayerColor color) {
		StackPane sp = new StackPane();
		VBox side_pane = new VBox(0);
		PlayerColor yourcolor = state.getPlayerList().stream().filter(s -> s.getUsername().equals(view.getUsername())).map(ClientVoyagePlayer::getColor).findFirst().orElse(PlayerColor.NONE);
		side_pane.setAlignment(Pos.CENTER);
		side_pane.setPrefHeight(10000);
		side_pane.setMaxWidth(333);
		side_pane.setId("#voyage-side-pane");
		sp.setMaxWidth(333);
		sp.setAlignment(Pos.CENTER);
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
		
		this.cstatetree = new VBox(10);
		this.cstatetree.setAlignment(Pos.CENTER);
		this.cstatetree.setMaxWidth(333);
		Label card = new Label("Card: " + (state.getType().getTurns() - state.getCardsLeft()) + "/" + state.getType().getTurns());
		card.setFont(new Font(18));
		card.setFont(new Font(18));
		side_pane.getChildren().add(card);
		state.getCardState().showCardState(this);
		cstatetree.setId("voyage-card-state-pane");
		side_pane.getChildren().add(cstatetree);
		side_pane.getChildren().add(createColorSwitchTree(view, state, color));
		sp.getChildren().add(side_pane);
		side_pane.toFront();
		cstatetree.setDisable(color != state.getPlayerList().stream().filter(s -> s.getUsername().equals(view.getUsername())).map(p -> p.getColor()).findFirst().orElse(PlayerColor.NONE));
		return sp;
	}

	@Override//a
	public void show(ClientAwaitConfirmCardStateDecorator state) {
		HBox awaiting = new HBox(8);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.getStyleClass().add("voyage-awaiting-list");
		Label label = new Label("Awaiting:");
		label.setFont(new Font(18));
		awaiting.getChildren().add(label);
		for (var e : state.getAwaiting()) {
			awaiting.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/" + e.toString() + ".png"));
		}
		awaiting.setMaxWidth(333);
		this.cstatetree.getChildren().add(awaiting);
		Button confirm = new Button("Continue");
		confirm.getStyleClass().add("button-construction");
		confirm.setId("confirm-button");
		confirm.setOnAction(event -> {
			view.sendMessage(new SendContinueMessage());
		});
		this.cstatetree.getChildren().add(confirm);
	}

	@Override//a
	public void show(ClientBaseCardState state) {
		var e = new ImageView("galaxy_trucker_imgs/cards/GT-card-" + state.getID() + ".jpg");
		e.getStyleClass().add("voyage-card-image");
		e.setPreserveRatio(true);
		e.setFitHeight(250);
		this.cstatetree.getChildren().add(e);
	}

	@Override//a
	public void show(ClientCargoPenaltyCardStateDecorator state) {
		HBox awaiting = new HBox(10);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.setMaxWidth(333);
		awaiting.getStyleClass().add("voyage-cargo-pen-label");
		Label message = new Label("Cargo penalty: ");
		message.setFont(new Font(18));
		ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
		awaiting.getChildren().addAll(message, piece);
		this.cstatetree.getChildren().add(awaiting);
		HBox required = new HBox(10);
		required.setAlignment(Pos.CENTER);
		required.setMaxWidth(333);
		required.getStyleClass().add("voyage-cargo-pen-required");
		for (int i = 0; i < state.getShipments().length; i++) {
			for (int j = 0; j < state.getShipments()[i]; j++) {
				var node = i == 0 ? new BatteryPiece(view, null) : new CargoPiece(view, null, ShipmentType.fromValue(i));
				required.getChildren().add(node);
			}
		}
		this.cstatetree.getChildren().add(required);
	}

	@Override//a
	public void show(ClientCargoRewardCardStateDecorator state) {
		HBox awaiting = new HBox(10);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.setMaxWidth(333);
		awaiting.getStyleClass().add("voyage-cargo-rew-label");
		Label message = new Label("Cargo Reward: ");
		message.setFont(new Font(18));
		ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
		awaiting.getChildren().addAll(message, piece);
		this.cstatetree.getChildren().add(awaiting);
		HBox required = new HBox(10);
		required.setAlignment(Pos.CENTER);
		required.setMaxWidth(333);
		required.getStyleClass().add("voyage-cargo-rew-available");
		int k = 0;
		for (int i : state.getShipments()) {
			for (int j = 0; j < i; j++) {
				required.getChildren().add(new CargoPiece(view, null, ShipmentType.fromValue(k + 1)));
			}
			k++;
		}
		this.cstatetree.getChildren().add(required);
		Button ignore = new Button("Continue");
		ignore.getStyleClass().add("button-construction");
		ignore.setOnAction(event -> {
			view.sendMessage(new SendContinueMessage());
		});
		this.cstatetree.getChildren().add(ignore);
	}

	@Override//a
	public void show(ClientCombatZoneIndexCardStateDecorator state) {
		Label index = new Label("Combat zone section: " + (state.getIndex() + 1));
		index.setFont(new Font(18));
		index.setId("combat-zone-index");
		this.cstatetree.getChildren().add(index);
	}

	@Override//a
	public void show(ClientCreditsRewardCardStateDecorator state) {
		HBox awaiting = new HBox(10);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.setMaxWidth(333);
		awaiting.getStyleClass().add("voyage-credits-rew-label");
		Label message = new Label("Credits Reward: ");
		message.setFont(new Font(18));
		ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
		awaiting.getChildren().addAll(message, piece);
		this.cstatetree.getChildren().add(awaiting);
		HBox selection = new HBox(20);
		selection.setAlignment(Pos.CENTER);
		selection.setMaxWidth(333);
		selection.setId("voyage-credits-rew-selection");
		Button refuse = new Button("Refuse reward");
		refuse.getStyleClass().add("button-construction");
		refuse.setOnAction(event -> {
			view.sendMessage(new TakeRewardMessage(false));
		});
		Button accept = new Button("Take reward");
		accept.getStyleClass().add("button-construction");
		accept.setOnAction(event -> {
			view.sendMessage(new TakeRewardMessage(true));
		});
		selection.getChildren().addAll(refuse, accept);
		this.cstatetree.getChildren().add(selection);
	}

	@Override//a
	public void show(ClientCrewPenaltyCardStateDecorator state) {
		HBox awaiting = new HBox(10);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.setMaxWidth(333);
		awaiting.getStyleClass().add("voyage-crew-pen-label");
		Label message = new Label("Crew penalty [" + state.getCrewLost() + " LEFT]: ");
		message.setFont(new Font(18));
		ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
		awaiting.getChildren().addAll(message, piece);
		this.cstatetree.getChildren().add(awaiting);
	}

	@Override//a
	public void show(ClientLandingCardStateDecorator state) {
		HBox awaiting = new HBox(10);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.setMaxWidth(333);
		awaiting.getStyleClass().add("voyage-landing-label");
		this.cstatetree.getChildren().add(awaiting);
		if (state.getCredits() == 0 && state.getCrewNeeded() == 0) {
			Label message = new Label("Awaiting: ");
			message.setFont(new Font(18));
			ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
			awaiting.getChildren().addAll(message, piece);
			for (var p : state.getAvailable()) {
				if (p.getVisited()) continue;
				HBox landing_contains = new HBox(10);
				landing_contains.setAlignment(Pos.CENTER);
				landing_contains.setMaxWidth(333);
				Button land = new Button("Land:");
				land.getStyleClass().add("button-construction");
				land.setOnAction(event -> {
					view.sendMessage(new SelectLandingMessage(p.getID()));
				});
				landing_contains.getChildren().add(land);
				int k = 0;
				for (int l : p.getContains()) {
					for (int j = 0; j < l; j++) {
						CargoPiece cargo = new CargoPiece(view, null, ShipmentType.fromValue(k + 1));
						if (p.getContains().length >= 4) {
							cargo.setPreserveRatio(true);
							cargo.setFitWidth(cargo.getFitWidth() * 0.8);
						}
						landing_contains.getChildren().add(cargo);
					}
					k++;
				}
				this.cstatetree.getChildren().add(landing_contains);
			}
		} else if (state.getCredits() == 0) {
			Label message = new Label("Awaiting: ");
			message.setFont(new Font(18));
			ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
			awaiting.getChildren().addAll(message, piece);
			int i = 0;
			for (var p : state.getAvailable()) {
				if (p.getVisited()) continue;
				HBox landing_contains = new HBox(10);
				landing_contains.setAlignment(Pos.CENTER);
				landing_contains.setMaxWidth(333);
				Button land = new Button("Land here:");
				land.getStyleClass().add("button-construction");
				int x = i;
				land.setOnAction(event -> {
					view.sendMessage(new SelectLandingMessage(x));
				});
				landing_contains.getChildren().add(land);
				int k = 0;
				for (int l : p.getContains()) {
					for (int j = 0; j < l; j++) {
						landing_contains.getChildren().add(new CargoPiece(view, null, ShipmentType.fromValue(k + 1)));
					}
					k++;
				}
				this.cstatetree.getChildren().add(landing_contains);
				i++;
			}
		} else {
			Label message = new Label("Awaiting: ");
			message.setFont(new Font(18));
			ImageView piece = new ImageView("galaxy_trucker_imgs/piece/" + state.getTurn().toString() + ".png");
			awaiting.getChildren().addAll(message, piece);
			Button ship_landing = new Button(state.getCredits() + " credits.");
			ship_landing.getStyleClass().add("button-construction");
			ship_landing.setOnAction(event -> {
				view.sendMessage(new SelectLandingMessage(0));
			});
			this.cstatetree.getChildren().add(ship_landing);
		}
		Button refuse = new Button("Don't land.");
		refuse.getStyleClass().add("button-construction");
		refuse.setOnAction(event -> {
			view.sendMessage(new SelectLandingMessage(-1));
		});
		this.cstatetree.getChildren().add(refuse);
	}

	@Override//a
	public void show(ClientMeteoriteCardStateDecorator state) {
		Label message = new Label(state.getProjectile().getDimension() + " meteor on index: " + normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()));
		message.setFont(new Font(18));
		message.setId("meteor-announce-label");
		this.cstatetree.getChildren().add(message);
		var img = new ImageView("galaxy_trucker_imgs/piece/meteor_" + (state.getProjectile().getDimension() == ProjectileDimension.BIG ? "b" : "s") + ".png");
		img.setRotate(90 * (state.getProjectile().getDirection().getShift() + 2));
		this.cstatetree.getChildren().add(img);
	}

	@Override//a
	public void show(ClientNewCenterCardStateDecorator state) {
		HBox awaiting = new HBox(8);
		awaiting.setAlignment(Pos.CENTER);
		awaiting.getStyleClass().add("voyage-awaiting-list");
		Label label = new Label("Awaiting:");
		label.setFont(new Font(18));
		awaiting.getChildren().add(label);
		for (var e : state.getAwaiting()) {
			awaiting.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/" + e.toString() + ".png"));
		}
		awaiting.setMaxWidth(333);
		this.cstatetree.getChildren().add(awaiting);
		SelectBlobPiece p = new SelectBlobPiece();
		this.cstatetree.getChildren().add(p);
	}

	@Override//a
	public void show(ClientProjectileCardStateDecorator state) {
		Label message = new Label(state.getProjectile().getDimension() + " shot on index: " + normalizeOffset(state.getProjectile().getDirection().getShift(), state.getProjectile().getOffset()));
		message.setFont(new Font(18));
		message.setId("shot-announce-label");
		this.cstatetree.getChildren().add(message);
		var img = new ImageView("galaxy_trucker_imgs/piece/shot_" + (state.getProjectile().getDimension() == ProjectileDimension.BIG ? "b" : "s") + ".png");
		img.setRotate(90 * (state.getProjectile().getDirection().getShift() + 2));
		this.cstatetree.getChildren().add(img);
	}

	@Override//a
	public void show(ClientEnemyCardStateDecorator state) {
	}

	private int normalizeOffset(int shift, int roll) {
		if (shift % 2 == 0) {
			if (roll < 4 || roll > 10) return -1;
			return roll - 4;
		}
		if (roll < 5 || roll > 9) return -1;
		return roll - 5;
	}

	public Node createColorSwitchTree(GUIView view, ClientVoyageState state, PlayerColor color) {
		HBox res = new HBox(20);
		Label lab = new Label("Jump to: ");
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
