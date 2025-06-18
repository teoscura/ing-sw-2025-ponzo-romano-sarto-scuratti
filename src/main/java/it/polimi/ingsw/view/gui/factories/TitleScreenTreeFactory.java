package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.state.TitleScreenState;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TitleScreenTreeFactory {
    
    static public Node createTitleScreen(TitleScreenState state){
        
        TextField input = new TextField();

        Label prompt = new Label("Insert Username: ");
        //prompt.setFont(new Font(30));
        prompt.setId("title-prompt");
        input.setMaxWidth(300);
        var image = new ImageView("galaxy_trucker_imgs/logos/galaxy_trucker_logo_med.png");
        image.setFitWidth(550);
        image.setFitHeight(500);
        Button confirm = new Button("Confirm");
        confirm.setOnAction(event -> {
            state.setUsername(input.getText());
        });
        VBox res = new VBox(10, image, prompt, input, confirm);
        res.setAlignment(Pos.CENTER);
        res.setMaxHeight(800);
        return res;
    }
}
