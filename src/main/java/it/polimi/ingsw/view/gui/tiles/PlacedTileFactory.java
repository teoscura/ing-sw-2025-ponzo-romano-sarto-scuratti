package it.polimi.ingsw.view.gui.tiles;

import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.BatteryPiece;
import it.polimi.ingsw.view.gui.tiles.piece.CargoPiece;
import it.polimi.ingsw.view.gui.tiles.piece.CrewPiece;
import javafx.scene.image.ImageView;

public class PlacedTileFactory implements ClientComponentVisitor {

    private final GUIView view;
    private PlacedTile tile = null;
    private ShipCoords center = null;

    public PlacedTileFactory(GUIView view){
        this.view = view;
    }

    public PlacedTile createTile(ShipCoords center, ClientComponent component){
        this.center = center;
        component.showComponent(this);
        return tile;
    }

    @Override
    public void show(ClientBaseComponent component) {
        tile = new PlacedTile(view, "galaxy_trucker_imgs/tiles/transparent/GT-tile-" + component.getId() + "_transparent.png", center);
        tile.setRotate(90*component.getRotation().getShift());
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
        tile.setOverlay(new ImageView("galaxy_trucker_imgs/tiles/broken.jpg"));
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
        tile.setOverlay(new ImageView("galaxy_trucker_imgs/tiles/powered.jpg"));
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
