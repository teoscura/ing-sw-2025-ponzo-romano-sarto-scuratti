package it.polimi.ingsw.view.tui.strategy;

import java.util.ArrayList;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import it.polimi.ingsw.view.tui.formatters.MenuFormatter;

/**
 * Strategy to be used when the title screen is displayed.
 */
public class TUITitleStrategy extends TUIStrategy {

    private final TitleScreenState state;
    private final ArrayList<String> titlescreen;

    /**
     * Constructs a {@link TUITitleStrategy} object.
     * 
     * @param view {@link TUIView} View to which this object is bound.
     * @param state {@link TitleScreenState} State to which this object is bound.
     */
    public TUITitleStrategy(TUIView view, TitleScreenState state){
        super(view);
        this.state = state;
        this.titlescreen = new ArrayList<>() {{
            add(".·:'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''':·.");
            add(": :  ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗  : :");
            add(": : ██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗ : :");
            add(": : ██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████╔╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝ : :");
            add(": : ██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗ : :");
            add(": : ╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║ : :");
            add(": :  ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝ : :");
            add("'·:..................................................................................................................:·'");
            add("");
            add("");
            add("");
            add("╭─────── Enter Username ───────╮");
            add("│                              │");
            add("╰──────────────────────────────╯");
        }};
    }

    /**
     * Sets the player username according to the input line.
     * 
     * @param s Username input.
     */
    @Override
    public void handleLine(String s) {
        state.setUsername(s);
    }

    /**
     * @return Runnable that displays the current state of the Title Screen.
     */
    @Override
    public Runnable getRunnable(TerminalWrapper terminal) {
        return () -> MenuFormatter.title(terminal, titlescreen);
    }
    
}

/*

 */
