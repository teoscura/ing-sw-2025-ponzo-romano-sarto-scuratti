package it.polimi.ingsw.view.tui.strategy;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TUIInGameStrategy extends TUIStrategy {

    private final CommandPreprocessor cp;

    public TUIInGameStrategy(TUIView view, ConnectedState state){
        super(view);
        this.cp = new CommandPreprocessor(view, state);
    }

    @Override
    public void handleLine(String s) {
		cp.process(s);
	}

    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> {};
    }
    
}
