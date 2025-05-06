package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.server.connections.Connection;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.player.Player;

public class ClientDescriptor {
    
    private static final long TIMEOUT_DURATION = 15000L;
    private final String username;
    private transient int id;
    private transient TimerTask pingtimer;
    private transient Player player = null;
    private transient final Connection connection;

    public ClientDescriptor(String username, Connection connection){
        if(username==null || connection == null) throw new NullPointerException();
        this.username = username;
        this.connection = connection;
        this.id = -1;
    }

    public void bindPlayer(Player p){
        p.bindDescriptor(this);
        this.player = p;
    }

    public void sendMessage(ClientMessage m) throws IOException {
        this.connection.sendMessage(m);
    }

    public void setPingTimerTask(TimerTask task){
        this.pingtimer = task;
        Timer t = new Timer(true);
        t.schedule(task, TIMEOUT_DURATION);
    }

    public String getUsername(){
        return this.username;
    }

    public int getId(){
        return this.id;
    }

    public void setID(int id){
        if(id<-1) throw new IllegalArgumentException();
        this.id = id;
    }

    public Player getPlayer(){
        return this.player;
    }

    public TimerTask getPingTimerTask(){
        return this.pingtimer;
    }

	public Connection getConnection() {
		return this.connection;
	}

}
