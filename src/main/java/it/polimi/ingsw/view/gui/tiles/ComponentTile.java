package it.polimi.ingsw.view.gui.tiles;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ComponentTile extends StackPane {

    protected final ImageView image;

    public ComponentTile(String path, double scale){
        this.image = new ImageView(path);
        image.setScaleX(scale);
        image.setScaleY(scale);
        this.setMaxWidth(image.getFitWidth()*scale);
        this.setMaxHeight(image.getFitHeight()*scale);
        this.getChildren().add(image);
    }
}
