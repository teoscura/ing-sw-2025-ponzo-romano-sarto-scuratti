package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
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
		state.setUsername(name_field.getText());
	}

	@FXML
	protected void initialize() {
		name_field.setOnAction(this::confirmName);
	}

	/*public void setState(TitleScreenState state) {
		this.state = state;
	}

	public void setView(GUIView view) {
		this.view = view;
	}*/

}
