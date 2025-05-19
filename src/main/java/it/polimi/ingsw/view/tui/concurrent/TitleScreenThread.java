package it.polimi.ingsw.view.tui.concurrent;

import java.util.ArrayList;

import org.jline.reader.Widget;
import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TitleScreenThread extends Thread {

    private final TerminalWrapper terminal;
    private final TitleScreenState state;
    private final ArrayList<String> screen;

    public TitleScreenThread(TerminalWrapper terminal, TitleScreenState state){
        if (terminal == null || state==null) throw new NullPointerException();
        this.terminal = terminal;
        this.state = state;
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
        //Show username prompt,
        //Insert prompt when ready.
        screen();
        while(!terminal.isAvailable()){
            screen();
            Widget s = terminal.readBinding();
            if(s!=null) s.apply();
        }
        state.setUsername(terminal.takeInput());
    }

    private void screen(){
        terminal.puts(Capability.clear_screen);
        String current_input = terminal.peekInput();
        String shown = current_input.length() > 30 ? current_input.substring(current_input.length()-30) : String.format("%1$-30s", current_input);
        this.screen.set(screen.size()-2,"│"+shown+"│");
        terminal.printCentered(screen);
        //Ascii art and text box centered.

    }















}
