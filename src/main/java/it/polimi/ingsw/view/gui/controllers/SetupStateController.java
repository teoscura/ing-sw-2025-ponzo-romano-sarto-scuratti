package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.server.LeaveSetupMessage;
import it.polimi.ingsw.message.server.OpenLobbyMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SetupStateController {

	private final ClientSetupState state;
	private final GUIView view;
	private GameModeType gameModeType;
	private PlayerCount numPlayers;

	@FXML
	private ChoiceBox player_number;
	@FXML
	private ChoiceBox game_type;
	@FXML
	private ListView<ClientGameListEntry> unfinishedListView;

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

		if (unfinishedListView != null) {
			unfinishedListView.getItems().addAll(state.getUnfinishedList());

			unfinishedListView.setCellFactory(new Callback<>() {
				@Override
				public ListCell<ClientGameListEntry> call(ListView<ClientGameListEntry> listView) {
					return new ListCell<>() {
						@Override
						protected void updateItem(ClientGameListEntry entry, boolean empty) {
							super.updateItem(entry, empty);
							if (empty || entry == null) {
								setText(null);
							} else {
								setText(formatUnfinishedEntry(entry));
							}
						}
					};
				}
			});
		}

	}

	private String formatUnfinishedEntry(ClientGameListEntry entry) {
		boolean isFull = entry.getCount().getNumber() == entry.getPlayers().size();
		String mode = entry.getType() == GameModeType.LVL2 ? "Level 2" : "Test Flight";
		String fullTag = isFull ? " (Full)" : "";

		StringBuilder sb = new StringBuilder();
		sb.append("Game ").append(entry.getModelId())
				.append(" - Mode: ").append(mode)
				.append(" | Players: ").append(entry.getPlayers().size()).append("/").append(entry.getCount().getNumber()).append(fullTag)
				.append(" - ");

		for (String player : entry.getPlayers()) {
			sb.append(player).append(" ");
		}

		return sb.toString().trim();
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
		switch (selectedPlayerNumber) {
			case "2":
				numPlayers = PlayerCount.TWO;
				break;
			case "3":
				numPlayers = PlayerCount.THREE;
			case "4":
				numPlayers = PlayerCount.FOUR;
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

		view.setInput(new OpenLobbyMessage(gameModeType, numPlayers));
		//view.setInput(new LeaveSetupMessage());

	}
	@FXML
	protected void leaveSetup(){
		view.setInput(new LeaveSetupMessage());
	}
}
