package it.polimi.ingsw.view.tui.strategy;

import java.util.ArrayList;

import it.polimi.ingsw.controller.client.connections.ConnectionType;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import it.polimi.ingsw.view.tui.formatters.MenuFormatter;

/**
 * Strategy to be used when connecting to a remote game server with a {@link TUIView}.
 */
public class TUIConnectionSetupStrategy extends TUIStrategy {
    
    private final ArrayList<String> args;
    private final ConnectingState state;
    private final ArrayList<String> screen;

    /**
     * Constructs a {@link TUIConnectionSetupStrategy} object.
     * 
     * @param view {@link TUIView} View to which this object is bound.
     * @param state {@link ConnectingState} State to which this object is bound.
     */
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

    /**
     * Adds the input line to the connect command args.
     * 
     * @param s String to be parsed as an argument.
     */
    @Override
    public void handleLine(String s) {
        args.add(s);
        if(args.size()<3) return;
        if (!validate()) state.connect("", 0, ConnectionType.NONE);
		else
			state.connect(args.get(0), Integer.parseInt(args.get(1)), args.get(2).equals("rmi") ? ConnectionType.RMI : args.get(2).equals("tcp") ? ConnectionType.SOCKET : ConnectionType.NONE);
	
    }

    /**
     * @return Runnable that displays the current state of the Connection Setup screen.
     */
    public Runnable getRunnable(TerminalWrapper terminal){
        return () -> MenuFormatter.connection(terminal, args, screen);
    }
    
    /**
     * @return Whether the args the user as set are valid.
     */
	private boolean validate() {
		try {
			var i = Integer.parseInt(args.get(1));
            if(i<0||i>=65535) return false;
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
    
}
