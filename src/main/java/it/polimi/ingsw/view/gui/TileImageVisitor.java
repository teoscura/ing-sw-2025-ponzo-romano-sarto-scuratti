package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.view.gui.assets.TileAsset;
import javafx.scene.image.Image;

public class TileImageVisitor implements ClientComponentVisitor {

	private TileAsset tile;

	public TileAsset getTile(){
		return tile;
	}

	@Override
	public void show(ClientBaseComponent component) {
		Image base = new Image("galaxy_trucker_imgs/tiles/GT-tile-" + component.getId() + ".jpg");
		tile.setBase(base);
	}

	@Override
	public void show(ClientEmptyComponent component) {
		Image base = new Image("galaxy_trucker_imgs/tiles/GT-tile-157.jpg");
		tile.setBase(base);
	}

	@Override
	public void show(ClientBatteryComponentDecorator component) {

	}

	@Override
	public void show(ClientBrokenVerifyComponentDecorator component) {

	}

	@Override
	public void show(ClientCabinComponentDecorator component) {

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

	}

	@Override
	public void show(ClientShieldComponentDecorator component) {

	}

	@Override
	public void show(ClientShipmentsComponentDecorator component) {

	}
}
