package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.message.server.ServerMessage;
import javafx.scene.image.ImageView;

/**
 * Abstract class representing a piece that may be dragged and dropped on a {@link it.polimi.ingsw.view.gui.tiles.PlacedTile}.
 */
public abstract class DraggablePiece extends ImageView {
    
    protected DraggablePiece(String path, double scale){
        super(path);
        this.setPreserveRatio(true);
        this.setFitWidth(scale*this.getImage().getWidth());
    }

    /**
     * Method used to support visitor compatibility.
     * @param dec {@link it.polimi.ingsw.view.gui.tiles.piece.DraggablePieceDecoder} Decoder visitor to call upon.
     * @return {@link it.polimi.ingsw.message.server.ServerMessage} Message resulting from the Drag and Drop event.
     */
    abstract public ServerMessage getDecoded(DraggablePieceDecoder dec);

}
