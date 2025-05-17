package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectingThread extends Thread {
    
    private final TerminalWrapper terminal;
    private final ConnectingState state;
    private final ArrayList<String> screen;

    public ConnectingThread(TerminalWrapper terminal, ConnectingState state){
        if (terminal == null || state==null) throw new NullPointerException();
        this.terminal = terminal;
        this.state = state;
    }



}
