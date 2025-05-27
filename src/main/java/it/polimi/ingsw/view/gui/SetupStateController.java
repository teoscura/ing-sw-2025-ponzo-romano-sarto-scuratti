package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class SetupStateController {

	private final ClientSetupState state;
	private final GUIView view;
	private GameModeType gameModeType;
	private int numPlayers;

	@FXML
	private ChoiceBox player_number;
	@FXML
	private ChoiceBox game_type;

	public SetupStateController(ClientSetupState state, GUIView view) {
		this.state = state;
		this.view = view;
	}


	@FXML
	protected void initialize() {
		player_number.getItems().add("2");
		player_number.getItems().add("3");
		player_number.getItems().add("4");
		game_type.getItems().add("Test Flight");
		game_type.getItems().add("Level 2");

	}

	@FXML
	protected void create() {
		String selectedPlayerNumber = (String) player_number.getValue();
		String selectedGameType = (String) game_type.getValue();

		if (selectedPlayerNumber == null) {
			view.showTextMessage("Select the number of players.");
			return;
		}

		if (selectedGameType == null) {
			view.showTextMessage("Select the game type.");
			return;
		}

		try {
			numPlayers = Integer.parseInt(selectedPlayerNumber);
		} catch (NumberFormatException e) {

			view.showTextMessage("Invalid player number format.");
			return;
		}

		switch (selectedGameType) {
			case "Test Flight":
				gameModeType = GameModeType.TEST;
				break;
			case "Level 2":
				gameModeType = GameModeType.LVL2;
				break;
			default:
				view.showTextMessage("Invalid game type selected.");
		}

		// send

	}
}
