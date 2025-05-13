package it.polimi.ingsw.controller;

import java.util.ArrayDeque;
import java.util.Queue;

import it.polimi.ingsw.message.Message;


public class ThreadSafeMessageQueue<T extends Message> {
    
    private final Queue<T> queue;
    private final Object queue_lock;

    public ThreadSafeMessageQueue(){
        queue = new ArrayDeque<>();
        queue_lock = new Object();
    }

    public T poll() throws InterruptedException{
        T item = null;
        synchronized(queue_lock){
            while(this.queue.isEmpty()) queue_lock.wait();
            item = this.queue.poll();
        }
        return item;
    }

    public void insert(T item) {
		synchronized (queue_lock) {
			this.queue.add(item);
			queue_lock.notifyAll();
		}
	}
}
