package it.polimi.ingsw.view.tui.strategy;

import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public abstract class TUIStrategy {
    
    protected final TUIView view;

    protected TUIStrategy(TUIView view){
        this.view = view;
    }

    public abstract void handleLine(String s);
    public abstract Runnable getRunnable(TerminalWrapper terminal);
}
