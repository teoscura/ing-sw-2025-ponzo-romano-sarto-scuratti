package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.model.player.Player;

public class ClientDescriptor {
    
    protected static final long TIMEOUT_DURATION = 15000L;
    private final String username;
    private transient final Connection connection;
    private transient TimerTask pingtimer;
    private transient Player player = null;

    public ClientDescriptor(String username, Connection connection){
        if(username==null/*XXX rimetti connection == null*/) throw new NullPointerException();
        this.username = username;
        this.connection = connection;
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
