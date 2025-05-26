package it.polimi.ingsw.view.tui.states;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import it.polimi.ingsw.view.tui.formatters.MenuFormatter;

public class TUITitleState extends TUIState {

    private final TitleScreenState state;

    public TUITitleState(TUIView view, TitleScreenState state){
        super(view);
        this.state = state;
    }

    @Override
    public void handleLine(String s) {
        state.setUsername(s);
    }

    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> MenuFormatter.title(terminal);
    }
    
}
