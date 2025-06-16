package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;

public class PlacedShipTreeFactory {
    
    static public Node createPlacedShip(GUIView view, ClientSpaceShip ship){
        var res = new GridPane();
        res.setGridLinesVisible(false);
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getColumnConstraints().add(new ColumnConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        // res.getRowConstraints().add(new RowConstraints(100));
        PlacedTileFactory f = new PlacedTileFactory(view);
        for(int j = 0; j < ship.getType().getHeight(); j++){
            for(int i = 0; i< ship.getType().getWidth(); i++){
                var tmp = new ShipCoords(ship.getType(), i, j);
                System.out.print(tmp+": ");
                res.add(f.createTile(tmp, ship.getComponent(tmp)), i, j, 1, 1);
            }
        }
        res.setMaxSize(1050, 750);
        return res;
    }

}
