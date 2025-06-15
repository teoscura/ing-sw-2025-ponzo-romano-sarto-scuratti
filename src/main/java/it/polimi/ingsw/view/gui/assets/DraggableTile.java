package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.components.ClientComponent;

public class DraggableTile extends TileAsset {

	double anchorX, anchorY;


	public DraggableTile(ClientComponent component) {
		super(component);

		base.setOnMousePressed(event -> {
			base.setMouseTransparent(true);
			anchorX = event.getX();
			anchorY = event.getY();
		});

		base.setOnDragDetected(event -> {
			base.startFullDrag();
		});

		/*base.setOnMouseDragged(event -> {
			base.setLayoutX(event.getSceneX() - anchorX);
			base.setLayoutY(event.getSceneY() - anchorY);
		});*/

		base.setOnMouseReleased(event -> {
			base.setMouseTransparent(false);
		});
	}
}
