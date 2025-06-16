package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.AlienType;

public class CrewSetPiece extends DraggablePiece {

    private final AlienType type;

    public CrewSetPiece(AlienType type) {
        super(PieceImagePathProvider.crew(type));
        
        this.type = type;

        //TODO on drag entered.

    }

    public AlienType getType() {
        return type;
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
