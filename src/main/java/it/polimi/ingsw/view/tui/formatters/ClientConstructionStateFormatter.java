package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
/**
 * Formatter that displays all info regarding a provided {@link ClientConstructionState}.
 */
public class ClientConstructionStateFormatter {

	private static final String bottom_line = "━Typed line:━";

	static public void format(TerminalWrapper terminal, ClientConstructionState state, PlayerColor color) {
		ArrayList<ClientConstructionPlayer> list = new ArrayList<>(state.getPlayerList());
		ClientConstructionPlayer p = list.stream().filter(pl -> pl.getColor() == color).findAny().orElse(null);
		if (p != null)
			terminal.print(ClientSpaceShipFormatter.formatLarge(p.getShip(), p.getUsername(), p.getColor(), 0, false, p.isDisconnected()), 2, 5);
		else terminal.print(ClientSpaceShipFormatter.getEmptyShipLarge(), 2, 4);
		list.remove(p);
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, list.getFirst().isDisconnected()), 3, 60);
			list.removeFirst();
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 60);
		}
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, list.getFirst().isDisconnected()), 11, 60);
			list.removeFirst();
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 11, 60);
		}
		if (list.size() > 0) {
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, list.getFirst().isDisconnected()), 3, 94);
			list.removeFirst();
		} else {
			terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 94);
		}
		if (color != PlayerColor.NONE || list.isEmpty())
			terminal.print(ClientSpaceShipFormatter.getConstructionHelpCorner(), 11, 94);
		else
			terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false, list.getFirst().isDisconnected()), 11, 94);
		terminal.print(getUserComponents(state, color), 19, 6);
		terminal.print(getDiscardedBoard(state), 19, 24);
	}

	public static void formatStatus(TerminalWrapper terminal, ClientConstructionState state) {
		terminal.printBottom(" ".repeat(terminal.getCols()), 2);
		terminal.printBottom(" ".repeat(terminal.getCols()), 1);
		terminal.printBottom(" ".repeat(terminal.getCols()), 0);
		terminal.printBottom(getBoardLine(state).toAnsi(), 2);
		terminal.printBottom(bottom_line + "━".repeat(terminal.getCols() - bottom_line.length()), 1);
		terminal.printBottom(terminal.peekInput(), 0);
	}

	static private AttributedString getBoardLine(ClientConstructionState state) {
		AttributedStringBuilder res = new AttributedStringBuilder();
		res.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
				.append("| Center board state - Uncovered: [" + state.getTilesLeft() + "] | ");
		if (state.getType().getLevel() == 2) {
			res.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW));
			int spots = state.getTogglesTotal() - state.getTogglesLeft() - 1;
			for (int i = 0; i < spots; i++) {
				res.append("[..] ");
			}
			res.append("[⌛] ");
			for (int i = 0; i < state.getTogglesLeft(); i++) {
				res.append("[..] ");
			}
			if (state.getLastToggle() != null) {
				Duration elapsed = Duration.between(state.getLastToggle(), Instant.now());
				Duration time_left = Duration.ofMillis(state.getHourglassDuration().toMillis() - elapsed.toMillis());
				boolean has_expired = elapsed.compareTo(state.getHourglassDuration()) > 0;
				String time = has_expired ? "Expired." : "Time left: " + formatTime(time_left) + "s";
				time += has_expired && state.getTogglesLeft() > 0 ? "can still build!" : "";
				res.style(AttributedStyle.BOLD.foreground(has_expired ? state.getTogglesLeft() > 0 ? AttributedStyle.YELLOW : AttributedStyle.RED : AttributedStyle.GREEN))
						.append(time + " | ");
			}
		}
		res.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
				.append("Still building: ");
		ArrayList<ClientConstructionPlayer> list = new ArrayList<>(state.getPlayerList());
		for (ClientConstructionPlayer p : list) {
			if (p.isFinished()) continue;
			res.style(AttributedStyle.BOLD.foreground(getColor(p.getColor())))
					.append(p.getColor().toString())
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
					.append(" ");
		}
		return res.toAttributedString();
	}

	static private String formatTime(Duration duration) {
		int s = (int) duration.toSeconds();
		return String.format("%02d", s);
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

	static private List<String> getUserComponents(ClientConstructionState state, PlayerColor color) {
		ArrayList<StringBuffer> res = new ArrayList<>();
		ArrayList<ClientConstructionPlayer> list = new ArrayList<>(state.getPlayerList());
		ClientConstructionPlayer pl = list.stream().filter(p -> p.getColor() == color).findAny().orElse(null);
		if (pl == null) {
			return res.stream().map(sb -> sb.toString()).toList();
		}
		Integer current = pl.getCurrent();
		ArrayList<Integer> reserved = new ArrayList<>(pl.getReserved());
		ClientLargeComponentPrinter pr = new ClientLargeComponentPrinter();
		ClientSmallComponentPrinter ps = new ClientSmallComponentPrinter();
		res.add(new StringBuffer("╭" + " Your tiles. " + "╮"));
		for (int i = 0; i < 7; i++) {
			if (i == 3) res.add(new StringBuffer("├"));
			else res.add(new StringBuffer("│"));
		}
		//Held component
		if (current > 0) {
			ClientComponent c = new ComponentFactory().getComponent(current).getClientComponent();
			pr.reset();
			c.showComponent(ps);
			pr.setCenter(ps.getComponentStringSmall());
			c.showComponent(pr);
			var a = pr.getComponentStringsLarge();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(a.get(k));
				res.get(k + 1).append("│");
			}
			res.get(4).append("[" + String.format("%04d", current) + "]┼");
		} else {
			var a = pr.getForbidden();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(a.get(k));
				res.get(k + 1).append("│");
			}
			res.get(4).append("──────┼");
		}
		//Reserved first.
		if (reserved.size() > 0) {
			ClientComponent c = new ComponentFactory().getComponent(reserved.getFirst()).getClientComponent();
			pr.reset();
			c.showComponent(ps);
			pr.setCenter(ps.getComponentStringSmall());
			c.showComponent(pr);
			var a = pr.getComponentStringsLarge();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(a.get(k));
				res.get(k + 1).append("│");
			}
			res.get(4).append("[" + String.format("%04d", reserved.getFirst()) + "]┤");
			reserved.removeFirst();
		} else {
			var a = pr.getForbidden();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(a.get(k));
				res.get(k + 1).append("│");
			}
			res.get(4).append("──────┤");
		}
		res.get(5).append("  ^^  │");
		res.get(6).append(" curr │");
		res.get(7).append("      │");
		res.add(new StringBuffer("╰──────┴"));
		//Second reserved
		if (reserved.size() > 0) {
			ClientComponent c = new ComponentFactory().getComponent(reserved.getFirst()).getClientComponent();
			pr.reset();
			c.showComponent(ps);
			pr.setCenter(ps.getComponentStringSmall());
			c.showComponent(pr);
			var a = pr.getComponentStringsLarge();
			for (int k = 0; k < 3; k++) {
				res.get(k + 5).append(a.get(k));
				res.get(k + 5).append("│");
			}
			res.get(8).append("[" + String.format("%04d", reserved.getFirst()) + "]╯");
		} else {
			var a = pr.getForbidden();
			for (int k = 0; k < 3; k++) {
				res.get(k + 5).append(a.get(k));
				res.get(k + 5).append("│");
			}
			res.get(8).append("──────╯");
		}
		return res.stream().map(sb -> sb.toString()).toList();
	}

	static private List<String> getDiscardedBoard(ClientConstructionState state) {
		ArrayList<StringBuffer> res = new ArrayList<>();
		ArrayList<Integer> components = new ArrayList<>(state.getDiscardedTiles());
		ClientLargeComponentPrinter pr = new ClientLargeComponentPrinter();
		ClientSmallComponentPrinter ps = new ClientSmallComponentPrinter();
		res.add(new StringBuffer("╭" + "──────┬".repeat(13) + "──────" + "╮"));
		int index = (res.get(0).length() - 12) / 2;
		res.get(0).replace(index, index + 12, " Discarded. ");
		for (int i = 0; i < 3; i++) {
			res.add(new StringBuffer("│"));
		}
		res.add(new StringBuffer("├"));
		for (int i = 0; i < 13; i++) {
			if (components.size() == 0) {
				var c = pr.getForbidden();
				for (int k = 0; k < 3; k++) {
					res.get(k + 1).append(c.get(k) + "│");
				}
				res.get(4).append("──────┼");
			} else {
				int id = components.removeFirst();
				ClientComponent c = new ComponentFactory().getComponent(id).getClientComponent();
				pr.reset();
				c.showComponent(ps);
				pr.setCenter(ps.getComponentStringSmall());
				c.showComponent(pr);
				var a = pr.getComponentStringsLarge();
				for (int k = 0; k < 3; k++) {
					res.get(k + 1).append(a.get(k));
					res.get(k + 1).append("│");
				}
				res.get(4).append("[" + String.format("%04d", id) + "]┼");
			}
		}
		if (components.size() == 0) {
			var c = pr.getForbidden();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(c.get(k) + "│");
			}
			res.get(4).append("──────┤");
		} else {
			int id = components.removeFirst();
			ClientComponent c = new ComponentFactory().getComponent(id).getClientComponent();
			pr.reset();
			c.showComponent(ps);
			pr.setCenter(ps.getComponentStringSmall());
			c.showComponent(pr);
			var a = pr.getComponentStringsLarge();
			for (int k = 0; k < 3; k++) {
				res.get(k + 1).append(a.get(k));
				res.get(k + 1).append("│");
			}
			res.get(4).append("[" + String.format("%04d", id) + "]┤");
		}
		for (int i = 0; i < 3; i++) {
			res.add(new StringBuffer("│"));
		}
		res.add(new StringBuffer("╰"));
		for (int i = 0; i < 13; i++) {
			if (components.size() == 0) {
				var c = pr.getForbidden();
				for (int k = 0; k < 3; k++) {
					res.get(k + 5).append(c.get(k) + "│");
				}
				res.get(8).append("──────┴");
			} else {
				int id = components.removeFirst();
				ClientComponent c = new ComponentFactory().getComponent(id).getClientComponent();
				pr.reset();
				c.showComponent(ps);
				pr.setCenter(ps.getComponentStringSmall());
				c.showComponent(pr);
				var a = pr.getComponentStringsLarge();
				for (int k = 0; k < 3; k++) {
					res.get(k + 5).append(a.get(k));
					res.get(k + 5).append("│");
				}
				res.get(8).append("[" + String.format("%04d", id) + "]┴");
			}
		}
		if (components.size() == 0) {
			var c = pr.getForbidden();
			for (int k = 0; k < 3; k++) {
				res.get(k + 5).append(c.get(k) + "│");
			}
			res.get(8).append("──────╯");
		} else if (components.size() == 1) {
			int id = components.removeFirst();
			ClientComponent c = new ComponentFactory().getComponent(id).getClientComponent();
			pr.reset();
			c.showComponent(ps);
			pr.setCenter(ps.getComponentStringSmall());
			c.showComponent(pr);
			var a = pr.getComponentStringsLarge();
			for (int k = 0; k < 3; k++) {
				res.get(k + 5).append(a.get(k));
				res.get(k + 5).append("│");
			}
			res.get(8).append("[" + String.format("%04d", id) + "]╯");
		} else {
			res.get(5).append("  ..  │");
			res.get(6).append(" " + String.format("%04d", components.size()) + " │");
			res.get(7).append(" more │");
			res.get(8).append("──────╯");
		}
		return res.stream().map(p -> p.toString()).toList();
	}

}
