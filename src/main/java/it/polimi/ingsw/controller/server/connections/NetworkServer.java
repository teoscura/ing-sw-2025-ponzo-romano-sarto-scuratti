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

public class NetworkServer extends Thread implements RMISkeletonProvider, Serializable {

	private final ExecutorService serverPool;
	private String ip = "localhost";
	private int tcpport = 0, rmiport;
	private boolean init = false;
	private ServerSocket server;

	public NetworkServer() {
		this.serverPool = new ThreadPoolExecutor(20, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
	}

	public void init(String address, int tcpport, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.tcpport = tcpport;
		this.rmiport = rmiport;
		this.init = true;
	}

	public void init(String address, int rmiport) {
		if (this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.tcpport = 0;
		this.rmiport = rmiport;
		this.init = true;
	}

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

	@Override
	public void run() {
		if (!this.init) throw new NotYetConnectedException();
		this.startServer();
		while (true) {
			try {
				SocketClient new_connection = new SocketClient(server.accept());
				MainServerController.getInstance().connectListener(new_connection);
				this.serverPool.submit(new_connection);
			} catch (IOException e) {
				Logger.getInstance().print(LoggerLevel.ERROR, "Server IOException trying to accept a connection with TCP: "+e.getMessage());
			}
		}
	}


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

	public VirtualServer accept(RMIClientConnection client) throws RemoteException {
		ClientDescriptor new_client = MainServerController.getInstance().connectListener(client);
		if(new_client==null) {
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
