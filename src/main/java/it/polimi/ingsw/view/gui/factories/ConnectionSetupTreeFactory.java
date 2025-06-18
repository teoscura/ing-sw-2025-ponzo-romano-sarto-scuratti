package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ConnectionSetupTreeFactory {
    
	static public Node createConnectionScreen(ConnectingState state) {
        Label l1 = new Label("Set IP: ");
        l1.getStyleClass().add("list-label-big");
        TextField ip = new TextField();
        ip.getStyleClass().add("titlescreen-field");
        ip.setMaxWidth(300);
        ip.setCenterShape(true);
        Label l2 = new Label("Set Port: ");
        l2.getStyleClass().add("list-label-big");
        TextField port = new TextField();
        port.getStyleClass().add("titlescreen-field");
        port.setMaxWidth(300);
        port.setCenterShape(true);
        ChoiceBox<String> connection_menu = new ChoiceBox<>();
        connection_menu.getItems().addAll("TCP", "RMI");
        Button confirm = new Button();
        confirm.setOnAction(event->{
            String ipv = null;
            int portv = 0;
            ConnectionType tv = ConnectionType.NONE;
            tv = connection_menu.getValue() == "RMI" ? ConnectionType.RMI : connection_menu.getValue() == "TCP" ? ConnectionType.SOCKET : ConnectionType.NONE;
            ipv = ip.getText();
            try{
                portv = Integer.parseInt(port.getText());
                if(portv<0||portv>65535) throw new NumberFormatException();
            } catch (NumberFormatException e){
                portv = 0;
                tv = ConnectionType.NONE;
            }
            state.connect(ipv, portv, tv);
        });
        confirm.setText("Connect");
        VBox res = new VBox(10.0, l1, ip, l2, port, connection_menu, confirm);
        res.setAlignment(Pos.CENTER);
        res.setMaxHeight(800);
        return res;
    }

}
