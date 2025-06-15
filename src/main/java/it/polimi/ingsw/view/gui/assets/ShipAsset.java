package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class ShipAsset {
	
	protected final GridPane pane;
	private PlacedTile[][] components;

	public ShipAsset(ClientConstructionPlayer player) {
		pane = new GridPane();
		pane.getColumnConstraints().addAll(new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75), new ColumnConstraints(75));
		pane.getRowConstraints().addAll(new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75), new RowConstraints(75));
		//TileImageVisitor v = new TileImageVisitor();
		PlacedTileFactory f = new PlacedTileFactory();
		for (int i=0; i<5; i++){
			for (int j=0; j<7; j++){
				ShipCoords tmp = new ShipCoords(player.getShip().getType(), i, j);
				components[i][j] = f.createTile(tmp, player.getShip().getComponent(tmp));
				pane.add(components[i][j], j, i);
			}
		}
		
	}

	public void addComponent(PlacedTile component){
		var tmp = component.getCoords();
		components[tmp.y][tmp.x] = component;
	}

	public GridPane getGrid() {
		return pane;
	}

}
