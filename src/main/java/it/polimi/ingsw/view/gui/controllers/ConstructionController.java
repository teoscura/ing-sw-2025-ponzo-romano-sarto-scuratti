package it.polimi.ingsw.view.gui.controllers;

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
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ConstructionController {

	private ClientConstructionState state;
	private final GUIView view;
	
	@FXML private ImageView planche_image;
	@FXML private ImageView card_pile;
	@FXML private ImageView ship_background; //done
	@FXML private GridPane ship_grid;
	@FXML private ListView<ImageView> discarded_list;

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
		image.setFitHeight(card_pile.getFitHeight());
		image.setFitWidth(card_pile.getFitWidth());
		image.setLayoutX(card_pile.getLayoutX());
		image.setLayoutY(card_pile.getLayoutY());
		AnchorPane.setBottomAnchor(image, AnchorPane.getBottomAnchor(card_pile));
		AnchorPane.setLeftAnchor(image, AnchorPane.getLeftAnchor(card_pile));
	}

	@FXML
	public void inizialize() {
		if (state.getType() == GameModeType.TEST){
			ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1.jpg"));
		}
		else ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1b.jpg"));

		ship_grid = player_ship.getGrid();

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
