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

/**
 * GUI representation of a placed {@link it.polimi.ingsw.model.client.components.ClientComponent} that allows for Drag and Drop support.
 */
public class PlacedTile extends ComponentTile {

    private final ShipCoords coords;
    private final ArrayList<DraggablePiece> tiles;

    /**
     * Constructs a {@link it.polimi.ingsw.view.gui.tiles.PlacedTile} object.
     * 
     * @param view {@link it.polimi.ingsw.view.gui.GUIView} View used to forward the {@link it.polimi.ingsw.message.server.OpenLobbyMessage} to the {@link it.polimi.ingsw.controller.client.state.ConnectedState}.
     * @param path Path of the image to display.
     * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coordinates used to create {@link it.polimi.ingsw.message.server.ServerMessage} triggered by Drag and Drop events.
     */
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

    /**
     * Adds a {@link it.polimi.ingsw.view.gui.tiles.piece.DraggablePiece} to the component.
     * 
     * @param piece {@link it.polimi.ingsw.view.gui.tiles.piece.DraggablePiece} To be added.
     */
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
