package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ConnectedState;

public class ConsumerThread extends Thread {

    private final ConnectedState state;
    private final ThreadSafeMessageQueue inqueue;

    public ConsumerThread(ConnectedState state, ThreadSafeMessageQueue inqueue){
        if(state == null || inqueue == null) throw new NullPointerException();
        this.state = state;
        this.inqueue = inqueue;
    }

    @Override
    public void run(){
        while (true) {
            try {
                inqueue.poll().receive(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
    }

}
