package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.MoveCargoMessage;
import it.polimi.ingsw.message.server.TakeCargoMessage;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;

public class CargoPiece extends DraggablePiece {
 
    public CargoPiece(ShipCoords starting, ShipmentType type){
        super(PieceImagePathProvider.cargo(type));
        
        this.setOnMouseDragReleased(event -> {
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            if(starting == null)  MainApplication.getView().sendMessage(new TakeCargoMessage(coords, type));
            MainApplication.getView().sendMessage(new MoveCargoMessage(coords, starting, type));
        });

        this.setOnMouseClicked(event -> {
            if(event.getClickCount()!=2) return;
            MainApplication.getView().sendMessage(new DiscardCargoMessage(starting, type));
        });

    }

}
