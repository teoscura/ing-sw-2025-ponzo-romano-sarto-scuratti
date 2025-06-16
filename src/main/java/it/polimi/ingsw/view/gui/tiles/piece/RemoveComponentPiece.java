package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.RemoveComponentMessage;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;

public class RemoveComponentPiece extends DraggablePiece {
    
    public RemoveComponentPiece() {
        super(PieceImagePathProvider.crew(AlienType.HUMAN));
        //TODO: immagine trivella.

        this.setOnMouseDragReleased(event -> {
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            MainApplication.getView().sendMessage(new RemoveComponentMessage(coords));
        });

    }
}
