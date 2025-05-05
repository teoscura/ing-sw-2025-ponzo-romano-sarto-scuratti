package it.polimi.ingsw.controller.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.controller.client.RMIClientStub;
import it.polimi.ingsw.controller.server.rmi.RMISkeletonProvider;
import it.polimi.ingsw.controller.server.rmi.RemoteServer;

public class Server extends Thread implements RMISkeletonProvider {

	private final ExecutorService serverPool;
	private String ip = "localhost";

	public Server() {
		this.serverPool = new ThreadPoolExecutor(6, 60, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>(true));
	}

	@Override
	public void run() {
		System.out.println("Starting server on localhost.");
		System.out.println("Setting up RMI.");
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(9999);
			registry.rebind("galaxy_truckers", this);
			UnicastRemoteObject.exportObject(this, 9999);
		} catch (RemoteException e) {
			throw new RuntimeException("Failed to setup the rmi registry and remote object, terminating.");
		}
		System.out.println("Finished setting up RMI.");
		ServerSocket server = null;
		try {
			server = new ServerSocket();
		} catch (IOException e) {
			throw new RuntimeException("Failed to setup the server socket, terminating.");
		}
		try {
			server.bind(new InetSocketAddress(this.ip, 10000));
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
				server.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
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
