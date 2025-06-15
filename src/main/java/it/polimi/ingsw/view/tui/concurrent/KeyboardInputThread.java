package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.TUIView;
import it.polimi.ingsw.view.tui.TerminalWrapper;

/**
 * Thread object in charge of reading the keypresses of the user.
 */
public class KeyboardInputThread extends Thread {

	private final TerminalWrapper terminal;
	private final TUIView view;

	/**
	 * Constructs a {@link KeyboardInputThread} object.
	 * 
	 * @param terminal {@link TerminalWrapper} Terminal to read the keypresses from.
	 * @param view {@link it.polimi.ingsw.view.ClientView} View to notify of any input lines.
	 */
	public KeyboardInputThread(TerminalWrapper terminal, TUIView view) {
		this.terminal = terminal;
		this.view = view;
	}

	/**
	 * Main loop of the {@link KeyboardInputThread}, reads keypresses and notifies the view of any input lines.
	 */
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
