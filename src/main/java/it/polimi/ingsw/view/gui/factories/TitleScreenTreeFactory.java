package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.state.TitleScreenState;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class TitleScreenTreeFactory {
    
    static public Node createTitleScreen(TitleScreenState state){
        
        TextField input = new TextField();

        Label prompt = new Label("Insert Username: ");
        //prompt.setFont(new Font(30));
        input.setMaxWidth(300);
        var image = new ImageView("galaxy_trucker_imgs/logos/galaxy_trucker_logo_med.png");
        image.setFitWidth(550);
        image.setFitHeight(500);
        prompt.getStyleClass().add("list-label-big");
        input.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                state.setUsername(input.getText());
            }
        });
        input.getStyleClass().add("titlescreen-field");
        VBox res = new VBox(10, image, prompt, input);
        res.setAlignment(Pos.CENTER);
        res.setMaxHeight(800);
        return res;
    }
}
