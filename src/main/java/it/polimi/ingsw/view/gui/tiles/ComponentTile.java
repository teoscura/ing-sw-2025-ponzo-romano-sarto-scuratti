package it.polimi.ingsw.view.gui.tiles;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class ComponentTile extends Group {

    public ComponentTile(String path){
        this.getChildren().add(new ImageView(path));
    }
}
