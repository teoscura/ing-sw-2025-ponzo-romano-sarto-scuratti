package it.polimi.ingsw.controller.client;

import java.util.ArrayDeque;
import java.util.Queue;

import it.polimi.ingsw.message.client.ClientMessage;

public class ThreadSafeMessageQueue {
    
    private final Queue<ClientMessage> queue;
    private final Object queue_lock;

    public ThreadSafeMessageQueue(){
        queue = new ArrayDeque<>();
        queue_lock = new Object();
    }

    public ClientMessage poll() throws InterruptedException{
        ClientMessage mess = null;
        synchronized(queue_lock){
            while(this.queue.isEmpty()) queue_lock.wait();
            mess = this.queue.poll();
        }
        return mess;
    }

    public void receiveMessage(ClientMessage message) {
		synchronized (queue_lock) {
			this.queue.add(message);
			queue_lock.notifyAll();
		}
	}
}
