package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;

public class BatteryPiece extends DraggablePiece {
 
    public BatteryPiece(GUIView view, ShipCoords starting){
        super(PieceImagePathProvider.battery());
        if(starting==null) throw new NullPointerException();
        
        this.setOnMouseDragReleased(event -> {
            var node = event.getPickResult().getIntersectedNode();
            if(node==null || !(node instanceof PlacedTile)) return;
            var coords = ((PlacedTile)node).getCoords();
            view.sendMessage(new TurnOnMessage(coords, starting));
        });

        this.setOnMouseClicked(event -> {
            if(event.getClickCount()!=2) return;
            view.sendMessage(new DiscardCargoMessage(starting, ShipmentType.EMPTY));
        });
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
