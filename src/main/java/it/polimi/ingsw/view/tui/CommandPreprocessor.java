package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.client.state.ConnectedState;
import it.polimi.ingsw.message.server.ServerMessage;

/**
 * Class in charge of handling TUI input strings and converting them into requests to either the view or the remote game server.
 */
public class CommandPreprocessor {

	private final TUIView view;
	private final ConnectedState state;

	/**
	 * Constructs a {@link CommandPreprocessor} object tied to a {@link it.polimi.ingsw.view.ClientView} and a {@link ConnectedState}.
	 * @param view {@link it.polimi.ingsw.view.ClientView} View bound to the constructed object.
	 * @param state {@link ConnectedState} State bound to the constructed object.
	 */
	public CommandPreprocessor(TUIView view, ConnectedState state) {
		this.view = view;
		this.state = state;
	}

	/**
	 * Preprocesses a String into a view command or a {@link it.polimi.ingsw.message.server.ServerMessage} to be sent.
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
				try {
					ServerMessage message = MessageBuilder.build(s, view);
					state.sendMessage(message);
				} catch (Throwable e) {
					view.showTextMessage("Input an illegal command!");
				}
				break;
		}
	}

}
