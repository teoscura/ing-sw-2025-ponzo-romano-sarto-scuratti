package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;

public class SenderThread extends Thread {

	private final ThreadSafeMessageQueue<ServerMessage> outqueue;
	private final ServerConnection connection;
	private final ConnectedState state;

	public SenderThread(ConnectedState state, ThreadSafeMessageQueue<ServerMessage> outqueue, ServerConnection connection) {
		if (state == null || outqueue == null || connection == null) throw new NullPointerException();
		this.state = state;
		this.outqueue = outqueue;
		this.connection = connection;
	}

	@Override
	public void run() {
		while (true) {
			try {
				try {
					this.connection.sendMessage(outqueue.take());
				} catch (IOException e) {
					System.out.println("Failed to send a message, terminating connection!");
					this.state.onClose();
				}
			} catch (InterruptedException e) {
				System.out.println("Shutting down connection thread.");
			}
		}
	}


}
