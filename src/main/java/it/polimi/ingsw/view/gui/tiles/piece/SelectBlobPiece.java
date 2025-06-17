package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class SelectBlobPiece extends DraggablePiece {
    
    public SelectBlobPiece() {
        super(PieceImagePathProvider.blob(), 1);
        
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
        
    }

    @Override
    public ServerMessage getDecoded(DraggablePieceDecoder dec) {
        return dec.decode(this);
    }

}
