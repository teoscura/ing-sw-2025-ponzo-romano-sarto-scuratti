package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

import java.util.ArrayList;

public class ClientWaitingStateFormatter {

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
		terminal.print(" ".repeat(128), 30, 0);
		terminal.print(" ".repeat(128), 31, 0);
		terminal.print(bottom_line + "━".repeat(128 - bottom_line.length()), 30, 0);
		terminal.print(terminal.peekInput(), 31, 0);
	}

}
