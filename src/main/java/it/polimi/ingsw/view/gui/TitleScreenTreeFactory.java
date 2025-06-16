package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.state.TitleScreenState;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class TitleScreenTreeFactory {
    
    static public Node createTitleScreen(final StackPane root, TitleScreenState state){
        res = FXMLLoader.load("/it/polimi/ingsw/TitleScreenWidget.fxml");
        root.setAlignment(res, Pos.CENTER);
    }
}
