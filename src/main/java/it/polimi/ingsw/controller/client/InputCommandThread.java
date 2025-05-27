package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.server.ServerMessage;
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
			try{
				ServerMessage mess = view.takeInput();
				cc.sendMessage(mess);
			} catch (InterruptedException e) {
				view.showTextMessage("Closing input command thread!");
				cc.disconnect();
			}
		}
	}

}
