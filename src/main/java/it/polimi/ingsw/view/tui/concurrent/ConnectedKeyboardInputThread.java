package it.polimi.ingsw.view.tui.concurrent;

import org.jline.reader.Widget;

import it.polimi.ingsw.view.tui.CommandPreprocessor;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ConnectedKeyboardInputThread extends Thread {

    private final TerminalWrapper terminal;
    private final TUIView view;

    public ConnectedKeyboardInputThread(TerminalWrapper terminal, TUIView view){
        this.terminal = terminal;
        this.view = view;
    }

    @Override
    public void run(){
        CommandPreprocessor cb = new CommandPreprocessor(view);
        while(true){
            while(!terminal.isAvailable()){
                Widget s = terminal.readBinding(1000);
                if(s!=null) s.apply();
            }
            String s = terminal.takeInput();
            cb.process(s);
            view.redraw();
        }
    }

}
