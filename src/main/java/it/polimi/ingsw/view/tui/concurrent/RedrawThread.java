package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.TUIView;

public class RedrawThread extends Thread {
    
    private final TUIView view;

    public RedrawThread(TUIView view){
        this.view = view;
    }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                view.showTextMessage("Redraw thread got interrupted!");
            }
            view.redraw();
        }
    }

}
