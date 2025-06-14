package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.client.state.ConnectedState;

/**
 * Class in charge of handling TUI input strings and converting them into requests to either the view or the remote game server.
 */
public class CommandPreprocessor {

	private final TUIView view;
	private final ConnectedState state;

	/**
	 * Constructs a {@link CommandPreprocessor} object tied to a {@link ClientView} and a {@link ConnectedState}.
	 * @param view {@link ClientView} View bound to the constructed object.
	 * @param state {@link ConnectedState} State bound to the constructed object.
	 */
	public CommandPreprocessor(TUIView view, ConnectedState state) {
		this.view = view;
		this.state = state;
	}

	/**
	 * Preprocesses a String into a view command or a {@link ServerMessage} to be sent.
	 * 
	 * @param s String to parse.
	 */
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
				state.sendMessage(MessageBuilder.build(s, view));
				break;
		}
	}

}
