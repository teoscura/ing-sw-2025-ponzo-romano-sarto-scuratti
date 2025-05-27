package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ClientLobbyStateController {
	private final ClientLobbySelectState state;
	private final GUIView view;

	@FXML
	private ListView<String> lobbyListView;

	public ClientLobbyStateController(ClientLobbySelectState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	public void initialize() {
		for (ClientGameListEntry entry : state.getLobbyList()) {
			String lobbyInfo = formatLobbyEntry(entry);
			lobbyListView.getItems().add(lobbyInfo);
		}
	}

	private String formatLobbyEntry(ClientGameListEntry entry) {
		StringBuilder sb = new StringBuilder();
		sb.append("ID ").append(entry.getModelId()).append(" - ")
				.append(entry.getType()).append(" | ")
				.append(entry.getPlayers().size()).append("/")
				.append(entry.getCount().getNumber()).append(" - ");
		for (String player : entry.getPlayers()) {
			sb.append(player).append(" ");
		}
		return sb.toString();
	}

}
