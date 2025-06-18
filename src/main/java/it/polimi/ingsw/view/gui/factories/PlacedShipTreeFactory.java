package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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

        ImageView bg = null;
        if(ship.getType()==GameModeType.TEST) bg = new ImageView("galaxy_trucker_imgs/ship_background_lv1.png");
        else bg = new ImageView("galaxy_trucker_imgs/ship_background_lv2.png");
        bg.setId("ship-bg-img");
        bg.setOpacity(0.4);
        StackPane sp = new StackPane(bg, res);
        sp.setMaxWidth(840);
        sp.setMaxHeight(768);
        sp.setAlignment(Pos.CENTER);
        sp.setId("ship");
        return sp;
    }

}
