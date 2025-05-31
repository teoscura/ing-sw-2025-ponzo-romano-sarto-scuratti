package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ConstructionController {

	private final ClientConstructionState state;

	@FXML
	private ImageView planche_image;

	@FXML
	private ImageView card_pile;

	public ConstructionController(ClientConstructionState state) {
		this.state = state;
	}

	public void paint() {
		if (state.getType() == GameModeType.TEST) {
			planche_image.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-3.png"));
		} else planche_image.setImage(new Image("/galaxy_trucker_imgs/cardboard/cardboard-5.png"));
	}

	public void grabCard() {

	}
}
