package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class TitleScreenController{

	private TitleScreenState state;
	private GUIView view;

	@FXML
	private ImageView logo;

	@FXML
	private TextField name_field;

	public TitleScreenController(TitleScreenState state, GUIView view) {
		this.state = state;
		this.view = view;
		logo.setFitHeight();
		name_field.setOnAction(this::confirm_name);
	}

	public void confirm_name(ActionEvent event) {
		state.setUsername(name_field.getText());
		//view.show(state.getNext());
	}

}
