package it.polimi.ingsw.controller.client.state;

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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the state of the client after having successfully connected to the game server.
 */
public class ConnectedState extends ClientControllerState {

	private final ServerConnection connection;
	private final ThreadSafeMessageQueue<ServerMessage> outqueue;
	private final String username;
	private final Thread consumer_thread;
	private final Thread sender_thread;
	private final Thread shutdown_hook;
	private final Timer pingtimer;

	/**
	 * Construct a {@link ConnectedState} object.
	 * 
	 * @param controller {@link ClientController} Controller to which this state is tied to.
	 * @param view {@link it.polimi.ingsw.view.ClientView} View to which this state is tied to.
	 * @param username Username with which the client has connected to the server.
	 * @param connection A established connection to the server.
	 * @param inqueue A queue used to send messages to the server.
	 */
	public ConnectedState(ClientController controller, ClientView view, String username, ServerConnection connection, ThreadSafeMessageQueue<ClientMessage> inqueue) {
		super(controller, view);
		this.username = username;
		this.connection = connection;
		this.consumer_thread = new ConsumerThread(this, inqueue);
		this.outqueue = new ThreadSafeMessageQueue<>(100);
		this.sender_thread = new SenderThread(this, this.outqueue, this.connection);
		this.shutdown_hook = this.getShutdownHook();
		this.pingtimer = new Timer(true);
	}

	/**
	 * Initialize the state by adding all necessary cleanup related shutdown hooks, notifying the view and starting all accessory threads.
	 */
	@Override
	public void init() {
		Runtime.getRuntime().addShutdownHook(this.shutdown_hook);
		this.startPingTask();
		this.view.connect(this);
		consumer_thread.start();
		sender_thread.start();
	}

	@Override
	public ClientControllerState getNext() {
		return new TitleScreenState(controller, view);
	}

	/**
	 * Cleanup all connection and thread related resources that might cause conflicts of any kind down the line.
	 */
	@Override
	public void onClose() {
		Runtime.getRuntime().removeShutdownHook(this.shutdown_hook);
		try {
			this.sender_thread.interrupt();
			stopPingTask();
			this.connection.sendMessage(new ServerDisconnectMessage());
			this.connection.close();
			this.consumer_thread.interrupt();
		} catch (RemoteException e) {
			view.showTextMessage("Forced RMI Disconnect!");
		} catch (IOException e) {
			view.showTextMessage("Forced TCP Disconnect!");
		} finally {
			this.view.disconnect();
		}
	}

	// -------------------------------------------------------------
	// Communication methods
	// -------------------------------------------------------------

	/**
	 * Adds a {@link it.polimi.ingsw.message.server.ServerMessage} to the {@link ThreadSafeMessageQueue queue} asynchronously to be sent to the server.
	 * @param message {@link it.polimi.ingsw.message.server.ServerMessage} Message to be sent.
	 */
	public void sendMessage(ServerMessage message) {
		this.outqueue.insert(message);
	}

	/**
	 * Disconnect from the server and cleanup all connection and thread related resources that might cause conflicts of any kind down the line.
	 */
	public void disconnect() {
		try {
			this.sender_thread.interrupt();
			stopPingTask();
			this.connection.sendMessage(new ServerDisconnectMessage());
			this.sender_thread.interrupt();
			this.consumer_thread.interrupt();
			this.connection.close();
		} catch (RemoteException e) {
			view.showTextMessage("Forced RMI Disconnect!");
		} catch (IOException e) {
			view.showTextMessage("Forced TCP Disconnect!");
		} finally {
			this.view.disconnect();
			this.controller.reset();
		}
	}

	// -------------------------------------------------------------
	// View methods
	// -------------------------------------------------------------

	public ClientView getView() {
		return this.view;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * Show a text message on the {@link it.polimi.ingsw.view.ClientView}.
	 * 
	 * @param message Text to be shown.
	 */
	public void showTextMessage(String message) {
		this.view.showTextMessage(message);
	}

	// -------------------------------------------------------------
	// Ping and disconnection resilience methods.
	// -------------------------------------------------------------

	/**
	 * Start the recurrent task in charge of sending regular {@link PingMessage pings} to the server.
	 */
	private void startPingTask() {
		pingtimer.scheduleAtFixedRate(this.getPingTask(), 0, 100);
	}

	/**
	 * Stops the recurrent task in charge of sending regular {@link PingMessage pings} to the server.
	 */
	private void stopPingTask() {
		pingtimer.cancel();
	}

	/**
	 * Returns a task that sends a single {@link PingMessage} to the server.
	 * 
	 * @return The task.
	 */
	private TimerTask getPingTask() {
		return new TimerTask() {
			public void run() {
				ping();
			}
		};
	}

	/**
	 * Sends a single {@link PingMessage}.
	 */
	private void ping() {
		this.sendMessage(new PingMessage());
	}

	private Thread getShutdownHook() {
		return this.connection.getShutdownHook();
	}

}
