package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.TileImageVisitor;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class ShipAsset {
	private GridPane pane;

	public ShipAsset(ClientConstructionPlayer player) {
		pane = new GridPane();
		pane.getColumnConstraints().addAll(new ColumnConstraints(50), new ColumnConstraints(50), new ColumnConstraints(50), new ColumnConstraints(50), new ColumnConstraints(50));
		pane.getRowConstraints().addAll(new RowConstraints(50), new RowConstraints(50), new RowConstraints(50), new RowConstraints(50), new RowConstraints(50), new RowConstraints(50), new RowConstraints(50));
		TileImageVisitor v = new TileImageVisitor();
		for (int i=0; i<5; i++){
			for (int j=0; j<7; j++){
				player.getShip().getComponent(new ShipCoords(player.getShip().getType(), i, j)).showComponent(v);
				ImageView temp = new ImageView();
				temp.setFitHeight(50);
				temp.setFitWidth(50);
				pane.add(temp, j, i);
			}
		}
	}
}
