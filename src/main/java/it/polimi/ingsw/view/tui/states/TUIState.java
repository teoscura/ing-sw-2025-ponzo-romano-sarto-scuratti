package it.polimi.ingsw.view.tui.states;

import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public abstract class TUIState {
    
    protected final TUIView view;

    protected TUIState(TUIView view){
        this.view = view;
    }

    public abstract void handleLine(String s);
    public abstract Runnable getRunnable(TerminalWrapper terminal);
}
