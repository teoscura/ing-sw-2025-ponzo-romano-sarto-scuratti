package it.polimi.ingsw.controller.server.connections;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;

import java.io.IOException;
import java.io.Serializable;
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
		try {
			registry = LocateRegistry.createRegistry(this.rmiport);
			registry.bind("galaxy_truckers", this);
			UnicastRemoteObject.exportObject(this, this.rmiport);
			/*XXX*/System.out.println("Set up RMI on address: '" + this.ip + ":" + this.rmiport + "'...");
			Runtime.getRuntime().addShutdownHook(this.RMICleanup());
		} catch (RemoteException e) {
			/*XXX*/System.out.println("Failed to setup the rmi registry and remote object.");
		} catch (InaccessibleObjectException e) {
			/*XXX*/System.out.println("Couldn't bind RMI due to access permissions! Is this running inside a sandboxed JUnit test?");
		} catch (AlreadyBoundException e) {
			/*XXX*/System.out.println("Name is already bound, terminating.");
			System.exit(-1);
		}
		try {
			this.server = new ServerSocket();
			this.server.bind(new InetSocketAddress(this.ip, this.tcpport));
			/*XXX*/System.out.println("Started server on: '" + ip + ":" + this.server.getLocalPort() + "'...");
			Runtime.getRuntime().addShutdownHook(this.TCPCleanup());
		} catch (IOException e) {
			/*XXX*/System.out.println("Couldn't start server on the specified address and port, terminating.");
			System.exit(-1);

		}
		/*XXX*/System.out.println("Successfully started server.");
	}

	@Override
	public void run() {
		if (!this.init) throw new NotYetConnectedException();
		this.startServer();
		try {
			while (true) {
				SocketClient new_connection = new SocketClient(server.accept());
				MainServerController.getInstance().connectListener(new_connection);
				this.serverPool.submit(new_connection);
			}
		} catch (IOException e) {
			try {
				this.server.close();
				System.exit(-1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(-1);
		}
	}

	private Thread RMICleanup() {
		return new Thread() {
			public void run() {
				try {
					this.interrupt();
					Registry registry = LocateRegistry.getRegistry();
					registry.unbind("galaxy_truckers");
				} catch (RemoteException | NotBoundException e) {
				}
				/*XXX*/System.out.println("Cleaned up RMI connection.");
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
				/*XXX*/System.out.println("Cleaned up TCP connection.");
			}
		};
	}

	public VirtualServer accept(RMIClientConnection client) throws RemoteException {
		ClientDescriptor new_client = MainServerController.getInstance().connectListener(client);
		try {
			return MainServerController.getInstance().getStub(new_client);
		} catch (RemoteException e) {
			/*XXX*/System.out.println(e.getMessage());
			return null;
		}
	}

}
