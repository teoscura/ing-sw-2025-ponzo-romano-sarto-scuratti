package it.polimi.ingsw.view.tui.strategy;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

/**
 * Strategy to be used when connected to a remote game server with a {@link TUIView}.
 */
public class TUIInGameStrategy extends TUIStrategy {

    private final CommandPreprocessor cp;

    /**
     * Constructs a {@link TUIInGameStrategy} object.
     * 
     * @param view {@link TUIView} View to which this object is bound.
     * @param state {@link ConnectedState} State to which this object is bound.
     */
    public TUIInGameStrategy(TUIView view, ConnectedState state){
        super(view);
        this.cp = new CommandPreprocessor(view, state);
    }

    /**
     * Processes line with a {@link CommandPreprocessor}.
     * 
     * @param s String to be processed.
     */
    @Override
    public void handleLine(String s) {
		cp.process(s);
	}

    /**
     * @returns Empty Runnable.
     */
    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> {};
    }
    
}
