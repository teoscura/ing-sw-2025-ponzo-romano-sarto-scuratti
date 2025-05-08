package it.polimi.ingsw.controller.client.state;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.ConsumerThread;
import it.polimi.ingsw.controller.client.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.message.server.PingMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.view.ClientView;

public class ConnectedState extends ClientControllerState {
    
    private final ServerConnection connection;
	private final String username;
	private final Thread consumer_thread;

	public ConnectedState(ClientController controller, ClientView view, String username, ServerConnection connection, ThreadSafeMessageQueue inqueue){
		super(controller, view);
		this.username = username;
		this.connection = connection;
		this.consumer_thread = new ConsumerThread(this, inqueue);
	}

	@Override
	public void init(){
		this.setPingTask();
		consumer_thread.start();
	}

	@Override
	public ClientControllerState getNext(){
		return new TitleScreenState(controller, view);
	}

	// -------------------------------------------------------------
    // Communication methods
    // -------------------------------------------------------------

	@Override
	public void sendMessage(ServerMessage message) {
		try {
			this.connection.sendMessage(message);
		} catch (IOException e) {
			System.out.println("Failed to send a message, terminating program!");
			this.connection.close();
			this.transition();
		}
	}

    public void disconnect() {
		try {
			this.connection.sendMessage(new ServerDisconnectMessage());
            this.connection.close();
            this.transition();
		} catch (RemoteException e) {
			this.transition();
		} catch (IOException e) {
			this.transition();
		}
	}

	// -------------------------------------------------------------
    // View methods
    // -------------------------------------------------------------

	public ClientView getView(){
		return this.view;
	}

	public String getUsername(){
		return username;
	}

	public void showTextMessage(String message) {
		this.view.showTextMessage(message);
	}

	// -------------------------------------------------------------
    // Ping and disconnection resilience methods.
    // -------------------------------------------------------------

	public void ping() {
		this.sendMessage(new PingMessage());
		this.setPingTask();
	}

	private void setPingTask() {
		Timer t = new Timer(true);
		t.schedule(this.getPingTask(this), 100);
	}

	private TimerTask getPingTask(ConnectedState controller) {
		return new TimerTask() {
			public void run() {
				controller.sendMessage(new PingMessage());
			}
		};
	}

}
