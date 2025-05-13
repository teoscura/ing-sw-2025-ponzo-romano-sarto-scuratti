package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.Message;

public class AsyncInsertTask<T extends Message> extends Thread {
    
    private final ThreadSafeMessageQueue<T> queue;
    private final T message;

    public AsyncInsertTask(ThreadSafeMessageQueue<T> queue, T mess){
        if(queue == null || mess == null) throw new NullPointerException();
        this.queue = queue;
        this.message = mess;
    }   

    @Override
    public void run(){
        this.queue.insert(message);
    }

}
