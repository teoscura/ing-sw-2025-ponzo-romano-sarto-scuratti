package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.view.gui.TileImageVisitor;
import javafx.scene.image.ImageView;

public class TileAsset {

	protected final ImageView base;
	private final ClientComponent component;

	public TileAsset(ClientComponent component) {
		this.component = component;
		TileImageVisitor visitor = new TileImageVisitor();
		component.showComponent(visitor);
		base = new ImageView(visitor.getTile());
	}

	public ImageView getBase() {
		return base;
	}

	public ClientComponent getComponent() {
		return component;
	}

}
