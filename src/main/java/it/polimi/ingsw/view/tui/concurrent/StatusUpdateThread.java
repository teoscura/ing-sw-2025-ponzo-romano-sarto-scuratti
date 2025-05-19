package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.TUIView;

public class StatusUpdateThread extends Thread {
    
    private final TUIView view;

    public StatusUpdateThread(TUIView view){
        this.view = view;
    }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                view.showTextMessage("Status update thread got interrupted!");
            }
            view.getStatusRunnable().run();
        }
    }

}
