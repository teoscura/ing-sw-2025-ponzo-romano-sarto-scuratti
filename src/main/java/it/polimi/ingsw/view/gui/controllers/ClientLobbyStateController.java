package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.message.server.EnterLobbyMessage;
import it.polimi.ingsw.message.server.EnterSetupMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.ClientGameListEntry;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ClientLobbyStateController {
	private final ClientLobbySelectState state;
	private final GUIView view;

	@FXML
	private ListView<ClientGameListEntry> lobbyListView;

	public ClientLobbyStateController(ClientLobbySelectState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	@FXML
	public void initialize() {
		System.out.println(">>> GUI initialize() called");
		System.out.println(">>> lobbyListView is null? " + (lobbyListView == null));

		if (lobbyListView != null) {
			lobbyListView.getItems().addAll(state.getLobbyList());
		}
		lobbyListView.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ClientGameListEntry> call(ListView<ClientGameListEntry> listView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ClientGameListEntry entry, boolean empty) {
						super.updateItem(entry, empty);
						if (empty || entry == null) {
							setText(null);
							setGraphic(null);
						} else {
							setText(formatLobbyEntry(entry));
						}
					}
				};
			}
		});
	}

	private String formatLobbyEntry(ClientGameListEntry entry) {
		boolean isFull = entry.getCount().getNumber() == entry.getPlayers().size();
		String typeColor = entry.getType() == GameModeType.LVL2 ? "Level 2" : "Test Flightfix";
		String fullTag = isFull ? " (Full)" : "";

		StringBuilder sb = new StringBuilder();
		sb.append("Lobby ").append(entry.getModelId())
				.append(" - Mode: ").append(entry.getType()).append(typeColor)
				.append(" | Players: ").append(entry.getPlayers().size()).append("/")
				.append(entry.getCount().getNumber()).append(fullTag)
				.append(" - ");

		for (String player : entry.getPlayers()) {
			sb.append(player).append(" ");
		}

		return sb.toString().trim();
	}

	@FXML
	public void selectLobby(MouseEvent event) {
		if (event.getClickCount() == 2) {
			int selection = lobbyListView.getSelectionModel().getSelectedIndex();
			if (selection != -1) {
				int gameId = state.getLobbyList().get(selection).getModelId();
				view.sendMessage(new EnterLobbyMessage(gameId));
			}
		}

	}

	@FXML
	public void enterSetup() {
		System.out.println("change state");
		//TODO
		view.sendMessage(new EnterSetupMessage());

	}
}
