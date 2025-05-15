package it.polimi.ingsw.controller.server.connections;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InaccessibleObjectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.AccessException;
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

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.MainServerController;

public class NetworkServer extends Thread implements RMISkeletonProvider, Serializable {

	private final ExecutorService serverPool;
	private String ip = "localhost";
	private int rmiport;
	private boolean init = false;
	private ServerSocket server;

	public NetworkServer() {
		this.serverPool = new ThreadPoolExecutor(20, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
	}

	public void init(String address, int rmiport){
        if(this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.rmiport = rmiport;
		this.init = true;
		System.out.println("Setting up RMI.");
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(this.rmiport);
			registry.bind("galaxy_truckers", this);
			UnicastRemoteObject.exportObject(this, this.rmiport);
		} catch (RemoteException e) {
			System.out.println("Failed to setup the rmi registry and remote object, terminating.");
		} catch (InaccessibleObjectException e) {
			System.out.println("Couldn't bind RMI! Are you testing?");
		} catch (AlreadyBoundException e) {
			System.out.println("Already bound!");
		}
		System.out.println("Successfully set up RMI.");
		System.out.println("Starting server on: \'"+ip+"\'.");
		try {
			this.server = new ServerSocket();
			this.server.bind(new InetSocketAddress(this.ip, 0));
			Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){cleanUp();}});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to setup the server socket, terminating.");
		}
		System.out.println("Successfully started server on: \'"+ip+"\':\'"+this.server.getLocalPort()+"\''.");
		
    }

	@Override
	public void run() {
		if(!this.init) throw new NotYetConnectedException();
		try {
			while (true) {
				SocketClient new_connection = new SocketClient(server.accept());
				MainServerController.getInstance().connectListener(new_connection);
				this.serverPool.submit(
					() -> {
						while (true) {
							if (new_connection.getSocket().isClosed()) {
								return;
							}
							new_connection.read(MainServerController.getInstance());
						}
					}
				);
			}
		} catch (IOException e) {
			try {
				this.server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(-1);
		}
	}

	private void cleanUp(){
		try {
			Registry registry = LocateRegistry.getRegistry();
			registry.unbind("galaxy_truckers");
			this.server.close();
			//Exceptions dont matter in a shutdown hook.
		} catch (AccessException e) {
		} catch (RemoteException e) {
		} catch (NotBoundException e) {
		} catch (IOException e) {
		}
	}

	public VirtualServer accept(RMIClientConnection client) throws RemoteException {
		ClientDescriptor new_client = MainServerController.getInstance().connectListener(client);
		try {
			return MainServerController.getInstance().getStub(new_client);
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
