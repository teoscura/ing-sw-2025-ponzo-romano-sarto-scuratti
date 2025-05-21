package it.polimi.ingsw.view.tui.concurrent;

import java.util.ArrayList;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TitleScreenThread extends Thread {

    private final TitleScreenState state;
    private final TUIView view;
    private final ArrayList<String> screen;

    public TitleScreenThread(TitleScreenState state, TUIView view){
        if (state==null) throw new NullPointerException();
        this.state = state;
        this.view = view;
        this.screen = new ArrayList<>(){{
            add(".·:\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\':·.");
            add(": :    ___      _                    _____                 _                  : :");
            add(": :   / _ \\__ _| | __ ___  ___   _  /__   \\_ __ _   _  ___| | _____ _ __ ___  : :");
            add(": :  / /_\\/ _` | |/ _` \\ \\/ / | | |   / /\\/ \'__| | | |/ __| |/ / _ \\ \'__/ __| : :");
            add(": : / /_\\\\ (_| | | (_| |>  <| |_| |  / /  | |  | |_| | (__|   <  __/ |  \\__ \\ : :");
            add(": : \\____/\\__,_|_|\\__,_/_/\\_\\\\__, |  \\/   |_|   \\__,_|\\___|_|\\_\\___|_|  |___/ : :");
            add(": :                          |___/                                            : :");
            add("\'·:...........................................................................:·\'");
            add("");
            add("");
            add("");
            add("╭─────── Enter Username ───────╮");
            add("│                              │");
            add("╰──────────────────────────────╯");
        }};
    }

    public void run(){
        state.setUsername(view.takeLine());
    }

    public void format(TerminalWrapper terminal){
        terminal.puts(Capability.clear_screen);
        String current_input = terminal.peekInput();
        String shown = current_input.length() > 30 ? current_input.substring(current_input.length()-30) : String.format("%1$-30s", current_input);
        this.screen.set(screen.size()-2,"│"+shown+"│");
        terminal.printCentered(screen);
    }















}
