package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.CommandBuilder;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectedThread extends Thread {

    private final TerminalWrapper terminal;
    private final TUIView view;

    public ConnectedThread(TerminalWrapper terminal, TUIView view){
        this.terminal = terminal;
        this.view = view;
    }

    @Override
    public void run(){
        CommandBuilder cb = new CommandBuilder();
        while(true){
            while(!terminal.isAvailable()){
                terminal.readBinding().apply();
                view.getClientState().sendToView(view);
            }
            String s = terminal.takeInput();
            //if(s.equals("help")) terminal.showHelpScreen();
            /*else*/ //if(s.equals("help")) view.changeShip(s);
            view.setInput(cb.build(s));
        }
    }

}
