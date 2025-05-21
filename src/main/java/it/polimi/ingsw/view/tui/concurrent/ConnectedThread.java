package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;

public class ConnectedThread extends Thread {
    
    private final TUIView view;

    public ConnectedThread(TUIView view){
        this.view = view; 
    }

    @Override
    public void run(){
        CommandPreprocessor cb = new CommandPreprocessor(view);
        while(view.connected()){
            cb.process(view.takeLine());
        }
    }

}
