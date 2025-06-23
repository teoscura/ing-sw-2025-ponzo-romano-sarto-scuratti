package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.Cleaner;
import java.lang.reflect.InaccessibleObjectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Represents the entry point for any incoming connection to the {@link MainServerController}, for both protocols.
 */
public class NetworkServer extends Thread implements VirtualServerProvider, Serializable {

	private final ExecutorService serverPool;
	private String ip = "localhost";
	private int tcpport = 0, rmiport;
	private boolean init = false;
	private ServerSocket server;

	/**
	 * Constructs a {@link NetworkServer} object, only initializing the {@link ExecutorService}.
	 */
	public NetworkServer() {
		this.serverPool = new ThreadPoolExecutor(20, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
	}

	/**
	 * Properly sets all parameters to open the server to inbound connections.
	 * 
	 * @param address IP address on which both protocols will be listening.
	 * @param tcpport Port on which the TCP Socket will be opened.
	 * @param rmiport Port on which the RMI Registy will be created.
	 */
	public void init(String address, int tcpport, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.tcpport = tcpport;
		this.rmiport = rmiport;
		this.init = true;
	}

	/**
	 * Properly sets all parameters to open the server to inbound connections. TCP port to be randomly selected.
	 * 
	 * @param address IP address on which both protocols will be listening.
	 * @param rmiport Port on which the RMI Registy will be created.
	 */
	public void init(String address, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.tcpport = 0;
		this.rmiport = rmiport;
		this.init = true;
	}

	/**
	 * Starts both protocols with the values set using {@link NetworkServer#init(String, int, int) init(...)} and creates the RMI Registry objects binding this object to an entry.
	 */
	private void startServer() {
		Registry registry = null;
		Cleaner c = Cleaner.create();
		//Rmi ports less than zero are not allowed in argument parsing at launch, but are used by tests to get around
		//Certain JVM limitations, as only one rmi registry per JVM may be launched, breaking JUnit tests that don't 
		//rely on Network functionality, but may call the singleton MainServerController, which launches this class.
		if (rmiport >= 0) {
			try {
				System.setProperty("java.rmi.server.hostname", ip);
				registry = LocateRegistry.createRegistry(rmiport);
				registry.bind("galaxy_truckers", this);
				UnicastRemoteObject.exportObject(this, this.rmiport);
				Logger.getInstance().print(LoggerLevel.SERVR, "Set up RMI on address: '" + this.ip + ":" + this.rmiport + "'...");
				c.register(this, this.RMICleanup());
			} catch (RemoteException e) {
				Logger.getInstance().print(LoggerLevel.ERROR, "Failed to setup the rmi registry and remote object, terminating.");
				System.exit(-1);
			} catch (InaccessibleObjectException e) {
				Logger.getInstance().print(LoggerLevel.WARN, "Couldn't bind RMI due to access permissions! Is this running inside a sandboxed JUnit test?");
			} catch (AlreadyBoundException e) {
				Logger.getInstance().print(LoggerLevel.ERROR, "Name is already bound, terminating.");
				System.exit(-1);
			}
		}
		try {
			this.server = new ServerSocket();
			this.server.bind(new InetSocketAddress(this.ip, this.tcpport));
			Logger.getInstance().print(LoggerLevel.SERVR, "Set up TCP on address: '" + ip + ":" + this.server.getLocalPort() + "'...");
			c.register(this, this.TCPCleanup());
		} catch (IOException e) {
			Logger.getInstance().print(LoggerLevel.ERROR, "Couldn't start server on the specified address and port, terminating.");
			System.exit(-1);

		}
		Logger.getInstance().print(LoggerLevel.SERVR, "Successfully started server.");
	}

	/**
	 * Main loop of the {@link NetworkServer}, listening for incoming TCP connections.
	 */
	@Override
	public void run() {
		if (!this.init) throw new NotYetConnectedException();
		this.startServer();
		while (true) {
			try {
				var socket = server.accept();
				socket.setSoTimeout(15000);
				SocketClient new_connection = new SocketClient(socket);
				MainServerController.getInstance().connectListener(new_connection);
				this.serverPool.submit(new_connection);
			} catch (IOException e) {
				Logger.getInstance().print(LoggerLevel.ERROR, "Server IOException trying to accept a connection with TCP: " + e.getMessage());
			}
		}
	}

	/**
	 * @return A thread object related to the proper closure of any remnants of the RMI Server.
	 */
	private Thread RMICleanup() {
		return new Thread() {
			public void run() {
				try {
					this.interrupt();
					Registry registry = LocateRegistry.getRegistry();
					registry.unbind("galaxy_truckers");
					UnicastRemoteObject.unexportObject(registry, true);
				} catch (RemoteException | NotBoundException e) {
				}
				Logger.getInstance().print(LoggerLevel.SERVR, "Cleaned up RMI connection.");
			}
		};
	}

	/**
	 * @return A thread object related to the proper closure of any remnants of the TCP Server.
	 */
	private Thread TCPCleanup() {
		return new Thread() {
			public void run() {
				try {
					this.interrupt();
					server.close();
				} catch (IOException e) {
				}
				Logger.getInstance().print(LoggerLevel.SERVR, "Cleaned up TCP connection.");
			}
		};
	}

	/**
	 * Main entry method for any {@link RMIClientConnection} wishing to connect. 
	 * 
	 * @param client {@link RMIClientConnection} Client stub object exported before connecting.
	 * @return {@link VirtualServer} Server stub object used by client to send {@link it.polimi.ingsw.message.server.ServerMessage messages}, null if connection is refused.
	 * @throws RemoteException If the underlying RMI TCP channel is disrupted in any unrecoverable way.
	 */
	public VirtualServer accept(RMIClientConnection client) throws RemoteException {
		ClientDescriptor new_client = MainServerController.getInstance().connectListener(client);
		if (new_client == null) {
			try {
				client.sendMessage(new ViewMessage("A player with that name is already connected!"));
				client.sendMessage(new ClientDisconnectMessage());
			} catch (IOException e) {
				Logger.getInstance().print(LoggerLevel.ERROR, "Failed to send a disconnect message to a refused RMI connection");
			}
			return null;
		}
		try {
			return MainServerController.getInstance().getStub(new_client);
		} catch (RemoteException e) {
			Logger.getInstance().print(LoggerLevel.ERROR, e.getMessage());
			return null;
		}
	}

}
