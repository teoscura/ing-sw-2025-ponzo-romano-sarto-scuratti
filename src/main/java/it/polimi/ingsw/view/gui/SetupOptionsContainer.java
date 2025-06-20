package it.polimi.ingsw.view.gui;


import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import javafx.scene.Node;

/**
 * Utility class used to hold all info used to open a lobby, used through lambda expression captures.
 */
public class SetupOptionsContainer {
	private PlayerCount count;
	private GameModeType mode;
	private Node count_button;
	private Node mode_button;

	/**
	 * Uses the currently selected parameters to open a lobby.
	 * 
	 * @param view {@link it.polimi.ingsw.view.gui.GUIView} View used to forward the {@link it.polimi.ingsw.message.server.OpenLobbyMessage} to the {@link it.polimi.ingsw.controller.client.state.ConnectedState}.
	 */
	public void sendSetup(GUIView view) {
		if (count != null && mode != null) {
			view.sendMessage(new OpenLobbyMessage(mode, count));
		} else {
			System.err.println("Something was not selected.");
		}
	}

	public PlayerCount getCount() {
		return count;
	}

	/**
	 * Sets a {@link it.polimi.ingsw.model.PlayerCount} to be used.
	 * 
	 * @param count {@link it.polimi.ingsw.model.PlayerCount} Count to be selected.
	 * @param button JavaFX node to highlight with a CSS class.
	 */
	public void setCount(PlayerCount count, Node button) {
		this.count = count;
		if (this.count_button != null) {
			this.count_button.getStyleClass().remove("count-option-selected");
		}
		this.count_button = button;
		this.count_button.getStyleClass().add("count-option-selected");
	}

	public GameModeType getMode() {
		return mode;
	}


	/**
	 * Sets a {@link it.polimi.ingsw.model.GameModeType} to be used.
	 * 
	 * @param mode {@link it.polimi.ingsw.model.GameModeType} Mode type to be selected.
	 * @param button JavaFX node to highlight with a CSS class.
	 */
	public void setMode(GameModeType mode, Node button) {
		this.mode = mode;
		if (this.mode_button != null) {
			this.mode_button.getStyleClass().remove("mode-option-selected");
		}
		this.mode_button = button;
		this.mode_button.getStyleClass().add("mode-option-selected");
	}

	public Node getcount_button() {
		return count_button;
	}

	public Node getmode_button() {
		return mode_button;
	}
}

