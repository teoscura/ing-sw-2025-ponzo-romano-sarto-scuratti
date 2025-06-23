package it.polimi.ingsw.controller.client.connections;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerDisconnectMessage;
import it.polimi.ingsw.message.server.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A client side TCP connection to a remote game server.
 */
public class SocketConnection extends Thread implements ServerConnection {

	private final ThreadSafeMessageQueue<ClientMessage> inqueue;
	private final Socket socket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;

	/**
	 * Constructs a {@link SocketConnection} object.
	 * 
	 * @param inqueue {@link ThreadSafeMessageQueue} Queue in which incoming {@link it.polimi.ingsw.message.server.ServerMessage messages} will be inserted.
	 * @param server_ip IP address on which the remote game server is running.
	 * @param server_port Port on which the remote TCP game server is listening.
	 * @throws IOException If there are any errors during the connection process.
	 */
	public SocketConnection(ThreadSafeMessageQueue<ClientMessage> inqueue, String server_ip, int server_port)
			throws IOException {
		if (inqueue == null)
			throw new NullPointerException();
		this.inqueue = inqueue;
		this.socket = new Socket();
		this.socket.connect(new InetSocketAddress(server_ip, server_port), 15000);
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Main loop of a socket connection, in charge of reading any possible incoming messages and casting them to a supported format.
	 */
	public void run() {
		while (!this.socket.isClosed()) {
			ClientMessage message = null;
			try {
				message = (ClientMessage) in.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("Received an invalid object via TCP, discarding it.");
			} catch (IOException e) {
				System.out.println("Failed to read object from: " + socket.getInetAddress() + ", closing socket.");
				this.close();
				return;
			}
			inqueue.insert(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(ServerMessage message) throws IOException {
		this.out.reset();
		this.out.writeObject(message);
		this.out.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			this.interrupt();
			socket.close();
		} catch (IOException e) {
			System.out.println("Closing socket connection.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Thread getShutdownHook() {
		SocketConnection caller = this;
		return new Thread() {
			public void run() {
				caller.interrupt();
				try {
					sendMessage(new ServerDisconnectMessage());
					socket.close();
				} catch (IOException e) {
					System.out.println("Ran Shutdown Hook on SocketConnection");
				}
			}
		};
	}


}
