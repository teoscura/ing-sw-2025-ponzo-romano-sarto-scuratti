package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;

/**
 * Thread object in charge of sending all messages to the server.
 */
public class SenderThread extends Thread {

	private final ThreadSafeMessageQueue<ServerMessage> outqueue;
	private final ServerConnection connection;
	private final ConnectedState state;

	/**
	 * Construct a {@link SenderThread} object.
	 * 
	 * @param state {@link ConnectedState} Client Controller State to which this Thread object is bound.
	 * @param outqueue {@link ThreadSafeMessageQueue} Queue containing all received messages.
	 * @param connection {@link ServerConnection} Connection used to send messages with.
	 */
	public SenderThread(ConnectedState state, ThreadSafeMessageQueue<ServerMessage> outqueue, ServerConnection connection) {
		if (state == null || outqueue == null || connection == null) throw new NullPointerException();
		this.state = state;
		this.outqueue = outqueue;
		this.connection = connection;
	}

	/**
	 * Main loop for the SenderThread object, sending any message available in the queue in a FIFO order.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				this.connection.sendMessage(outqueue.take());
			} catch (IOException e) {
				outqueue.dump();
				this.state.disconnect();
				state.getView().showTextMessage("Error sending message to server, closing connection!");
				return;
			} catch (InterruptedException e) {
				state.getView().showTextMessage("Interrupted Sender Thread!");
				outqueue.dump();
				return;
			}
		}
	}


}
