package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.CommandBuilder;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectedThread extends Thread {

    private final TerminalWrapper terminal;
    private final ClientView view;

    public ConnectedThread(TerminalWrapper terminal, ClientView view){
        this.terminal = terminal;
        this.view = view;
    }

    @Override
    public void run(){
        CommandBuilder cb = new CommandBuilder();
        while(true){
            while(!terminal.isAvailable()){
                terminal.readBinding().apply();
                terminal.updateStatus();
            }
            view.setInput(cb.build(terminal.takeInput()));
        }
    }

}
