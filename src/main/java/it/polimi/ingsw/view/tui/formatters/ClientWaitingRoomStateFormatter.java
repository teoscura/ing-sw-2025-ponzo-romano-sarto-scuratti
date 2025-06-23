package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

import java.util.ArrayList;

/**
 * Formatter that displays all info regarding a provided {@link ClientWaitingRoomState}.
 */
public class ClientWaitingRoomStateFormatter {

	private static final String bottom_line = "━Typed line:━";

	public static void format(TerminalWrapper terminal, ClientWaitingRoomState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("Waiting Room");
		res.add("Game type: " + state.getType() + " | Size: " + state.getCount());

		for (var e : state.getPlayerList()) {
			res.add(e.getColor() + ": " + e.getUsername());
		}
		int i = state.getCount().getNumber() - state.getPlayerList().size();
		while (i > 0) {
			res.add("..");
			i--;
		}
		terminal.printCentered(res);
	}

	public static void formatStatus(TerminalWrapper terminal, ClientWaitingRoomState state) {
		terminal.printBottom(" ".repeat(terminal.getCols()), 1);
		terminal.printBottom(" ".repeat(terminal.getCols()), 0);
		terminal.printBottom(bottom_line + "━".repeat(terminal.getCols() - bottom_line.length()), 1);
		terminal.printBottom(terminal.peekInput(), 0);
	}

}
