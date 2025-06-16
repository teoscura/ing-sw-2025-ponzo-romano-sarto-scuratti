package it.polimi.ingsw.view.gui.tiles;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ComponentTile extends StackPane {

    public ComponentTile(String path){
        var img = new ImageView(path);
        this.setMaxWidth(img.getFitWidth());
        this.setMaxHeight(img.getFitHeight());
        this.getChildren().add(img);
    }
}
