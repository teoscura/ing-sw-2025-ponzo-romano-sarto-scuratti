package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.player.ShipCoords;

public class DraggablePieceDecoder {

    private final ShipCoords ending_coords;

    public DraggablePieceDecoder(ShipCoords ending_coords){
        this.ending_coords = ending_coords;
    }

    public ServerMessage decode(BatteryPiece p){
        return null;
    }

    public ServerMessage decode(CargoPiece p){
        return null;
    }

    public ServerMessage decode(CrewPiece p){
        return null;
    }

    public ServerMessage decode(CrewSetPiece p){
        return null;
    }

    public ServerMessage decode(RemoveComponentPiece p){
        return null;
    }

    public ServerMessage decode(SelectBlobPiece p){
        return null;
    }

}

