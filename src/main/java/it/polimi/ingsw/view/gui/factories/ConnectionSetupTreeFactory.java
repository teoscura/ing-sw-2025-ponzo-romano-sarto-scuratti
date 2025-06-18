package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.gui.GUIView;
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
        TextField ip = new TextField();
        ip.setMaxWidth(300);
        ip.setCenterShape(true);
        Label l2 = new Label("Set Port: ");
        TextField port = new TextField();
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
