package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.RemoveCrewMessage;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.MainApplication;

public class CrewPiece extends DraggablePiece {

    public CrewPiece(ShipCoords starting, AlienType type) {
        super(PieceImagePathProvider.crew(type));

        this.setOnMouseClicked(event -> {
            if(event.getClickCount()!=2) return;
            MainApplication.getView().sendMessage(new RemoveCrewMessage(starting));
        });

    }

}
