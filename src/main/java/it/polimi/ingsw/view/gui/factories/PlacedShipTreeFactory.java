package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class PlacedShipTreeFactory {
    
    static public Node createPlacedShip(GUIView view, ClientSpaceShip ship){
        var res = new GridPane();
        res.setGridLinesVisible(true);
        PlacedTileFactory f = new PlacedTileFactory(view);
        for(int j = 0; j < ship.getType().getHeight(); j++){
            for(int i = 0; i< ship.getType().getWidth(); i++){
                var tmp = new ShipCoords(ship.getType(), i, j);
                res.add(f.createTile(tmp, ship.getComponent(tmp)), i, j, 1, 1);
            }
        }
        res.setMaxWidth(700);
        res.setMaxHeight(500);

        res.setOnDragDropped(event -> {
            var src = event.getGestureSource();
            var node = event.getPickResult().getIntersectedNode();
            if(src==null || node == null) return;
            if(!(src instanceof ConstructionTile)) return;
            if(!(node instanceof PlacedTile)) return;
            event.setDropCompleted(true);
            var comp = (ConstructionTile) src;
            var trgt = (PlacedTile) node;
            ServerMessage msg = new PutComponentMessage(comp.getID(), trgt.getCoords(), comp.getRotation());
            view.sendMessage(msg);
            event.setDropCompleted(true);
        });

        res.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        return res;
    }

}
