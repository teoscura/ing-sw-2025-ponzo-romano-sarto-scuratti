package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.DiscardCargoMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class BatteryPiece extends DraggablePiece {
 
    private final ShipCoords coords;

    public BatteryPiece(GUIView view, ShipCoords starting){
        super(PieceImagePathProvider.battery());
        if(starting==null) throw new NullPointerException();
        this.coords = starting;

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
            if(this.coords==null) return;
            view.sendMessage(new DiscardCargoMessage(starting, ShipmentType.EMPTY));
        });
    }

    public ShipCoords getCoords() {
        return coords;
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
