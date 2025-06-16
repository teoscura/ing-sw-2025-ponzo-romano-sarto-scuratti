package it.polimi.ingsw.view.gui.tiles;

import java.util.ArrayList;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.DraggablePiece;
import it.polimi.ingsw.view.gui.tiles.piece.DraggablePieceDecoder;
import javafx.scene.image.ImageView;

public class PlacedTile extends ComponentTile {

    private final ShipCoords coords;
    private final ArrayList<DraggablePiece> tiles;

    public PlacedTile(GUIView view, String path, ShipCoords coords){
        super(path);
        this.coords = coords;
        tiles = new ArrayList<>();

        this.setOnDragDropped(event -> {
            var o = event.getSource();
            if(!(o instanceof DraggablePiece)) return;
            var dec = new DraggablePieceDecoder(coords);
            ServerMessage res = ((DraggablePiece)o).getDecoded(dec);
            view.sendMessage(res);
        });

    }

    public void setOverlay(ImageView overlay){
        this.getChildren().add(overlay);
        overlay.relocate(0, 0);
    }

    public void addToList(DraggablePiece piece){
        double offset = 0;
        for(DraggablePiece p : tiles){
            offset += p.getFitWidth();
        }
        tiles.add(piece);
        this.getChildren().add(piece);
        piece.relocate(this.getLayoutBounds().getMaxX() - piece.getFitWidth() - offset, this.getLayoutBounds().getMaxY() - piece.getFitHeight());
    }

    public ShipCoords getCoords(){
        return coords;
    }

}
