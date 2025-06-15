package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;

/**
 * Formatter that displays all info regarding a provided {@link ClientVerifyState}.
 */
public class ClientVerifyStateFormatter {

	private static final String bottom_line = "━Typed line:━";

	static public void format(TerminalWrapper terminal, ClientVerifyState state, PlayerColor color) {
		ArrayList<ClientVerifyPlayer> list = new ArrayList<>(state.getPlayerList());
		ClientVerifyPlayer p = list.stream().filter(pl -> pl.getColor() == color).findAny().orElse(null);
		if (p != null)
			terminal.print(ClientSpaceShipFormatter.formatLarge(p.getShip(), p.getUsername(), p.getColor(), 0, false, p.isDisconnected()), 2, 5);
		else terminal.print(ClientSpaceShipFormatter.getEmptyShipLarge(), 2, 4);
		list.remove(p);
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, p.isDisconnected()), 3, 60);
			list.removeFirst();
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 60);
		}
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, p.isDisconnected()), 11, 60);
			list.removeFirst();
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 11, 60);
		}
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, p.isDisconnected()), 3, 94);
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 94);
		}
		if (color != PlayerColor.NONE || list.isEmpty())
			terminal.print(ClientSpaceShipFormatter.getHelpCorner(), 11, 94);
		else
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, p.isDisconnected()), 11, 94);
	}

	static public void formatStatus(TerminalWrapper terminal, ClientVerifyState state) {
		terminal.print(" ".repeat(128), 29, 0);
		terminal.print(getMissingLine(state).toAnsi(), 29, 0);
		terminal.print(" ".repeat(128), 30, 0);
		terminal.print(bottom_line + "━".repeat(128 - bottom_line.length()), 30, 0);
		terminal.print(" ".repeat(128), 31, 0);
		terminal.print(terminal.peekInput(), 31, 0);
	}


	static private AttributedString getMissingLine(ClientVerifyState state) {
		AttributedStringBuilder res = new AttributedStringBuilder();
		res.style(AttributedStyle.BOLD.foreground(AttributedStyle.MAGENTA))
				.append("Still setupping: ");
		ArrayList<ClientVerifyPlayer> list = new ArrayList<>(state.getPlayerList());
		list.stream().sorted((p1, p2) -> Integer.compare(p1.getOrder(), p2.getOrder()));
		for (ClientVerifyPlayer p : list) {
			if (p.hasProgressed()) continue;
			res.style(AttributedStyle.BOLD.foreground(getColor(p.getColor())))
					.append(p.getColor().toString())
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.MAGENTA))
					.append(" | ");
		}
		res.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
				.append("| Current finishing order: ");
		list.stream().sorted((p1, p2) -> Integer.compare(p1.getOrder(), p2.getOrder()));
		boolean ongoing = true;
		for (ClientVerifyPlayer p : list) {
			if (!p.isValid()) continue;
			ongoing = false;
			res.style(AttributedStyle.BOLD.foreground(getColor(p.getColor())))
					.append(p.getColor().toString())
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
					.append(" | ");
		}
		if (ongoing) res.append("none yet. | ");
		return res.toAttributedString();
	}

	static private int getColor(PlayerColor color) {
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
