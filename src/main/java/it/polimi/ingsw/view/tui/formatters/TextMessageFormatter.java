package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.view.tui.TUINotification;
import it.polimi.ingsw.view.tui.TerminalWrapper;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

public class TextMessageFormatter {

	static public void format(TerminalWrapper terminal, ArrayList<TUINotification> notifications) {
		ArrayList<TUINotification> to_show = new ArrayList<>();
		int size = notifications.size();
		if (size > 4) to_show.addAll(notifications.subList(size - 4, size));
		else to_show.addAll(notifications);
		int row = 1;
		for (int i = 0; i < to_show.size(); i++) {
			var notif = format(to_show.get(i).getText());
			terminal.print(notif, row, 94);
			row += notif.size();
		}
	}

	//TODO trim lenght of message because if not we have an issue xd xd xd xd xd;
	static private ArrayList<String> format(String message) {
		ArrayList<String> res = new ArrayList<>();
		int wraplength = 32;
		res.add("╭" + "─".repeat(8) + "  New Message!  " + "─".repeat(8) + "╮");
		StringBuffer remaining = new StringBuffer(message.trim());
		while (remaining.length() > 0) {
			String line = remaining.substring(0, remaining.length() > wraplength ? wraplength : remaining.length());
			line = line.length() == wraplength ? line : line + " ".repeat(wraplength - line.length());
			res.add("│" + line + "│");
			remaining.delete(0, remaining.length() > wraplength ? wraplength : remaining.length());
		}
		res.add("╰" + "─".repeat(wraplength) + "╯");
		return res;
	}

	static public boolean trimExpired(ArrayList<TUINotification> notifications) {
		Iterator<TUINotification> it = notifications.listIterator();
		boolean trimmed = false;
		while (it.hasNext()) {
			TUINotification n = it.next();
			if (Duration.between(n.getTimestamp(), Instant.now()).compareTo(n.getTTL()) < 0) continue;
			it.remove();
			trimmed = true;
		}
		return trimmed;
	}
}