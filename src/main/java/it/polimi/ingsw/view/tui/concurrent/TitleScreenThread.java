package it.polimi.ingsw.view.tui.concurrent;

import java.util.ArrayList;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TitleScreenThread extends Thread {

    private final TerminalWrapper terminal;
    private final TitleScreenState state;
    private final ArrayList<String> title;

    public TitleScreenThread(TerminalWrapper terminal, TitleScreenState state){
        if (terminal == null || state==null) throw new NullPointerException();
        this.terminal = terminal;
        this.state = state;
        this.title = new ArrayList<>(){{
            add(".·:\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\'\':·.");
            add(": :    ___      _                    _____                 _                  : :");
            add(": :   / _ \\__ _| | __ ___  ___   _  /__   \\_ __ _   _  ___| | _____ _ __ ___  : :");
            add(": :  / /_\\/ _` | |/ _` \\ \\/ / | | |   / /\\/ \'__| | | |/ __| |/ / _ \\ \'__/ __| : :");
            add(": : / /_\\\\ (_| | | (_| |>  <| |_| |  / /  | |  | |_| | (__|   <  __/ |  \\__ \\ : :");
            add(": : \\____/\\__,_|_|\\__,_/_/\\_\\\\__, |  \\/   |_|   \\__,_|\\___|_|\\_\\___|_|  |___/ : :");
            add(": :                          |___/                                            : :");
            add("\'·:...........................................................................:·\'");
            add("");
            add("╭─────── Enter Username ───────╮");
            add("│                              │");
            add("╰──────────────────────────────╯");
        }};
    }

    public void run(){
        //Show title screen
        //Show username prompt,
        //Insert prompt when ready.
        terminal.printCentered(title);
        while(!terminal.isAvailable()){
            //Draw update
            terminal.readBinding().apply();
        }
        state.setUsername(terminal.takeInput());
    }

    private void title(){
        //Ascii art and text box centered.

    }















}
