package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.player.ShipCoords;

public class CrewPiece extends DraggablePiece {

    public CrewPiece(ShipCoords starting, AlienType type) {
        super(PieceImagePathProvider.crew(type));

        //TODO on drag entered.

    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
