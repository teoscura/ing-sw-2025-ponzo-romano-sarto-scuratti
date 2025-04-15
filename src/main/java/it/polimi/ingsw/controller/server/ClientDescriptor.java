package it.polimi.ingsw.controller.server;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.player.Player;

public class ClientDescriptor {
    
    private final String username;
    private final Connection connection;
    //XXX timeout task running every time a ping gets here, pings reset it.
    private Player player = null;

    public ClientDescriptor(String username, Connection connection){
        if(username==null||connection==null) throw new NullPointerException();
        this.username = username;
        this.connection = connection;
    }

    public void bindPlayer(Player p) throws Exception{
        if(player!=null) throw new Exception("This descriptor already has a player");
        p.bindDescriptor(this);
        this.player = p;
    }

    public void sendMessage(ClientMessage m){
        this.connection.sendMessage(m);
    }

    public String getUsername(){
        return this.username;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void ping(){
        this.timer.ping();
    }

}
