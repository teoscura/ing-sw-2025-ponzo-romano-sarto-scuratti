package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.controller.client.connections.RMIClientStub;
import it.polimi.ingsw.controller.server.connections.RMISkeletonProvider;
import it.polimi.ingsw.controller.server.connections.RemoteServer;
import it.polimi.ingsw.controller.server.connections.SocketClient;

public class Server extends Thread implements RMISkeletonProvider {

	private final ExecutorService serverPool;
	private String ip = "localhost";
	private int rmiport;
	private boolean init = false;
	private ServerSocket server;

	public Server() {
		this.serverPool = new ThreadPoolExecutor(20, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
	}

	public void init(String address, int rmiport){
        if(this.init) throw new AlreadyConnectedException();
		this.ip = address;
		this.rmiport = rmiport;
        this.init = true;
    }

	@Override
	public void run() {
		if(!this.init) throw new NotYetConnectedException();
		System.out.println("Setting up RMI.");
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(this.rmiport);
			registry.rebind("galaxy_truckers", this);
			UnicastRemoteObject.exportObject(this, this.rmiport);
		} catch (RemoteException e) {
			throw new RuntimeException("Failed to setup the rmi registry and remote object, terminating.");
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
			Registry registry = LocateRegistry.createRegistry(9999);
			registry.unbind("galaxy_truckers");
			this.server.close();
			//Exceptions dont matter in a shutdown hook.
		} catch (AccessException e) {
		} catch (RemoteException e) {
		} catch (NotBoundException e) {
		} catch (IOException e) {
		}
	}

	public RemoteServer accept(RMIClientStub client) throws RemoteException {
		ClientDescriptor new_client = MainServerController.getInstance().connectListener(client);
		try {
			return MainServerController.getInstance().getStub(new_client);
		} catch (RemoteException e) {
			MainServerController.getInstance().disconnect(new_client);
			throw e;
		}
	}

}
