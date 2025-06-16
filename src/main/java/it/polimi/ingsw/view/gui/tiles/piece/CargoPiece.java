package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;

public class CargoPiece extends DraggablePiece {
 
    public CargoPiece(GUIView view, ShipCoords starting, ShipmentType type){
        super(PieceImagePathProvider.cargo(type));
        
        //TODO on drag entered.

        this.setOnMouseClicked(event -> {
            if(event.getClickCount()!=2) return;
            view.sendMessage(new DiscardCargoMessage(starting, type));
        });

    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
