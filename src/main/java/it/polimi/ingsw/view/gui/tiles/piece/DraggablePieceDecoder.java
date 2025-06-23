package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.MoveCargoMessage;
import it.polimi.ingsw.message.server.RemoveComponentMessage;
import it.polimi.ingsw.message.server.SelectBlobMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.SetCrewMessage;
import it.polimi.ingsw.message.server.TakeCargoMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.player.ShipCoords;

/**
 * Visitor class used to discertain what {@link it.polimi.ingsw.message.server.ServerMessage} to generate due to a Drag and Drop event.
 */
public class DraggablePieceDecoder {

    private final ShipCoords ending_coords;

    public DraggablePieceDecoder(ShipCoords ending_coords){
        this.ending_coords = ending_coords;
    }

    public ServerMessage decode(BatteryPiece p){
        return new TurnOnMessage(ending_coords, p.getCoords());
    }

    public ServerMessage decode(CargoPiece p){
        if(p.getCoords()==null) return new TakeCargoMessage(ending_coords, p.getType());
        return new MoveCargoMessage(ending_coords, p.getCoords(), p.getType());
    }

    public ServerMessage decode(CrewPiece p){
        return null;
    }

    public ServerMessage decode(CrewSetPiece p){
        return new SetCrewMessage(ending_coords, p.getType());
    }

    public ServerMessage decode(RemoveComponentPiece p){
        return new RemoveComponentMessage(ending_coords);
    }

    public ServerMessage decode(SelectBlobPiece p){
        return new SelectBlobMessage(ending_coords);
    }

}

