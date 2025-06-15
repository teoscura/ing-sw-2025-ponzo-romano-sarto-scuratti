package it.polimi.ingsw.view.tui.concurrent;

import it.polimi.ingsw.view.tui.TUIView;

/**
 * Thread object in charge of periodically redrawing the screen to keep up with the new information.
 */
public class RedrawThread extends Thread {

	private final TUIView view;

	/**
	 * Constructs a {@link RedrawThread} bound to a {@link TUIView view}.
	 * 
	 * @param view {@link TUIView} View to periodically redraw. 
	 */
	public RedrawThread(TUIView view) {
		this.view = view;
	}

	/**
	 * Main loop of the {@link RedrawThread}, periodically redraws the screen.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				view.showTextMessage("Redraw thread got interrupted!");
			}
			view.draw();
		}
	}

}
