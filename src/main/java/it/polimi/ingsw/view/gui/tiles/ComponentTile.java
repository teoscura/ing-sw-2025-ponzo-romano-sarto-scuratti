package it.polimi.ingsw.view.gui.tiles;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ComponentTile extends StackPane {

    protected final ImageView image;

    public ComponentTile(String path){
        this.image = new ImageView(path);
        this.getChildren().add(image);
    }
}
