package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class WaitingRoomController {

	private final GUIView view;
	ClientWaitingRoomState state;
	@FXML
	private ListView<ClientWaitingPlayer> player_list;

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
		player_list.setItems(FXCollections.observableList(state.getPlayerList()));

		player_list.setCellFactory(new Callback<>() {
			@Override
			public ListCell<ClientWaitingPlayer> call(ListView<ClientWaitingPlayer> listView) {
				return new ListCell<>() {
					@Override
					protected void updateItem(ClientWaitingPlayer player, boolean empty) {
						super.updateItem(player, empty);
						if (player == null || empty) {
							setText(null);
							setStyle("");
						} else {
							setText(player.getUsername());
							PlayerColor playerColor = player.getColor();
							String cssColor = mapPlayerColorToCss(playerColor);
							setStyle("-fx-background-color: " + cssColor + ";");
						}
					}
				};
			}
		});

		player_count.setText(state.getPlayerList().stream().filter(player -> player.getColor() != PlayerColor.NONE).count() + " / " + state.getCount().getNumber());
		game_type.setText(String.valueOf(state.getType()));
	}


	private String mapPlayerColorToCss(PlayerColor playerColor) {
		switch (playerColor) {
			case RED:
				return "lightcoral";
			case BLUE:
				return "lightblue";
			case GREEN:
				return "lightgreen";
			case YELLOW:
				return "yellow";
			default:
				return "gray";
		}
	}

	public void leaveWaitingRoom() {
		// not implemented
	}
}