package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;

/**
 * Formatter that displays all info regarding a provided {@link ClientLobbySelectState} or {@link ClientSetupState}.
 */
public class ClientLobbyStatesFormatter {

	private static final String bottom_line = "━Typed line:━";

	public static void format(TerminalWrapper terminal, ClientLobbySelectState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add(state.getLobbyList().size() > 0 ? "Available lobbies:" : "No lobbies open.");
		for (ClientGameListEntry e : state.getLobbyList()) {
			boolean full = e.getCount().getNumber() == e.getPlayers().size();
			AttributedStringBuilder t = new AttributedStringBuilder()
					.style(AttributedStyle.BOLD.foreground(full ? AttributedStyle.BLUE : AttributedStyle.GREEN))
					.append(Integer.toString(e.getModelId()))
					.style(AttributedStyle.DEFAULT)
					.append(" - ")
					.style(AttributedStyle.BOLD.foreground(e.getType() == GameModeType.LVL2 ? AttributedStyle.RED : AttributedStyle.GREEN))
					.append(e.getType().toString())
					.style(AttributedStyle.BOLD.foreground(full ? AttributedStyle.BLUE : AttributedStyle.GREEN))
					.append("| " + e.getPlayers().size() + "/" + e.getCount().getNumber())
					.style(AttributedStyle.DEFAULT)
					.append(" - "+e.getState() + " | ");
			for (String s : e.getPlayers()) {
				t.append(s + " ");
			}
			res.add(t.toAttributedString().toAnsi());
		}
		terminal.printCentered(res);
	}

	public static void formatStatus(TerminalWrapper terminal, ClientLobbySelectState state) {
		terminal.printBottom(" ".repeat(terminal.getCols()), 1);
		terminal.printBottom(" ".repeat(terminal.getCols()), 0);
		terminal.printBottom(new AttributedStringBuilder().append(bottom_line + "━".repeat(terminal.getCols() - bottom_line.length())).toAttributedString().toAnsi(), 1);
		terminal.printBottom(terminal.peekInput(), 0);
	}

	public static void format(TerminalWrapper terminal, ClientSetupState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add(state.getUnfinishedList().size() > 0 ? "Unfinished games available:" : "No unfinished games available.");
		for (ClientGameListEntry e : state.getUnfinishedList()) {
			AttributedStringBuilder t = new AttributedStringBuilder()
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
					.append("" + e.getModelId())
					.style(AttributedStyle.DEFAULT)
					.append(" - ")
					.style(AttributedStyle.BOLD.foreground(e.getType() == GameModeType.LVL2 ? AttributedStyle.RED : AttributedStyle.GREEN))
					.append(e.getType().toString())
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
					.append("| " + e.getCount())
					.style(AttributedStyle.DEFAULT)
					.append(" - "+e.getState());
			for (String s : e.getPlayers()) {
				t.append(s + " ");
			}
			res.add(t.toAttributedString().toAnsi());
		}
		terminal.printCentered(res);
	}

	public static void formatStatus(TerminalWrapper terminal, ClientSetupState state) {
		terminal.printBottom(" ".repeat(128), 1);
		terminal.printBottom(" ".repeat(128), 0);
		terminal.printBottom(new AttributedStringBuilder().append(bottom_line + "━".repeat(128 - bottom_line.length())).toAttributedString().toAnsi(), 1);
		terminal.printBottom(terminal.peekInput(), 0);
	}

}
