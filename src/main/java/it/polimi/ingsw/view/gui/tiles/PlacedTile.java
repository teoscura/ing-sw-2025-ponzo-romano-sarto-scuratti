package it.polimi.ingsw.view.gui.tiles;

import java.util.ArrayList;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.DraggablePiece;
import it.polimi.ingsw.view.gui.tiles.piece.DraggablePieceDecoder;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

public class PlacedTile extends ComponentTile {

    private final ShipCoords coords;
    private final ArrayList<DraggablePiece> tiles;

    public PlacedTile(GUIView view, String path, ShipCoords coords){
        super(path, 1.0);
        this.coords = coords;
        tiles = new ArrayList<>();

        this.setOnDragDropped(event -> {
            var o = event.getGestureSource();
            if(o==null) return;
            if(!(o instanceof DraggablePiece)) return;
            event.setDropCompleted(true);
            var dec = new DraggablePieceDecoder(coords);
            ServerMessage res = ((DraggablePiece)o).getDecoded(dec);
            view.sendMessage(res);
        });

        this.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

    }

    public void setOverlay(ImageView overlay){
        this.getChildren().add(overlay);
        overlay.relocate(0, 0);
    }

    public void addToList(DraggablePiece piece){
        piece.getStyleClass().add("draggable-piece");
        this.getChildren().add(piece);
        StackPane.setAlignment(piece, Pos.BOTTOM_RIGHT);
        piece.setTranslateX(-30*tiles.size()+7);
        piece.setTranslateY(3);
        this.tiles.add(piece);
    }

    public ShipCoords getCoords(){
        return coords;
    }

}
