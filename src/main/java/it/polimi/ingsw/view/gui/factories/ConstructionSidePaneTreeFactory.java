package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//gray: 169 169 169 

public class ConstructionSidePaneTreeFactory {
    
    static public Node createSidePane(GUIView view, ClientConstructionState state, PlayerColor color){
        ClientConstructionPlayer you = state.getPlayerList().stream().filter(p->p.getColor()==color).findAny().orElse(state.getPlayerList().getFirst());
        StackPane sp = new StackPane();
        sp.setMaxWidth(333);
        sp.getChildren().add(new Rectangle(333, 10000, new Color(169/255f,169/255f,169/255f,0.7)));
        VBox res = new VBox(20);
        res.setId("constr-pane-base");
        res.getChildren().add(createMainConstructionTileTree(view, you));
        res.getChildren().add(createReservedConstructionTileTree(view, you));
        res.getChildren().add(createDiscardedConstructionTileTree(view, state));
        if(state.getType().getLevel()>1) res.getChildren().add((createLevelTwoAddons(view, state)));
        Button confirm = new Button("Finish building!");
        confirm.setId("constr-confirm-button");
        confirm.setOnMouseClicked(event -> {
            view.sendMessage(new SendContinueMessage());
        });
        res.getChildren().add(confirm);
        res.getChildren().add(createColorSwitchTree(view, state, color));
        res.setAlignment(Pos.CENTER);
        res.setPrefHeight(10000);
        res.setMaxWidth(333);

        sp.getChildren().add(res);
        return sp;
    }

    static public Node createMainConstructionTileTree(GUIView view, ClientConstructionPlayer p){
        StackPane sp = new StackPane();
        sp.setMaxHeight(120);
        sp.setMaxWidth(120);
        sp.getChildren().add(new ImageView("galaxy_trucker_imgs/tiles/transparent/bg.png"));
        sp.getChildren().add(new ConstructionTile(view, p.getCurrent(), false, true, 1.0));
        return sp;
    }

    static public Node createReservedConstructionTileTree(GUIView view, ClientConstructionPlayer p){
        HBox res = new HBox(20);
        res.setAlignment(Pos.CENTER);
        for(int id : p.getReserved()){
            res.getChildren().add(new ConstructionTile(view, id, false, false, 0.5));
        }
        return res;
    }

    static public Node createDiscardedConstructionTileTree(GUIView view, ClientConstructionState state){
        ListView<ConstructionTile> res = new ListView<>();
        res.setMaxHeight(130);
        res.setOrientation(Orientation.HORIZONTAL);
        res.setId("constr-discarded-list");
        ObservableList<ConstructionTile> list = FXCollections.observableArrayList();
        for(int id : state.getDiscardedTiles()){
            list.add(new ConstructionTile(view, id, true, false, 0.8));
        }
        res.setItems(list);
        return res;
    }

    static public Node createLevelTwoAddons(GUIView view, ClientConstructionState state){
        HBox res = new HBox(30);
        res.setId("constr-leveltwo-addons");
        //TODO: questi.
        Button cards = new Button("Peek the cards");
        cards.setId("constr-peek-cards");
        Button toggle = new Button("Toggle hourglass");
        toggle.setId("constr-toggle-hourglass");
        res.getChildren().addAll(cards, toggle);
        return res;
    }

    static public Node createColorSwitchTree(GUIView view, ClientConstructionState state, PlayerColor color){
        HBox res = new HBox();
        res.setId("constr-color-switch");
        for(var p : state.getPlayerList()){
            if(p.getColor()==color) continue;
            //TODO: esiste costruttore Button(testo, Node), metterci un node figo.
            Button b = new Button(p.getColor().toString());
            b.setOnMouseClicked(event->{
                view.selectColor(p.getColor());
            });
            res.getChildren().add(b);
        }
        res.setAlignment(Pos.CENTER);
        return res;
    }
    

}
