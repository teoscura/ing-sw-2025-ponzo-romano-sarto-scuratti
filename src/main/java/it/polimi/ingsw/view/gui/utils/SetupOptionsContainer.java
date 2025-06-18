package it.polimi.ingsw.view.gui.utils;


import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.view.gui.GUIView;

public class SetupOptionsContainer {
	private PlayerCount count;
	private GameModeType mode;


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

	public void setCount(PlayerCount count) {
		this.count = count;
		System.out.println("PlayerCount set to: " + count);
	}

	public GameModeType getMode() {
		return mode;
	}

	public void setMode(GameModeType mode) {
		this.mode = mode;
		System.out.println("GameMode set to: " + mode);
	}
}

