package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.TileImageVisitor;
import it.polimi.ingsw.view.gui.assets.DraggableTile;
import it.polimi.ingsw.view.gui.assets.ShipAsset;
import it.polimi.ingsw.view.gui.assets.TileAsset;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ConstructionController {

	private final ClientConstructionState state;
	private final GUIView view;

	@FXML
	private ImageView planche_image;
	@FXML
	private ImageView card_pile;
	@FXML
	private ImageView ship_background; //done
	@FXML
	private GridPane ship_grid;
	@FXML
	private ListView<ImageView> discarded_list;

	ShipAsset player_ship;
	TileAsset current_tile;


	public ConstructionController(ClientConstructionState state, GUIView view) {
		this.state = state;
		this.view = view;
		player_ship = new ShipAsset(state.getPlayerList().stream().filter(player -> player.getColor().equals(view.getSelectedColor())).findFirst().orElse(null));
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

		ship_grid.setOnMouseDragReleased(event -> {
			ImageView image = (ImageView) event.getGestureSource();

			int rowIndex = (int) ((event.getSceneY() - ship_grid.getLayoutY()) / (ship_grid.getHeight() / ship_grid.getRowCount()));
			int colIndex = (int) ((event.getSceneX() - ship_grid.getLayoutX()) / (ship_grid.getWidth() / ship_grid.getColumnCount()));

			if (image.getParent() instanceof GridPane) {
				GridPane.setRowIndex(image, rowIndex);
				GridPane.setColumnIndex(image, colIndex);
			}
			else{
				ship_grid.add(image, colIndex, rowIndex);
			}
			image.setLayoutX(75*colIndex);
			image.setLayoutY(75*rowIndex);
			image.setMouseTransparent(false);
		});

		card_pile.setOnMouseClicked(event -> grabComponent());

		discarded_list.setOnMouseDragReleased(event -> {
			//component discarded message
			//discarded_list.
		});
	}

}
