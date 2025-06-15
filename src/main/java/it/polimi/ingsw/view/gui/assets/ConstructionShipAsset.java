package it.polimi.ingsw.view.gui.assets;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class ConstructionShipAsset extends ShipAsset{

	public ConstructionShipAsset(ClientConstructionPlayer player) {
		super(player);

		pane.setOnMouseDragReleased(event -> {
			ImageView image = (ImageView) event.getGestureSource();

			int rowIndex = (int) ((event.getSceneY() - pane.getLayoutY()) / (pane.getHeight() / pane.getRowCount()));
			int colIndex = (int) ((event.getSceneX() - pane.getLayoutX()) / (pane.getWidth() / pane.getColumnCount()));

			if (image.getParent() instanceof GridPane) {
				GridPane.setRowIndex(image, rowIndex);
				GridPane.setColumnIndex(image, colIndex);
			}
			else{
				pane.add(image, colIndex, rowIndex);
			}
			image.setLayoutX(75*colIndex);
			image.setLayoutY(75*rowIndex);
			image.setMouseTransparent(false);
		});
	}


}
