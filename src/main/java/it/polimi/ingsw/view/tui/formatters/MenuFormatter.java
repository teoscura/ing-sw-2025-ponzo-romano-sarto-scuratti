package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.view.tui.TerminalWrapper;

public class MenuFormatter {
    
    static private ArrayList<String> titlescreen = new ArrayList<>() {{
        add(".·:''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''':·.");
        add(": :    ___      _                    _____                 _                  : :");
        add(": :   / _ \\__ _| | __ ___  ___   _  /__   \\_ __ _   _  ___| | _____ _ __ ___  : :");
        add(": :  / /_\\/ _` | |/ _` \\ \\/ / | | |   / /\\/ '__| | | |/ __| |/ / _ \\ '__/ __| : :");
        add(": : / /_\\\\ (_| | | (_| |>  <| |_| |  / /  | |  | |_| | (__|   <  __/ |  \\__ \\ : :");
        add(": : \\____/\\__,_|_|\\__,_/_/\\_\\\\__, |  \\/   |_|   \\__,_|\\___|_|\\_\\___|_|  |___/ : :");
        add(": :                          |___/                                            : :");
        add("'·:...........................................................................:·'");
        add("");
        add("");
        add("");
        add("╭─────── Enter Username ───────╮");
        add("│                              │");
        add("╰──────────────────────────────╯");
	}};

    static private ArrayList<String> conncscreen = new ArrayList<>() {{
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

    static public void title(TerminalWrapper terminal) {
		terminal.puts(Capability.clear_screen);
		String current_input = terminal.peekInput();
		String shown = normalize(current_input, 30);
		titlescreen.set(titlescreen.size() - 2, "│" + shown + "│");
		terminal.printCentered(titlescreen);
	}

    static public void connection(TerminalWrapper terminal, ArrayList<String> args) {
		terminal.puts(Capability.clear_screen);
		int i = 0;
		for (var s : args) {
			String shown = normalize(s, 23);
			conncscreen.set(conncscreen.size() - 2 * (3 - i), "│" + shown + "│");
			i++;
		}
		if (args.size() < 3) {
			int index = 2 * (3 - args.size());
			String current_input = terminal.peekInput();
			String shown = normalize(current_input, 23);
			conncscreen.set(conncscreen.size() - index, "│" + shown + "│");
		}
		terminal.printCentered(conncscreen);
	}

    static private String normalize(String input, int length) {
		return input.length() > length ? input.substring(input.length() - length) : String.format("%1$-"+length+"s", input);
	}

}
