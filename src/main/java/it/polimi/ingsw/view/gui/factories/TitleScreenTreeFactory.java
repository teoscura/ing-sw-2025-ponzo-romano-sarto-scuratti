package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.controller.client.state.TitleScreenState;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class TitleScreenTreeFactory {
    
    static public Node createTitleScreen(TitleScreenState state){
        
        TextField input = new TextField();

        Label prompt = new Label("Insert Username: ");
        input.setMaxWidth(300);
        var image = new ImageView("galaxy_trucker_imgs/cardboard/cardboard-1b.jpg");
        Button confirm = new Button("Confirm");
        confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
             @Override public void handle(MouseEvent e) {
                if(e.getClickCount()!=1) return;
                state.setUsername(input.getText());
            }
        });
        VBox res = new VBox(10, image, prompt, input, confirm);
        res.setAlignment(Pos.CENTER);
        res.setMaxHeight(800);
        return res;
    }
}
