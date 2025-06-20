package it.polimi.ingsw.view.gui.tiles;

import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.BatteryPiece;
import it.polimi.ingsw.view.gui.tiles.piece.CargoPiece;
import it.polimi.ingsw.view.gui.tiles.piece.CrewPiece;
import javafx.scene.image.ImageView;

/**
 * Implementation of {@link it.polimi.ingsw.model.client.components.ClientComponentVisitor}, creates a {@link it.polimi.ingsw.view.gui.tiles.PlacedTile} that represents the given {@link it.polimi.ingsw.model.client.components.ClientComponent}.
 */
public class PlacedTileFactory implements ClientComponentVisitor {

    private final GUIView view;
    private PlacedTile tile = null;
    private ShipCoords center = null;

    public PlacedTileFactory(GUIView view){
        this.view = view;
    }

    /**
     * Returns the corresponding {@link it.polimi.ingsw.view.gui.tiles.PlacedTile} from the given Client Component.
     * @param center {@link it.polimi.ingsw.model.player.ShipCoords} Coordinates used to create {@link it.polimi.ingsw.message.server.ServerMessage} triggered by Drag and Drop events.
     * @param component {@link it.polimi.ingsw.model.client.components.ClientComponent} Component to convert.
     * @return {@link it.polimi.ingsw.view.gui.tiles.PlacedTile} Produced tile.
     */
    public PlacedTile createTile(ShipCoords center, ClientComponent component){
        this.center = center;
        component.showComponent(this);
        return tile;
    }

    @Override
    public void show(ClientBaseComponent component) {
        tile = new PlacedTile(view, "galaxy_trucker_imgs/tiles/transparent/GT-tile-" + component.getId() + "_transparent.png", center);
        tile.getImage().setRotate(90*component.getRotation().getShift());
    }

    @Override
    public void show(ClientEmptyComponent component) {
        tile = new PlacedTile(view, "galaxy_trucker_imgs/tiles/transparent/empty.png", center);
    }

    @Override
    public void show(ClientBatteryComponentDecorator component) {
        for(int i = 0; i < component.getBatteries(); ++i){
            tile.addToList(new BatteryPiece(view, center));
        }
    }

    @Override
    public void show(ClientBrokenVerifyComponentDecorator component) {
        tile.setOverlay(new ImageView("galaxy_trucker_imgs/tiles/transparent/broken.png"));
    }

    @Override
    public void show(ClientCabinComponentDecorator component) {
        for(int i = 0; i < component.getCrew(); ++i){
            tile.addToList(new CrewPiece(view, center, component.getAlienType()));
        }
    }

    @Override
    public void show(ClientCannonComponentDecorator component) {
    }

    @Override
    public void show(ClientEngineComponentDecorator component) {
    }

    @Override
    public void show(ClientLifeSupportComponentDecorator component) {
    }

    @Override
    public void show(ClientPoweredComponentDecorator component) {
        if(!component.getPowered()) return;
        var over = new ImageView("galaxy_trucker_imgs/tiles/transparent/powered.png");
        over.setOpacity(0.3);
        tile.setOverlay(over);
        
    }

    @Override
    public void show(ClientShieldComponentDecorator component) {
    }

    @Override
    public void show(ClientShipmentsComponentDecorator component) {
        int val = 0;
        for(var i : component.getShipments()){
            for(int j = 0; j<i; j++){
                tile.addToList(new CargoPiece(view, center, ShipmentType.fromValue(val+1)));
            }
            val++;
        }
    }

}
