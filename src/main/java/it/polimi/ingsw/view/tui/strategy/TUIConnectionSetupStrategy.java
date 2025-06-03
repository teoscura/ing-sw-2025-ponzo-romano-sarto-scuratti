package it.polimi.ingsw.view.tui.strategy;

import java.util.ArrayList;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import it.polimi.ingsw.view.tui.formatters.MenuFormatter;

public class TUIConnectionSetupStrategy extends TUIStrategy {
    
    private final ArrayList<String> args;
    private final ConnectingState state;
    private final ArrayList<String> screen;

    public TUIConnectionSetupStrategy(TUIView view, ConnectingState state){
        super(view);
        this.state = state;
        this.args = new ArrayList<>();
        this.screen = new ArrayList<>() {{
            add("Connection setup...");
            add("");
            add("╭─────── Address ───────╮");
            add("│                       │");
            add("│──────── Port: ────────│");
            add("│                       │");
            add("│───── 'tcp'|'rmi' ─────│");
            add("│                       │");
            add("╰───────────────────────╯");
        }};
    }

    @Override
    public void handleLine(String s) {
        args.add(s);
        if(args.size()<3) return;
        if (!validate()) state.connect("", 0, ConnectionType.NONE);
		else
			state.connect(args.get(0), Integer.parseInt(args.get(1)), args.get(2).equals("rmi") ? ConnectionType.RMI : args.get(2).equals("tcp") ? ConnectionType.SOCKET : ConnectionType.NONE);
	
    }

    public Runnable getRunnable(TerminalWrapper terminal){
        return () -> MenuFormatter.connection(terminal, args, screen);
    }
    
	private boolean validate() {
		try {
			Integer.parseInt(args.get(1));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
    
}
