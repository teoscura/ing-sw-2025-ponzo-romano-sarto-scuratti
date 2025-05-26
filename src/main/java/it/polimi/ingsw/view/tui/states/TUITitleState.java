package it.polimi.ingsw.view.tui.states;

import java.util.ArrayList;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import it.polimi.ingsw.view.tui.formatters.MenuFormatter;

public class TUITitleState extends TUIState {

    private final TitleScreenState state;
    private final ArrayList<String> titlescreen;

    public TUITitleState(TUIView view, TitleScreenState state){
        super(view);
        this.state = state;
        this.titlescreen = new ArrayList<>() {{
            add(".·:''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''':·.");
            add(": :    ___      _                    _____                 _                  : :");
            add(": :   / _ \\__ _| | __ ___  ___   _  /__   \\_ __ _   _  ___| | _____ _ __ ___  : :");
            add(": :  / /_\\/ _` | |/ _` \\ \\/ / | | |   / /\\/ '__| | | |/ __| |/ / _ \\ '__/ __| : :");
            add(": : / /_\\\\ (_| | | (_| |>  <| |_| |  / /  | |  | |_| | (__|   <  __/ |  \\__ \\ : :");
            add(": : \\____/\\__,_|_|\\__,_/_/\\_\\\\__, |  \\/   |_|   \\__,_|\\___|_|\\_\\___|_|  |___/ : :");
            add(": :                          |___/                                            : :");
            add("'·:...........................................................................:·'");
            add("");
            add("");
            add("");
            add("╭─────── Enter Username ───────╮");
            add("│                              │");
            add("╰──────────────────────────────╯");
        }};
    }

    @Override
    public void handleLine(String s) {
        state.setUsername(s);
    }

    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> MenuFormatter.title(terminal, titlescreen);
    }
    
}
