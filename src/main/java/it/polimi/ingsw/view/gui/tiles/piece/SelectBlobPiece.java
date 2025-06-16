package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.SelectBlobMessage;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;

public class SelectBlobPiece extends DraggablePiece {
    
    public SelectBlobPiece() {
        super(PieceImagePathProvider.crew(AlienType.HUMAN));
        //TODO: immagine blob select

        this.setOnMouseDragReleased(event -> {
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            MainApplication.getView().sendMessage(new SelectBlobMessage(coords));
        });
    }
}
