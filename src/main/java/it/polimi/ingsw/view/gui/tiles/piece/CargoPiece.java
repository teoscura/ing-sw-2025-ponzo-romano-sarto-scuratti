package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Represents a {@link it.polimi.ingsw.model.components.enums.ShipmentType} owned by a {@link it.polimi.ingsw.view.gui.tiles.PlacedTile} that represents a client side {@link it.polimi.ingsw.model.components.StorageComponent}.
 */
public class CargoPiece extends DraggablePiece {
 
    private final ShipmentType type;
    private final ShipCoords coords;

    public CargoPiece(GUIView view, ShipCoords starting, ShipmentType type){
        super(PieceImagePathProvider.cargo(type), 1);
        this.coords = starting;
        this.type = type;

        this.setOnDragDetected(event -> {
            Dragboard db = this.startDragAndDrop(TransferMode.ANY);
            var cb = new ClipboardContent();
            cb.putString(this.toString());
            db.setContent(cb);
            event.consume();
            db.setDragView(this.getImage(), 20.0, 20.0);
            this.setVisible(false);
        });

        this.setOnDragDone(event->{
            if(event.isDropCompleted()) return;
            this.setVisible(true);
        });

        this.setOnMouseClicked(event -> {
            if(event.getClickCount()!=2) return;
            if(coords==null) return;
            view.sendMessage(new DiscardCargoMessage(starting, type));
            event.consume();
        });

    }

    public ShipCoords getCoords() {
        return coords;
    }
    
    public ShipmentType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
