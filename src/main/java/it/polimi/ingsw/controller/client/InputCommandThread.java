package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.view.ClientView;

public class InputCommandThread extends Thread {

	private final ConnectedState cc;
	private final ClientView view;

	public InputCommandThread(ConnectedState cc, ClientView view) {
		this.cc = cc;
		this.view = view;
	}

	public void run() {
		while (true) {
			synchronized(view.getLock()){
				while(!view.inputAvailable()){
					try {
						view.getLock().wait();
					} catch (InterruptedException e) {
						view.showTextMessage("Shutdown of input thread!");
					}
				}
				cc.sendMessage(view.getInput());
			}
		}
	}

}
