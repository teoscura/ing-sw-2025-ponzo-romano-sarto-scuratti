package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.UsernameSetupMessage;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TimerTask;

public class SocketClient extends Thread implements ClientConnection {

	private final Socket socket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	private TimerTask setup_timeout;
	private String username;

	public SocketClient(Socket socket) throws IOException {
		if (socket == null) throw new NullPointerException();
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void setTimeout(TimerTask task) {
		this.setup_timeout = task;
	}

	public void cancelTimeout() {
		this.setup_timeout.cancel();
	}

	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public void run() {
		while (!this.socket.isClosed() && this.username == null) {
			this.readSetup();
		}
		while (!this.socket.isClosed()) {
			this.read();
		}
	}

	@Override
	public void sendMessage(ClientMessage message) throws IOException {
		this.out.reset();
		this.out.writeObject(message);
		this.out.flush();
	}

	private void readSetup() {
		MainServerController controller = MainServerController.getInstance();
		UsernameSetupMessage setup = null;
		try {
			setup = (UsernameSetupMessage) in.readObject();
		} catch (ClassNotFoundException e) {
			Logger.getInstance().print(LoggerLevel.NOTIF, "Failed to read class object from tcp socket: " + socket.getInetAddress());
			return;
		} catch (IOException e) {
			Logger.getInstance().print(LoggerLevel.ERROR, "Failed to read object from: " + socket.getInetAddress() + ", closing socket.");
			MainServerController.getInstance().disconnect(controller.getDescriptor(this.username));
		} catch (ClassCastException e) {
			Logger.getInstance().print(LoggerLevel.NOTIF, "Received non-setup message from tcp socket: " + socket.getInetAddress());
			return;
		}
		this.username = setup.getUsername();
		controller.setupSocketListener(this, this.username);
	}

	private void read() {
		MainServerController controller = MainServerController.getInstance();
		ServerMessage message = null;
		try {
			message = (ServerMessage) in.readObject();
		} catch (ClassNotFoundException e) {
			Logger.getInstance().print(LoggerLevel.NOTIF, "Failed to read class object from tcp socket: " + socket.getInetAddress());
			return;
		} catch (IOException e) {
			Logger.getInstance().print(LoggerLevel.ERROR, "Failed to read object from: " + socket.getInetAddress() + ", closing socket.");
			MainServerController.getInstance().disconnect(controller.getDescriptor(this.username));
			return;
		}
		message.setDescriptor(controller.getDescriptor(this.username));
		controller.receiveMessage(message);
	}

	@Override
	public void close() {
		try {
			this.interrupt();
			socket.close();
		} catch (IOException e) {
			Logger.getInstance().print(LoggerLevel.NOTIF, "Finalized closing procedure for socket: '" + socket.getInetAddress() + "'.");
		}
	}

}
