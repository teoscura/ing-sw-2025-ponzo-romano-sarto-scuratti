package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Factory that exposes a static method to create all Nodes needed to display a {@link it.polimi.ingsw.controller.client.state.ConnectingState}.
 */
public class ConnectionSetupTreeFactory {

	static public Node createConnectionScreen(ConnectingState state) {
		Label l1 = new Label("Set IP: ");
		l1.getStyleClass().add("list-label-big");
		TextField ip = new TextField();
		ip.getStyleClass().add("titlescreen-field");
		ip.setMaxWidth(300);

		Label l2 = new Label("Set Port: ");
		l2.getStyleClass().add("list-label-big");
		TextField port = new TextField();
		port.getStyleClass().add("titlescreen-field");
		port.setMaxWidth(300);


		Button tcpBtn = new Button("Connect with TCP");
		tcpBtn.getStyleClass().add("button-label-connection");

		tcpBtn.setOnAction(event -> {
			handleConnection(state, ip, port, ConnectionType.SOCKET);
		});

		Button rmiBtn = new Button("Connect with RMI");
		rmiBtn.getStyleClass().add("button-label-connection");
		rmiBtn.setOnAction(event -> {
			handleConnection(state, ip, port, ConnectionType.RMI);
		});

		HBox connectionButtons = new HBox(20.0, tcpBtn, rmiBtn);
		connectionButtons.setAlignment(Pos.CENTER);

		VBox res = new VBox(20.0, l1, ip, l2, port, connectionButtons);
		res.setAlignment(Pos.CENTER);
		res.setMaxHeight(800);
		return res;
	}


	private static void handleConnection(ConnectingState state, TextField ip, TextField port, ConnectionType type) {
		String ipv = ip.getText();
		int portv;

		if (ipv == null || ipv.isBlank()) {
			System.err.println("IP address is empty");
			return;
		}

		try {
			portv = Integer.parseInt(port.getText());
			if (portv < 0 || portv > 65535) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			portv = 0;
			type = ConnectionType.NONE;
		}
		state.connect(ipv, portv, type);
	}
}