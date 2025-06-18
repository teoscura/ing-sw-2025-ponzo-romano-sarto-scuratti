package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import javafx.scene.image.ImageView;

public abstract class DraggablePiece extends ImageView {
    
    protected DraggablePiece(String path, double scale){
        super(path);
        this.setPreserveRatio(true);
        this.setFitWidth(scale*this.getImage().getWidth());
    }

    abstract public ServerMessage getDecoded(DraggablePieceDecoder dec);

}
