package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.AlienType;

public class SelectBlobPiece extends DraggablePiece {
    
    public SelectBlobPiece() {
        super(PieceImagePathProvider.crew(AlienType.HUMAN));
        //TODO: immagine blob select

        //TODO on drag entered.
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
