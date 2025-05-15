package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.connections.ServerConnection;
import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;

public class SenderThread extends Thread {

	private final ThreadSafeMessageQueue<ServerMessage> outqueue;
	private final ServerConnection connection;

	public SenderThread(ThreadSafeMessageQueue<ServerMessage> outqueue, ServerConnection connection) {
		if (outqueue == null || connection == null) throw new NullPointerException();
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
					System.out.println("Failed to send a message, terminating program!");
					this.connection.close();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
