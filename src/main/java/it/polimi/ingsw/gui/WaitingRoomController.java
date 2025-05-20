package it.polimi.ingsw.gui;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WaitingRoomController {

    ClientWaitingRoomState state;

    @FXML
    private ListView<String> player_list;

    @FXML
    private Label player_count;

    public WaitingRoomController(ClientWaitingRoomState state) {
        this.state = state;

    }

    public void paint(){
        player_list.setFixedCellSize(50);
        player_list.setItems(FXCollections.observableList(state.getPlayerList().stream().map(ClientWaitingPlayer::getUsername).collect(Collectors.toList())));

        player_count.setText(String.valueOf(state.getPlayerList().size()) + "/4");
    }

}
