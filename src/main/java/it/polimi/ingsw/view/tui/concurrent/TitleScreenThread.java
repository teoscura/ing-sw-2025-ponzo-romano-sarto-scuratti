package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.tui.TUIView;

public class TitleScreenThread extends Thread {

	private final TitleScreenState state;
	private final TUIView view;

	public TitleScreenThread(TitleScreenState state, TUIView view) {
		if (state == null) throw new NullPointerException();
		this.state = state;
		this.view = view;
	}

	public void run() {
		try {
			state.setUsername(view.takeLine());
		} catch (InterruptedException e) {
			view.showTextMessage("Interrupted title screen thread.");
		}
	}

}
