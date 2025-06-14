package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.view.tui.TerminalWrapper;

/**
 * Formatter that displays all pre-connection menu screens.
 */
public class MenuFormatter {

    static public void title(TerminalWrapper terminal, ArrayList<String> titlescreen) {
		terminal.puts(Capability.clear_screen);
		String current_input = terminal.peekInput();
		String shown = normalize(current_input, 30);
		titlescreen.set(titlescreen.size() - 2, "│" + shown + "│");
		terminal.printCentered(titlescreen);
	}

    static public void connection(TerminalWrapper terminal, ArrayList<String> args, ArrayList<String> conncscreen) {
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
