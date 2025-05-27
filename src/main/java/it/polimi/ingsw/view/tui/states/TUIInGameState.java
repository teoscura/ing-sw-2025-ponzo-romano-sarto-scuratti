package it.polimi.ingsw.view.tui.states;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TUIInGameState extends TUIState {

    private final ConnectedState state;

    public TUIInGameState(TUIView view, ConnectedState state){
        super(view);
        this.state = state;
    }

    @Override
    public void handleLine(String s) {
		CommandPreprocessor cb = new CommandPreprocessor(view, state);
		cb.process(s);
	}

    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> {};
    }
    
}
