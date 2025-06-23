package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;

/**
 * Formatter that displays all info regarding a provided {@link ClientEndgameState}.
 */
public class ClientEndgameStateFormatter {

	private static final String bottom_line = "━Typed line:━";

	public static void format(TerminalWrapper terminal, ClientEndgameState state) {
		ArrayList<String> res = new ArrayList<>();
		ArrayList<ClientEndgamePlayer> list = new ArrayList<>(state.getPlayerList());
		res.add("Game Results:");

		list.stream().sorted((p1, p2) -> -Integer.compare(p1.getCredits(), p2.getCredits()));
		for (var e : list) {
			AttributedStringBuilder b = new AttributedStringBuilder()
				.append(e.getUsername())
				.append(" - " + e.getColor())
				.append(e.getPlanche_slot() >= 0 ? " - #" + (e.getPlanche_slot()+1) + " - " : " - DNF - ")
				.append(String.format("🟦: %3d | ", e.getShipments()[1]))
				.append(String.format("🟩: %3d | ", e.getShipments()[2]))
				.append(String.format("🟨: %3d | ", e.getShipments()[3]))
				.append(String.format("🟥: %3d | ", e.getShipments()[4]))
				.append(String.format("💰: %3d | " + (e.getPlanche_slot() == -1 ? "retired" : " alive "), e.getCredits()));
			res.add(b.toAnsi());
		}
		res.add("");
		AttributedStringBuilder line = new AttributedStringBuilder()
			.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(AttributedStyle.CYAN))
			.append("Awaiting confirm from: ");
		for (PlayerColor c : state.awaiting()) {
			line.style(AttributedStyle.BOLD.background(AttributedStyle.BLACK).foreground(getColor(c)))
				.append(c.toString())
				.style(AttributedStyle.DEFAULT)
				.append(" | ");
		}
		res.add(line.toAnsi());
		terminal.printCentered(res);
	}

	public static void formatStatus(TerminalWrapper terminal, ClientEndgameState state) {
		terminal.printBottom(" ".repeat(terminal.getCols()), 1);
		terminal.printBottom(" ".repeat(terminal.getCols()), 0);
		terminal.printBottom(bottom_line + "━".repeat(terminal.getCols() - bottom_line.length()), 1);
		terminal.printBottom(terminal.peekInput(), 0);
	}

 	private static int getColor(PlayerColor color) {
		switch (color) {
			case BLUE:
				return AttributedStyle.BLUE;
			case GREEN:
				return AttributedStyle.GREEN;
			case RED:
				return AttributedStyle.RED;
			case YELLOW:
				return AttributedStyle.YELLOW;
			default:
				return AttributedStyle.WHITE;
		}
	}

}
