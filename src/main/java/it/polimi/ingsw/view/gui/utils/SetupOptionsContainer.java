package it.polimi.ingsw.view.gui.utils;


import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.scene.Node;

public class SetupOptionsContainer {
	private PlayerCount count;
	private GameModeType mode;
	private Node count_button;
	private Node mode_button;

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

