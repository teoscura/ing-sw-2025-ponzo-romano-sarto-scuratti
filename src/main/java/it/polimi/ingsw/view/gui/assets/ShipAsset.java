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
	private final GridPane pane;
	private TileAsset[][] components;

	public ShipAsset(ClientConstructionPlayer player) {
		pane = new GridPane();
		pane.getColumnConstraints().addAll(new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75));
		pane.getRowConstraints().addAll(new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75));
		TileImageVisitor v = new TileImageVisitor();
		for (int i=0; i<5; i++){
			for (int j=0; j<7; j++){
				components[i][j] = new TileAsset(player.getShip().getComponent(new ShipCoords(player.getShip().getType(), i, j)));
				pane.add(components[i][j].getBase(), j, i);
			}
		}
	}

	public GridPane getGrid() {
		return pane;
	}



}
