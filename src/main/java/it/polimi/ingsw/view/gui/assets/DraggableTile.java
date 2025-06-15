package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.components.ClientComponent;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DraggableTile extends TileAsset{

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
