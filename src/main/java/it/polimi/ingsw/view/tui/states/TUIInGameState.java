package it.polimi.ingsw.view.tui.states;

import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TUIInGameState extends TUIState {

    public TUIInGameState(TUIView view){
        super(view);
    }

    @Override
    public void handleLine(String s) {
		CommandPreprocessor cb = new CommandPreprocessor(view);
		cb.process(s);
	}

    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> {};
    }
    
}
