package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.tui.TUIView;

import java.util.ArrayList;

public class ConnectingThread extends Thread {

	private final ConnectingState state;
	private final TUIView view;
	private final ArrayList<String> args;

	public ConnectingThread(ConnectingState state, TUIView view) {
		if (state == null) throw new NullPointerException();
		this.view = view;
		this.state = state;
		this.args = new ArrayList<>();
	}

	@Override
	public void run() {
		while (args.size() < 3) {
			try {
				args.add(view.takeLine());
			} catch (InterruptedException e) {
				view.showTextMessage("Interrupted connecting thread.");
			}
		}
		if (!validate()) state.connect("", 0, ConnectionType.NONE);
		else
			state.connect(args.get(0), Integer.parseInt(args.get(1)), args.get(2).equals("rmi") ? ConnectionType.RMI : args.get(2).equals("tcp") ? ConnectionType.SOCKET : ConnectionType.NONE);
	}

	public ArrayList<String> getArgs(){
		return new ArrayList<>(args);
	}

	private boolean validate() {
		try {
			Integer.parseInt(args.get(1));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
