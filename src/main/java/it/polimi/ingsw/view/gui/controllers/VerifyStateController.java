package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;

public class VerifyStateController {

	private ClientVerifyState state;
	private final GUIView view;

	@FXML private AnchorPane pane;
	@FXML private GridPane ship_grid;
	@FXML private Button player_button_1;
	@FXML private Button player_button_2;
	@FXML private Button player_button_3;
	@FXML private ImageView human_image;
	@FXML private ImageView brown_alien_image;
	@FXML private ImageView purple_alien_image;
	@FXML private ImageView drill_image;
	@FXML private ImageView select_blob_image;
	@FXML private TextFlow player_stats_flow;


	public VerifyStateController(ClientVerifyState state, GUIView view) {
		this.state = state;
		this.view = view;
		this.update(state);
	}

	@FXML
	private void initialize() {

	}

	public void update(ClientState state) {
		this.state = (ClientVerifyState) state;
	}
}
