package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.AlienType;

public class RemoveComponentPiece extends DraggablePiece {
    
    public RemoveComponentPiece() {
        super(PieceImagePathProvider.crew(AlienType.HUMAN));
        //TODO: immagine trivella.

        this.setOnMouseDragEntered(event -> {

        });
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }


}
