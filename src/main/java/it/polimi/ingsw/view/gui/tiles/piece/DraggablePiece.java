package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import javafx.scene.image.ImageView;

public abstract class DraggablePiece extends ImageView {
    
    protected DraggablePiece(String path){
        super(path);
    }

    abstract public ServerMessage getDecoded(DraggablePieceDecoder dec);

}
