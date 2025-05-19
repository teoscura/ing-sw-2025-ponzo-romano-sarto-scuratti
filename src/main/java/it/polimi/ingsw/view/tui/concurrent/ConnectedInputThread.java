package it.polimi.ingsw.view.tui.concurrent;

import org.jline.reader.Widget;

import it.polimi.ingsw.view.tui.CommandBuilder;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectedInputThread extends Thread {

    private final TerminalWrapper terminal;
    private final TUIView view;

    public ConnectedInputThread(TerminalWrapper terminal, TUIView view){
        this.terminal = terminal;
        this.view = view;
    }

    @Override
    public void run(){
        CommandBuilder cb = new CommandBuilder(view);
        while(true){
            while(!terminal.isAvailable()){
                Widget s = terminal.readBinding(1000);
                if(s!=null) s.apply();
            }
            String s = terminal.takeInput();
            view.setInput(cb.build(s));
            view.redraw();
        }
    }

}
