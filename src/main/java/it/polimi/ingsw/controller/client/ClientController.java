package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.PingMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientController implements iClientController {

    private final ClientView view;
    private ServerConnection connection;

    public ClientController(ViewType vtype, ConnectionType ctype, String local_ip, String server_ip, String username){
        //TODO
    }

    @Override
    public void showTextMessage(String message) {
        this.view.showTextMessage(message);
    }

    @Override
    public void ping() {
        this.connection.sendMessage(new PingMessage());
    }

    @Override
    public ClientView getView() {
        x;
    }

    @Override
    public void connect(String ip) {
        x;
    }

    @Override
    public void disconnect() {
        this.connection.sendMessage(new ServerDisconnectMessage());
        this.connection.close();
    }

    public void recieveMessage(ClientMessage message) {
        x;
    }

    public void setServerConnection(ServerConnection connection) {
        x;
    }
    
}
