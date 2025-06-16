package it.polimi.ingsw.view.gui.controllers;

import java.time.Duration;
import java.time.Instant;

import it.polimi.ingsw.message.server.TakeComponentMessage;
import it.polimi.ingsw.message.server.ToggleHourglassMessage;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientState;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.gui.assets.ConstructionPlayerTextFlow;
import it.polimi.ingsw.view.gui.assets.ShipAsset;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ConstructionController {

	private ClientConstructionState state;
	private final GUIView view;

	@FXML private AnchorPane pane;
	@FXML private GridPane ship_grid;
	@FXML private ListView<ConstructionTile> discarded_list;
	@FXML private ConstructionPlayerTextFlow player_button_1;
	@FXML private ConstructionPlayerTextFlow player_button_2;
	@FXML private ConstructionPlayerTextFlow player_button_3;
	@FXML private ConstructionTile reserved_1;
	@FXML private ConstructionTile reserved_2;
	@FXML private Button component_pile;
	@FXML private Button hourglass_button;
	@FXML private ImageView card_pile;

	private ShipAsset player_ship;
	private ConstructionTile current_tile;

	public ConstructionController(ClientConstructionState state, GUIView view) {
		this.state = state;
		this.view = view;
	}

	public void grabComponent() {
		MainApplication.getView().sendMessage(new TakeComponentMessage());
	}

	public void toggle_hourglass(){
		ToggleHourglassMessage message = new ToggleHourglassMessage();
		MainApplication.getView().sendMessage(message);
	}

	public void updateHourglassAnimation(){
		Instant end = state.getLastToggle().plus(Duration.ofSeconds(90));
		AnimationTimer hourglass_timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				Duration elapsed = Duration.between(Instant.now(), end);
				hourglass_button.setText(("[Left: "+state.getTogglesLeft())+"/"+state.getTogglesTotal()+"] " + (elapsed.toSeconds()) + " seconds left");
				if (Instant.now().isAfter(end)) {
					stop();
					hourglass_button.setText(("[Left: "+state.getTogglesLeft())+"/"+state.getTogglesTotal()+"] Toggle hourglass");
				}
			}
		};
		hourglass_timer.start();
	}

	public void show_cards(){
		//TODO Popup con le immagini delle carte.
	}

	@FXML
	public void inizialize() {
		var p = this.state.getPlayerList().stream().filter(player -> player.getColor().equals(view.getSelectedColor())).findFirst().orElse(this.state.getPlayerList().getFirst());
		if(p.getColor()!=view.getSelectedColor()) view.selectColor(p.getColor());
		if(p.getCurrent()==-1){
			this.current_tile.setVisible(false);
			this.current_tile.setDisable(true);
			this.component_pile.setVisible(true);
			this.component_pile.setDisable(false);
		} else {
			this.current_tile = new ConstructionTile(p.getCurrent(), false, true);
			AnchorPane.setTopAnchor(current_tile, component_pile.getLayoutY());
			AnchorPane.setLeftAnchor(current_tile, component_pile.getLayoutX());
			this.current_tile.setVisible(true);
			this.current_tile.setDisable(false);
			this.component_pile.setVisible(false);
			this.component_pile.setDisable(true);
		}
		int j = 0;
		for(var i : p.getReserved()){
			setDiscarded(j, new ConstructionTile(i, false, false));
			j++;
		}
		for(; j<2;j++){
			disableDiscarded(j);
		}
		this.player_ship = new ShipAsset(p);
		this.ship_grid = player_ship.getGrid();
		this.updateHourglassAnimation();
		j = 0;
		for(var pl : state.getPlayerList()){
			if(pl.getColor()==view.getSelectedColor()) continue;
			setSmallPlayer(j, new ConstructionPlayerTextFlow(pl, view));
			j++;
		}
		for(; j<2;j++){
			disableSmallPlayer(j);
		}
		//TODO set three player buttons.
		hourglass_button.setOnAction(event->toggle_hourglass());
		component_pile.setOnMouseClicked(event -> grabComponent());
		this.card_pile.setOnMouseClicked(event -> show_cards());
	}

	public void update(ClientState state){
		var tmpstate = (ClientConstructionState) state;
		var oldp = this.state.getPlayerList().stream().filter(player -> player.getColor().equals(view.getSelectedColor())).findFirst().orElse(this.state.getPlayerList().getFirst());
		var newp = tmpstate.getPlayerList().stream().filter(player -> player.getColor().equals(view.getSelectedColor())).findFirst().orElse(tmpstate.getPlayerList().getFirst());
		if(oldp.getColor()!=view.getSelectedColor()) view.selectColor(oldp.getColor());

		if(oldp.getCurrent()!=newp.getCurrent()){
			if(newp.getCurrent()==-1){
				this.current_tile.setVisible(false);
				this.current_tile.setDisable(true);
				this.card_pile.setVisible(true);
				this.card_pile.setDisable(false);
			} else {
				this.current_tile = new ConstructionTile(newp.getCurrent(), false, true);
				AnchorPane.setTopAnchor(current_tile, component_pile.getLayoutY());
				AnchorPane.setLeftAnchor(current_tile, component_pile.getLayoutX());
				this.current_tile.setVisible(true);
				this.current_tile.setDisable(false);
				this.card_pile.setVisible(false);
				this.card_pile.setDisable(true);
			}
		}
		if(!oldp.getReserved().equals(newp.getReserved())){
			int j = 0;
			for(var i : newp.getReserved()){
				setDiscarded(j, new ConstructionTile(i, false, false));
				j++;
			}
			for(; j<2;j++){
				disableDiscarded(j);
			}
		}
		if(!tmpstate.getDiscardedTiles().equals(this.state.getDiscardedTiles())){
			this.discarded_list.getItems().clear();
			ObservableList<ConstructionTile> t = FXCollections.observableArrayList();
			for(var i : tmpstate.getDiscardedTiles()){
				t.add(new ConstructionTile(i, true, false));
			}
			this.discarded_list.setItems(t);
		}
		if(!oldp.getShip().equals(newp.getShip())) {
			this.player_ship = new ShipAsset(newp);
			this.ship_grid = player_ship.getGrid();
		}
		if(!tmpstate.getLastToggle().equals(this.state.getLastToggle())){
			this.updateHourglassAnimation();
		}
		int j = 0;
		for(var pl : tmpstate.getPlayerList()){
			if(pl.getColor()==view.getSelectedColor()) continue;
			setSmallPlayer(j, new ConstructionPlayerTextFlow(pl, view));
			j++;
		}
		for(; j<2;j++){
			disableSmallPlayer(j);
		}
		this.state = tmpstate;
	}

	private void setDiscarded(int i, ConstructionTile t){
		if(i == 0) {
			this.reserved_1 = t;
		} else if (i == 1) {
			this.reserved_2 = t;
		}
		else throw new RuntimeException();
	}
	
	private void disableDiscarded(int i){
		if(i == 0) {
			this.reserved_1.setVisible(false);
			this.reserved_1.setDisable(true);
		} else if (i == 1) {
			this.reserved_2.setVisible(false);
			this.reserved_2.setDisable(true);
		}
		else throw new RuntimeException();
	}

	private void setSmallPlayer(int i, ConstructionPlayerTextFlow ctf){
		if(i == 0) {
			this.player_button_1 = ctf;
		} else if (i == 1) {
			this.player_button_2 = ctf;
		} else if (i == 2) {
			this.player_button_3 = ctf;
		}
		else throw new RuntimeException();
	}

	private void disableSmallPlayer(int i){
		if(i == 0) {
			this.player_button_1.setVisible(false);
			this.player_button_1.setDisable(true);
		} else if (i == 1) {
			this.player_button_2.setVisible(false);
			this.player_button_2.setDisable(true);
		} else if (i == 2) {
			this.player_button_3.setVisible(false);
			this.player_button_3.setDisable(true);
		}
		else throw new RuntimeException();
	}

}
