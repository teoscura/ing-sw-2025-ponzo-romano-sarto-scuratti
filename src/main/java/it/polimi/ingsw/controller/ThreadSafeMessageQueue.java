package it.polimi.ingsw.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.polimi.ingsw.message.Message;

//x fil: questa classe e' solo un wrapper che permette a chi manda messaggi di ritornare subito, e' quindi un 
// wrapper di arrayblockingqueue che permette ritorni asincroni.
public class ThreadSafeMessageQueue<T extends Message> {
    
    private final ExecutorService threadpool;
    private final ArrayBlockingQueue<T> queue;

    public ThreadSafeMessageQueue(int size){
        threadpool = new ThreadPoolExecutor(1, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        queue = new ArrayBlockingQueue<>(size, true);
    }

    public T take() throws InterruptedException{
        return queue.take();
    }

    public void insert(T item) {
        threadpool.submit(()->{
            queue.add(item);
        });
	}
}
