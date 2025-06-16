package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SetupTreeFactory {
	static public Node createSetupScreen(ClientSetupState state, GUIView view) {
		Label l1 = new Label("Player number: ");
		ChoiceBox<String> player_number = new ChoiceBox<>();

		player_number.getItems().addAll("2", "3", "4");

		Label l2 = new Label("Game mode: ");
		ChoiceBox<String> game_type = new ChoiceBox<>();
		game_type.getItems().addAll("Test Flight", "Level 2");

		Button create = new Button();
		create.setOnMouseClicked(event -> {
			String selectedPlayerNumber = player_number.getValue();
			String selectedGameType = game_type.getValue();
			GameModeType gameModeType = null;
			PlayerCount numPlayers = null;
			if (selectedPlayerNumber == null) {
				view.showTextMessage("Select the number of players.");
				return;
			}
			if (selectedGameType == null) {
				view.showTextMessage("Select the game type.");
				return;
			}
			switch (selectedPlayerNumber) {
				case "2":
					numPlayers = PlayerCount.TWO;
					break;
				case "3":
					numPlayers = PlayerCount.THREE;
					break;
				case "4":
					numPlayers = PlayerCount.FOUR;
					break;
				default:
					view.showTextMessage("Invalid player number.");
			}
			switch (selectedGameType) {
				case "Test Flight":
					gameModeType = GameModeType.TEST;
					break;
				case "Level 2":
					gameModeType = GameModeType.LVL2;
					break;
				default:
					view.showTextMessage("Invalid game ViewMessagetype.");
			}
			System.out.println(gameModeType);
			System.out.println(numPlayers);

			view.sendMessage(new OpenLobbyMessage(gameModeType, numPlayers));

		});

		VBox res = new VBox(10.0, l1, player_number, l2, game_type, create);
		res.setAlignment(Pos.CENTER);
		res.setMaxHeight(800);
		return res;
	}
}
