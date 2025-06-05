package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
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
	private ImageView ship_background;
	@FXML
	private GridPane ship_grid;
	@FXML
	private ListView discarded_list;


	public ConstructionController(ClientConstructionState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	public void grabComponent() {
		//TakeComponentMessage -> Recieve component
		TileAsset tile = new TileAsset(new ClientBaseComponent(1, ComponentRotation.U000, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}));
		ImageView image = tile.getBase();
		image.setFitHeight(card_pile.getFitHeight());
		image.setFitWidth(card_pile.getFitWidth());
		image.setLayoutX(card_pile.getLayoutX());
		image.setLayoutY(card_pile.getLayoutY());
		AnchorPane.setBottomAnchor(image, AnchorPane.getBottomAnchor(card_pile));
		AnchorPane.setLeftAnchor(image, AnchorPane.getLeftAnchor(card_pile));


	}

	private void handleDrag(Node node) {

		node.setOnMouseDragged(takeComponentEvent -> {
			node.setLayoutX(takeComponentEvent.getSceneX());
			node.setLayoutY(takeComponentEvent.getSceneY());
		});

		node.setOnMouseReleased(takeComponentEvent -> {
			Object source = takeComponentEvent.getSource();

		});
	}

	@FXML
	public void inizialize() {
		if (state.getType() == GameModeType.TEST){
			ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1.jpg"));
		}
		else ship_background.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-1b.jpg"));
		card_pile.setOnMouseClicked(event -> grabComponent());
		//print PlayerTextFlows
	}

}
