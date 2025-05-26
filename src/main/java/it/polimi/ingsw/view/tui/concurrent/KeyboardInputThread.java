package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class KeyboardInputThread extends Thread {

	private final TerminalWrapper terminal;
	private final TUIView view;

	public KeyboardInputThread(TerminalWrapper terminal, TUIView view) {
		this.terminal = terminal;
		this.view = view;
	}

	@Override
	public void run() {

		while (true) {
			while (!terminal.isAvailable()) {
				terminal.readBinding().apply();
			}
			view.handleLine(terminal.takeInput());
		}
	}

}
