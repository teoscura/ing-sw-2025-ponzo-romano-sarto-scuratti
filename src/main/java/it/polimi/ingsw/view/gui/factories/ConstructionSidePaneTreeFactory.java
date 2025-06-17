package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class ConstructionSidePaneTreeFactory {
    
    static public Node createSidePane(GUIView view, ClientConstructionState state, PlayerColor color){
        // ClientConstructionPlayer you = state.getPlayerList().stream().filter(p->p.getColor()==color).findAny().orElse(null);
        // if(you == null) return new Group();
        // HBox secondary = new HBox();
        // secondary.getChildren().addAll(new Button("Peek cards."), new Button("Hourglass."));
        // secondary.setMaxWidth(333);
        // HBox player_picker = new HBox();
        // player_picker.setMaxWidth(333);
        // for(var pl : state.getPlayerList()){
        //     if(pl.equals(you)) continue;
        //     player_picker.getChildren().add(new Button(pl.getColor().toString()));
        // }
        // secondary.setAlignment(Pos.CENTER);
        // player_picker.setAlignment(Pos.CENTER);
        // VBox res = new VBox(secondary, player_picker);
        // res.setPrefHeight(900);
        // res.setMaxWidth(333);
        var res = new Rectangle(300, 900);
        return res;
    }

    

}
