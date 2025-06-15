package it.polimi.ingsw.view.tui.strategy;

import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

/**
 * Abstract class representing how a {@link TUIView} user inputs should be handled and displayed.
 */
public abstract class TUIStrategy {
    
    protected final TUIView view;

    /**
     * Constructs a {@link TUIStrategy} object.
     * 
     * @param view {@link TUIView} View to which this object is bound.
     */
    protected TUIStrategy(TUIView view){
        this.view = view;
    }
    
    public abstract void handleLine(String s);
    public abstract Runnable getRunnable(TerminalWrapper terminal);
}
