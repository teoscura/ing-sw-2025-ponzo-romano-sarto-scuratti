package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.tui.TerminalWrapper;

import java.util.ArrayList;

public class ClientStateOverlayFormatter implements ClientStateVisitor {

	private final TerminalWrapper terminal;

	public ClientStateOverlayFormatter(TerminalWrapper terminal) {
		this.terminal = terminal;
	}

	public void show(ClientLobbySelectState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

	public void show(ClientSetupState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

	public void show(ClientWaitingRoomState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

	public void show(ClientConstructionState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(7) + " Uncovered Cards: " + "─".repeat(7) + "╮");
		if (state.getConstructionCards() == null) {
			for (int i = 0; i < 4; i++) res.add("│" + " ".repeat(32) + "│");
			res.add("│ [State has no uncovered cards] │");
			for (int i = 0; i < 4; i++) res.add("│" + " ".repeat(32) + "│");
		} else {
			LevelOneCardFactory f1 = new LevelOneCardFactory();
			LevelTwoCardFactory f2 = new LevelTwoCardFactory();
			for (Integer t : state.getConstructionCards()) {
				String id = String.format("%03d", t);
				String line = " [" + id + "]: " + (t > 20 ? f2.getCard(t) : f1.getCard(t)).getClass().getSimpleName();
				res.add("│" + line + " ".repeat(32 - line.length()) + "│");
			}
		}
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 9, 48);
	}

	public void show(ClientVerifyState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

	public void show(ClientVoyageState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

	public void show(ClientEndgameState state) {
		ArrayList<String> res = new ArrayList<>();
		res.add("╭" + "─".repeat(32) + "╮");
		res.add("│" + " State doesn't have extra info! " + "│");
		res.add("╰" + "─".repeat(32) + "╯");
		terminal.print(res, 13, 48);
	}

}
