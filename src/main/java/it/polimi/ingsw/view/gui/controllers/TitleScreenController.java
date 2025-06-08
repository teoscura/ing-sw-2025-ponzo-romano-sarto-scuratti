package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class TitleScreenController {

	private final TitleScreenState state;
	private final GUIView view;

	@FXML
	private ImageView logo;

	@FXML
	private TextField name_field;

	public TitleScreenController(TitleScreenState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	public void confirmName(ActionEvent event) {
		String name = name_field.getText();
		if (!name.isEmpty()) {
			state.setUsername(name);
			return;
		}
		view.showTextMessage("Name cannot be empty");
	}

	@FXML
	protected void initialize() {
		name_field.setOnAction(this::confirmName);
	}
}
