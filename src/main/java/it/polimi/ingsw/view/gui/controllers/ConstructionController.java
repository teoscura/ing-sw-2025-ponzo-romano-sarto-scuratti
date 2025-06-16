package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.server.ToggleHourglassMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.assets.DraggableTile;
import it.polimi.ingsw.view.gui.assets.ShipAsset;
import it.polimi.ingsw.view.gui.assets.TileAsset;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.time.Duration;
import java.time.Instant;

public class ConstructionController {

	private ClientConstructionState state;
	private final GUIView view;


	@FXML private GridPane ship_grid;
	@FXML private ListView<ImageView> discarded_list;
	@FXML private Button player_button_1;
	@FXML private Button player_button_2;
	@FXML private Button player_button_3;
	@FXML private ImageView discarded_1;
	@FXML private ImageView discarded_2;
	@FXML private ImageView component_pile;
	@FXML private Button hourglass_button;
	@FXML private Label components_left_label;
	@FXML private ImageView card_pile;
	ShipAsset player_ship;
	TileAsset current_tile;

	public ConstructionController(ClientConstructionState state, GUIView view) {
		this.state = state;
		this.view = view;
		this.update(state);
	}

	public void grabComponent() {
		//TakeComponentMessage -> Recieve component
		current_tile = new DraggableTile(new ClientBaseComponent(1, ComponentRotation.U000, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL})); //will get component from message
		ImageView image = current_tile.getBase();
		image.setFitHeight(component_pile.getFitHeight());
		image.setFitWidth(component_pile.getFitWidth());
		image.setLayoutX(component_pile.getLayoutX());
		image.setLayoutY(component_pile.getLayoutY());
		AnchorPane.setBottomAnchor(image, AnchorPane.getBottomAnchor(component_pile));
		AnchorPane.setLeftAnchor(image, AnchorPane.getLeftAnchor(component_pile));
	}

	public void toggle_hourglass(){
		hourglass_button.setDisable(true);
		ToggleHourglassMessage message = new ToggleHourglassMessage();
		AnimationTimer hourglass_timer = new AnimationTimer() {
			Instant end = Instant.now().plus(Duration.ofSeconds(90));
			@Override
			public void handle(long l) {
				Duration elapsed = Duration.between(Instant.now(), end);
				hourglass_button.setText((3-state.getTogglesLeft()) + "/3" + (elapsed.toSeconds()) + " seconds left");
				if (Instant.now().isAfter(end)) {
					if (state.getTogglesLeft()>0) hourglass_button.setDisable(false);
					stop();
					hourglass_button.setDisable(false);
					hourglass_button.setText((3-state.getTogglesLeft()) + "/3 Toggle hourglass");
				}
			}
		};
		hourglass_timer.start();
	}

	@FXML
	public void inizialize() {
		/*if (state.getType() == GameModeType.TEST){
			ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1.jpg"));
		}
		else ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1b.jpg"));*/

		ship_grid = player_ship.getGrid();

		hourglass_button.setOnAction(event->toggle_hourglass());

		card_pile.setOnMouseClicked(event -> grabComponent());
	}

	public void update(ClientState state){
		this.state = (ClientConstructionState) state;
		var p = this.state.getPlayerList().stream().filter(player -> player.getColor().equals(view.getSelectedColor())).findFirst().orElse(null);
		var new_ship = new ShipAsset(p);

		//PER OGNI COSA, CONTROLLARE SE UGUALE, SE NON LO E' RICOSTRUISCI.
		//FINE.
		//TODO construire clientcomponent e la construction tile. this.current_tile = p.getCurrent();

	}

}
