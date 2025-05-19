package it.polimi.ingsw.view.tui.concurrent;

import java.util.ArrayList;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectingThread extends Thread {
    
    private final TerminalWrapper terminal;
    private final ConnectingState state;
    private final ArrayList<String> screen;
    private final ArrayList<String> args;

    public ConnectingThread(TerminalWrapper terminal, ConnectingState state){
        if (terminal == null || state==null) throw new NullPointerException();
        this.terminal = terminal;
        this.state = state;
        this.args = new ArrayList<>();
        this.screen = new ArrayList<>(){{
            add("Connection setup...");
            add("");
            add("╭─────── Address ───────╮");
            add("│                       │");
            add("│──────── Port: ────────│");
            add("│                       │");
            add("│───── \"tcp\"|\"rmi\" ─────│");
            add("│                       │");
            add("╰───────────────────────╯");
        }};
    }

    @Override
    public void run(){
        while(!terminal.isAvailable()){
            screen();
            terminal.readBinding().apply();
        }
        args.add(terminal.takeInput());
        while(!terminal.isAvailable()){
            screen();
            terminal.readBinding().apply();
        }
        args.add(terminal.takeInput());
        while(!terminal.isAvailable()){
            screen();
            terminal.readBinding().apply();
        }
        args.add(terminal.takeInput());
        if(!validate()) state.connect("", 0, ConnectionType.NONE);
        else state.connect(args.get(0), Integer.parseInt(args.get(1)), args.get(2).equals("rmi") ? ConnectionType.RMI :args.get(2).equals("tcp") ? ConnectionType.SOCKET : ConnectionType.NONE);
    }

    private void screen(){
        terminal.puts(Capability.clear_screen);
        int i = 0;
        for(var s : args){
            String shown = normalize(s);
            this.screen.set(screen.size()-2*(3-i),"│"+shown+"│");
            i++;
        }
        int index = 2*(3-args.size());
        String current_input = terminal.peekInput();
        String shown = normalize(current_input);
        this.screen.set(screen.size()-index,"│"+shown+"│");
        terminal.printCentered(screen);
    }

    private boolean validate(){
        try{
            Integer.parseInt(args.get(1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String normalize(String input){
        return input.length() > 23 ? input.substring(input.length()-23) : String.format("%1$-23s", input);
    }

}
