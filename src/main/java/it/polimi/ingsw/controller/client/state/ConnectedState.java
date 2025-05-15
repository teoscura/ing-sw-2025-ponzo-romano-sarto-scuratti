package it.polimi.ingsw.controller.client.state;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.ConsumerThread;
import it.polimi.ingsw.controller.client.SenderThread;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.PingMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.commandbuilder.InputCommandTask;

public class ConnectedState extends ClientControllerState {

    private final ServerConnection connection;
	private final ThreadSafeMessageQueue<ServerMessage> outqueue;
	private final String username;
	private final Thread consumer_thread;
	private final Thread sender_thread;
	private final Thread commandbuilder_thread;
	private final Thread shutdown_hook;

	public ConnectedState(ClientController controller, ClientView view, String username, ServerConnection connection, ThreadSafeMessageQueue<ClientMessage> inqueue){
		super(controller, view);
		this.username = username;
		this.connection = connection;
		this.consumer_thread = new ConsumerThread(this, inqueue);
		this.outqueue = new ThreadSafeMessageQueue<>(100);
		this.sender_thread = new SenderThread(this.outqueue, this.connection);
		this.commandbuilder_thread = new InputCommandTask(this);
		this.shutdown_hook = this.getShutdownHook();
	}

	@Override
	public void init(){
		Runtime.getRuntime().addShutdownHook(this.shutdown_hook);
		this.startPingTask();
		consumer_thread.start();
		sender_thread.start();
        commandbuilder_thread.start();
	}

	@Override
	public ClientControllerState getNext(){
		return new TitleScreenState(controller, view);
	}

	@Override
	public void onClose() {
		Runtime.getRuntime().removeShutdownHook(this.shutdown_hook);
		this.disconnect();
	}

	// -------------------------------------------------------------
    // Communication methods
    // -------------------------------------------------------------

	@Override
	public void sendMessage(ServerMessage message) {
		this.outqueue.insert(message);
	}

    public void disconnect() {
		try {
			this.connection.sendMessage(new ServerDisconnectMessage());
            this.connection.close();
			this.sender_thread.interrupt();
			this.consumer_thread.interrupt();
            this.controller.reset();
		} catch (RemoteException e) {
			this.controller.reset();
		} catch (IOException e) {
			this.controller.reset();
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

	private void startPingTask() {
		Timer t = new Timer(true);
		t.scheduleAtFixedRate(this.getPingTask(this), 0, 100);
	}

	private TimerTask getPingTask(ConnectedState controller) {
		return new TimerTask() {
			public void run() {
				ping();
			}
		};
	}

	private void ping() {
		this.sendMessage(new PingMessage());
	}

	private Thread getShutdownHook(){
		return this.connection.getShutdownHook();
	}

}
