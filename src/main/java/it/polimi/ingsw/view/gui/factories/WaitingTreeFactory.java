package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class WaitingTreeFactory {

    static public Node createWaitingScreen(ClientWaitingRoomState state, GUIView guiView) {
        VBox list = new VBox(15);
        int i = 0;
        for(var p : state.getPlayerList()){
            list.getChildren().add(getWaitingPlayer(p));
            i++;
        }
        while(i<state.getCount().getNumber()){
            list.getChildren().add(getWaitingPlayer(new ClientWaitingPlayer("...", PlayerColor.NONE)));
            i++;
        }
        
        HBox res = new HBox(20, list, getGameModeSplash(state.getType()));
        res.setMaxWidth(800);
        res.setMaxHeight(600);
        return res;
    }

    static public Node getWaitingPlayer(ClientWaitingPlayer p){
        StackPane sp = new StackPane();
        sp.setMaxHeight(135);
        sp.setMaxWidth(390);
        Rectangle r = new Rectangle(390, 135, getColor(p.getColor()));
        r.getStyleClass().add("ui-rectangle");
        Label l = new Label(p.getUsername());
        l.getStyleClass().clear();
        l.getStyleClass().add("list-label-big");
        sp.getChildren().addAll(r, l);
        return sp;
    }

    static public Color getColor(PlayerColor color){
        switch(color){
            case BLUE:
                return new Color(51/255f, 205/255f, 255/255f, 1);
            case GREEN:
                return new Color(51/255f, 204/255f, 51/255f, 1);
            case RED:
                return new Color(255/255f, 51/255f, 0, 1);
            case YELLOW:
                return new Color(255/255f, 204/255f, 0, 1);
            default:
                return new Color(158/255f, 158/255f, 158/255f, 0.9);
        }
    }

    static public StackPane getGameModeSplash(GameModeType t){
        StackPane sp = new StackPane();
        switch(t){
            case LVL2: {
                var splash = new Rectangle(390, 596, new Color(163/255f, 5/255f, 63/255f, 1.0));
                splash.getStyleClass().add("ui-rectangle");
                sp.getChildren().add(splash);
                var logo = new ImageView("/galaxy_trucker_imgs/logos/lvl2.png");
                logo.setScaleX(0.6);
                logo.setScaleY(0.6);
                sp.getChildren().add(logo);
            } break;
            case TEST: {
                var splash = new Rectangle(390, 596, new Color(13/255f, 88/255f, 209/255f, 1.0));
                sp.getChildren().add(splash);
                splash.getStyleClass().add("ui-rectangle");
                var logo = new ImageView("/galaxy_trucker_imgs/logos/test.png");
                logo.setScaleX(0.6);
                logo.setScaleY(0.6);
                sp.getChildren().add(logo);
            } break;
        }
        Label l = new Label(t.toString());
        l.getStyleClass().clear();
        l.getStyleClass().add("list-label-big");
        sp.getChildren().add(l);
        StackPane.setAlignment(l, Pos.BOTTOM_CENTER);
        StackPane.setMargin(l, new Insets(0, 0, 40, 0));    
        return sp;
    }
    
}
