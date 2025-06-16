package it.polimi.ingsw.view.gui;

import java.util.Objects;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class ConnectionSetupTreeFactory {
    
	static public ConnectingStateController(final StackPane root, ConnectingState state) {
        TextField ip_field;
        TextField port_field;
        ChoiceBox<String> connection_menu;
        Button connect = new Button();

        .setOnMouseClicked(event->{
            //BLa bla controllo validita' valori bla bla.
            state.connect(ip_field.getText(), Integer.parseInt(port_field.getText()), connection_menu.getValue());
        });
        
        Node res = null;

        //Aggiungi a root in centro
        root.getChildren().add(res, Pos.CENTER);
	}

}
