package it.polimi.ingsw.view.gui;

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


}
