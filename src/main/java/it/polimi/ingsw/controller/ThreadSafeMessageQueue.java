package it.polimi.ingsw.controller;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.message.Message;


public class ThreadSafeMessageQueue<T extends Message> {
    
    private final ExecutorService threadpool;
    private final Queue<T> queue;
    private final Object queue_lock;

    public ThreadSafeMessageQueue(){
        threadpool = new ThreadPoolExecutor(1, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
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
        threadpool.submit(()->{
            synchronized (queue_lock) {
			    this.queue.add(item);
		    	queue_lock.notifyAll();
		    }
        });
	}
}
