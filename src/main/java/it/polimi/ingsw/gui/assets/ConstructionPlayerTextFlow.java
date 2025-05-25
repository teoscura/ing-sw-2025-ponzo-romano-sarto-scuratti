package it.polimi.ingsw.gui.assets;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Window;

public class ConstructionPlayerTextFlow {

	private final ClientConstructionPlayer player;
	private final Text player_name;
	private final Text cannon_text;
	private final Text engine_text;
	private final Text battery_text;
	private final TextFlow player_viewer;

	public ConstructionPlayerTextFlow(ClientConstructionPlayer player) {
		this.player = player;
		this.player_name = new Text(player.getUsername() + "\n");
		switch (player.getColor()) {
			case RED -> player_name.setFill(Color.RED);
			case YELLOW -> player_name.setFill(Color.YELLOW);
			case GREEN -> player_name.setFill(Color.GREEN);
			case BLUE -> player_name.setFill(Color.BLUE);
		}
		this.cannon_text = new Text("Cannon power: " + player.getShip().getCannonPower() + "\n");
		this.engine_text = new Text("Engine power: " + player.getShip().getEnginePower() + "\n");
		this.battery_text = new Text("Batteries: " + player.getShip().getContainers()[0] + "\n");
		player_viewer = new TextFlow(player_name, cannon_text, engine_text, battery_text);
		player_viewer.setOnMouseClicked(event -> showPlayerShip(player_viewer.getScene().getWindow()));
	}

	public void showPlayerShip(Window stage) {

		//TODO: create and build ship asset
		GridPane ship = new GridPane();
		Scene scene = new Scene(ship);
		Popup ship_popup = new Popup();
		ship_popup.setAutoHide(true);
		ship_popup.setAutoFix(true);
		ship_popup.show(stage);
	}

	public TextFlow getPlayerViewer() {
		return this.player_viewer;
	}

}
