package it.polimi.ingsw.controller.client.state;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the state of the client at username selection.
 */
public class TitleScreenState extends ClientControllerState {

	private String username;

	/**
	 * Constructs a {@link TitleScreenState} object.
	 * @param controller {@inheritDoc}
	 * @param view {@inheritDoc}
	 */
	public TitleScreenState(ClientController controller, ClientView view) {
		super(controller, view);
	}

	/**
	 * Notifies the view of the change.
	 */
	@Override
	public void init() {
		view.show(this);
	}

	@Override
	public ClientControllerState getNext() {
		if (!validateUsername(username)) {
			view.showTextMessage("Invalid username.");
			return new TitleScreenState(controller, view);
		}
		return new ConnectingState(this.controller, this.view, this.username);
	}

	/**
	 * Sets the username used to then connect to the remote game server.
	 * 
	 * @param username Username to be selected.
	 */
	public void setUsername(String username) {
		this.username = username;
		this.transition();
	}

	public void exit() {
		this.controller.close();
	}

	/**
	 * Validates the username.
	 * @param username Input username.
	 * @return Whether the username is valid or not.
	 */
	private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}

}
