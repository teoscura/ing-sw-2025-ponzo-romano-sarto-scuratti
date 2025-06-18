package it.polimi.ingsw.view.gui.utils;


import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.scene.control.Button;

public class SetupOptionsContainer {
	private PlayerCount count;
	private GameModeType mode;
	private Button countButton;
	private Button modeButton;

	public void sendSetup(GUIView view) {
		if (count != null && mode != null) {
			view.sendMessage(new OpenLobbyMessage(mode, count));
			System.out.println("GameMode:" + mode + ", PlayerCount:" + count);
		} else {
			System.err.println("Something was not selected.");
		}
	}

	public PlayerCount getCount() {
		return count;
	}

	public void setCount(PlayerCount count, Button countButton) {
		this.count = count;
		if (this.countButton != null) {
			this.countButton.getStyleClass().remove("count-option-selected");
		}
		this.countButton = countButton;
		this.countButton.getStyleClass().add("count-option-selected");
		System.out.println("PlayerCount set to: " + count);
	}

	public GameModeType getMode() {
		return mode;
	}

	public void setMode(GameModeType mode, Button modeButton) {
		this.mode = mode;
		if (this.modeButton != null) {
			this.modeButton.getStyleClass().remove("mode-option-selected");
		}
		this.modeButton = modeButton;
		this.modeButton.getStyleClass().add("mode-option-selected");
		System.out.println("GameMode set to: " + mode);
	}

	public Button getCountButton() {
		return countButton;
	}

	public Button getModeButton() {
		return modeButton;
	}
}

