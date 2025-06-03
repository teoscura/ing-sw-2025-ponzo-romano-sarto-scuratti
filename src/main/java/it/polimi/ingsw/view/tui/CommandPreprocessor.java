package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.client.state.ConnectedState;

public class CommandPreprocessor {

	private final TUIView view;
	private final ConnectedState state;

	public CommandPreprocessor(TUIView view, ConnectedState state) {
		this.view = view;
		this.state = state;
	}

	public void process(String s) {
		switch (s) {
			case "red", "blue", "green", "yellow":
				view.changeShip(s);
				break;
			case "help":
				view.showHelpScreen();
				break;
			case "stateinfo":
				view.showStateInfo();
				break;
			case "disconnect":
				state.disconnect();
				break;
			default:
				forward(s);
				break;
		}
	}

	private void forward(String s) {
		state.sendMessage(CommandBuilder.build(s, view));
	}

}
