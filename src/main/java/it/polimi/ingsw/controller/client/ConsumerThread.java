package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.ThreadSafeMessageQueue;
import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.client.ClientMessage;

/**
 * Thread object in charge of retrieving all received messages from the queue and processing them.
 */
public class ConsumerThread extends Thread {

	private final ConnectedState state;
	private final ThreadSafeMessageQueue<ClientMessage> inqueue;

	/**
	 * Construct a {@link ConsumerThread} object.
	 * 
	 * @param state {@link ConnectedState} Client Controller State to which this Thread object is bound.
	 * @param inqueue {@link ThreadSafeMessageQueue} Queue containing all received messages.
	 */
	public ConsumerThread(ConnectedState state, ThreadSafeMessageQueue<ClientMessage> inqueue) {
		if (state == null || inqueue == null) throw new NullPointerException();
		this.state = state;
		this.inqueue = inqueue;
	}

	/**
	 * Main loop for the ConsumerThread object, takes messages from the queue and processes them.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				inqueue.take().receive(state);
			} catch (InterruptedException e) {
				state.getView().showTextMessage("Interrupted Consumer Thread!");
				inqueue.dump();
				return;
			}
		}
	}

}
