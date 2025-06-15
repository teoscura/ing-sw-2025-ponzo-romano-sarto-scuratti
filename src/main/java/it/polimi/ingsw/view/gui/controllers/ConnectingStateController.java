package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.Objects;

public class ConnectingStateController {

	private final ConnectingState state;
	private final GUIView view;
	private String ip, port, connection_type;

	@FXML
	private TextField ip_field;
	@FXML
	private TextField port_field;
	@FXML
	private ChoiceBox<String> connection_menu;

	public ConnectingStateController(ConnectingState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	protected void initialize() {
		connection_menu.getItems().addAll("TCP", "RMI");
	}

	@FXML
	protected void connect() {
		ip = ip_field.getText();
		port = port_field.getText();
		if (connection_menu.getValue() == null) {
			errorMessage("Select either TCP or RMI");
			return;
		}
		try {
			Integer.parseInt(port);
		} catch (NumberFormatException e) {
			errorMessage("The port is not an integer");
			return;
		}

		connection_type = connection_menu.getValue();
		if (Objects.equals(connection_type, "TCP")) {
			state.connect(ip, Integer.parseInt(port), ConnectionType.SOCKET);
		} else if (Objects.equals(connection_type, "RMI")) {
			state.connect(ip, Integer.parseInt(port), ConnectionType.RMI);
		}
	}

	@FXML
	protected void errorMessage(String error) {
		System.out.println(error);
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setHeaderText("Input not valid");
		errorAlert.setContentText(error);
		errorAlert.showAndWait();
	}
}
