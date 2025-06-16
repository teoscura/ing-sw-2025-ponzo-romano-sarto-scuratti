package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.SetCrewMessage;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;

public class CrewSetPiece extends DraggablePiece {
    
    public CrewSetPiece(AlienType type) {
        super(PieceImagePathProvider.crew(type));

        this.setOnMouseDragReleased(event -> {
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            MainApplication.getView().sendMessage(new SetCrewMessage(coords, type));
        });

    }

}
