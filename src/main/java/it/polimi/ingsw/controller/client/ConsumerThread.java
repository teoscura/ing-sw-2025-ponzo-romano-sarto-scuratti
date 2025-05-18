package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.client.ClientMessage;

public class ConsumerThread extends Thread {

	private final ConnectedState state;
	private final ThreadSafeMessageQueue<ClientMessage> inqueue;

	public ConsumerThread(ConnectedState state, ThreadSafeMessageQueue<ClientMessage> inqueue) {
		if (state == null || inqueue == null) throw new NullPointerException();
		this.state = state;
		this.inqueue = inqueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				inqueue.take().receive(state);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
