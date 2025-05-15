package it.polimi.ingsw.view.commandbuilder;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConnectTask extends Thread {

	private final ConnectingState state;

	public ConnectTask(ConnectingState state) {
		this.state = state;
	}

	public void run() {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!r.ready()) {
			}
			String s = r.readLine();
			var arr = s.split(" ");
			if (arr.length != 3) {
				this.state.getController().reset();
				return;
			}
			ConnectionType t = arr[2].equals("rmi") ? ConnectionType.RMI : ConnectionType.SOCKET;
			state.connect(arr[0], Integer.parseInt(arr[1]), t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}