package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

public class ConnectingStateController {

	private final ConnectingState state;
	private final GUIView view;
	private String ip, port;

	@FXML
	private TextField ip_field;
	@FXML
	private TextField port_field;
	@FXML
	private MenuButton connection_menu;

	public ConnectingStateController(ConnectingState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	public void confirmIp(ActionEvent event) {
		ip = ip_field.getText();
		ip_field.setDisable(true);
		port_field.setDisable(false);
	}

	@FXML
	public void confirmPort(ActionEvent event) {
		port = port_field.getText();
		connection_menu.setDisable(false);
	}

	@FXML
	public void confirmRMI(ActionEvent event) {

		//state.connect(ip, port, "rmi");
	}

	@FXML
	public void confirmTCP(ActionEvent event) {
		//state.connect(ip, port, "tcp");
	}

	@FXML
	protected void initialize() {
		ip_field.setOnAction(this::confirmIp);
		port_field.setOnAction(this::confirmPort);
		connection_menu.getItems().get(0).setOnAction(this::confirmRMI);
		connection_menu.getItems().get(1).setOnAction(this::confirmTCP);
	}

}
