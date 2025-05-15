package it.polimi.ingsw.controller.server.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TimerTask;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;

public class SocketClient implements ClientConnection {

	private final Socket socket;
	private TimerTask setup_timeout;
	private String username;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;

	public SocketClient(Socket socket) throws IOException {
		if (socket == null) throw new NullPointerException();
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void setTimeout(TimerTask task){
		this.setup_timeout = task;
	}

	public void cancelTimeout(){
		this.setup_timeout.cancel();
	}

	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public void sendMessage(ClientMessage message) throws IOException {
		this.out.reset();
		this.out.writeObject(message);
		this.out.flush();
	}

	public void read(MainServerController controller) {
		ServerMessage message = null;
		try {
			message = (ServerMessage) in.readObject();
		} catch (ClassNotFoundException e) {
			//XXX logger message
		} catch (IOException e) {
			//XXX logger
			System.out.println("Failed to read object from: " + socket.getInetAddress() + ", closing socket.");
			this.close();
		}
		if (message.getDescriptor() != null) {
			message.setDescriptor(null);
		}
		if (username == null) {
			UsernameSetupMessage setup = null;
			try {
				setup = (UsernameSetupMessage) message;
			} catch (ClassCastException e) {
				//XXX logger
				System.out.println("Received non-setup message from tcp socket: " + socket.getInetAddress());
			}
			this.username = setup.getUsername();
			controller.setupSocketListener(this, this.username);
			return;
		}
		message.setDescriptor(controller.getDescriptor(this.username));
		controller.receiveMessage(message);
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			//XXX logger message
		}
	}

}
