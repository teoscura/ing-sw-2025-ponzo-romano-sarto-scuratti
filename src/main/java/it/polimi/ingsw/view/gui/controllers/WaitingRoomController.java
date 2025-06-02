package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

public class WaitingRoomController {

	private final GUIView view;
	ClientWaitingRoomState state;
	@FXML
	private ListView<String> player_list;

	@FXML
	private Label player_count;

	@FXML
	private Label game_type;

	public WaitingRoomController(ClientWaitingRoomState state, GUIView view) {
		this.state = state;
		this.view = view;

	}

	public void initialize() {
		player_list.setFixedCellSize(50);
		player_list.setItems(FXCollections.observableList(state.getPlayerList().stream().map(ClientWaitingPlayer::getUsername).collect(Collectors.toList())));

		//player_count.setText(String.valueOf(state.getCount().getNumber() - state.getPlayerList().size()));
		//player_count.setText(state.getPlayerList().size() + "/4");
		player_count.setText(state.getPlayerList().size() + " / " + state.getCount().getNumber());
		game_type.setText(String.valueOf(state.getType()));
	}

	public void leaveWaitingRoom() {
		// not implemented
	}

}
