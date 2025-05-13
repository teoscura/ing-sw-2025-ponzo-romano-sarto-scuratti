package it.polimi.ingsw.controller.client.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;

public class SocketConnection extends Thread implements ServerConnection {

	private final ThreadSafeMessageQueue<ClientMessage> inqueue;
	private final Socket socket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;

	public SocketConnection(ThreadSafeMessageQueue<ClientMessage> inqueue, String server_ip, int server_port)
			throws IOException {
		if (inqueue == null)
			throw new NullPointerException();
		this.inqueue = inqueue;
		this.socket = new Socket(server_ip, server_port);
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void run() {
		while (!this.socket.isClosed()) {
			ClientMessage message = null;
			try {
				message = (ClientMessage) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Failed to read object from: " + socket.getInetAddress() + ", closing socket.");
				this.close();
				e.printStackTrace();
			}
			inqueue.insert(message);
		}
	}

	@Override
	public void sendMessage(ServerMessage message) throws IOException {
		this.out.reset();
		this.out.writeObject(message);
		this.out.flush();
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
