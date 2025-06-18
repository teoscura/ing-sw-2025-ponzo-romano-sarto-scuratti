package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EndgameTreeFactory {
    
    public static Node createEnding(GUIView view, ClientEndgameState state){
        VBox list = new VBox(20);
        list.setMaxWidth(840);
        for(var p : state.getPlayerList()){
            list.getChildren().add(getEntry(p));
        }
        list.setAlignment(Pos.CENTER);
        list.getChildren().add(awaiting(state));
        return list;
    }

    public static Node getEntry(ClientEndgamePlayer p){
        StackPane sb = new StackPane();
        sb.setMaxWidth(840);
        sb.setMaxHeight(120);
        Rectangle base = new Rectangle(840, 120);
        base.getStyleClass().add("ui-rectangle");
        switch(p.getColor()){
            case BLUE:
                base.getStyleClass().add("list-blue-bg");
                break;
            case GREEN:
                base.getStyleClass().add("list-green-bg");
                break;
            case RED:
                base.getStyleClass().add("list-red-bg");
                break;
            case YELLOW:
                base.getStyleClass().add("list-yellow-bg");
                break;
            default:
                base.getStyleClass().add("list-gray-bg");
                break;
        }
        sb.getChildren().add(base);
        StackPane.setAlignment(base, Pos.CENTER);
        Rectangle flourish = new Rectangle(120,120);
        flourish.getStyleClass().add("ui-rectangle");
        switch(p.getColor()){
            case BLUE:
                flourish.getStyleClass().add("list-blue-faint");
                break;
            case GREEN:
                flourish.getStyleClass().add("list-green-faint");
                break;
            case RED:
                flourish.getStyleClass().add("list-red-faint");
                break;
            case YELLOW:
                flourish.getStyleClass().add("list-yellow-faint");
                break;
            default:
                flourish.getStyleClass().add("list-gray-faint");
                break;
        }
        sb.getChildren().add(flourish);
        StackPane.setAlignment(flourish, Pos.CENTER_LEFT);
        ImageView piece = new ImageView("galaxy_trucker_imgs/piece/"+p.getColor()+".png");
        piece.setPreserveRatio(true);
        piece.setFitWidth(120);
        sb.getChildren().add(piece);
        StackPane.setAlignment(piece, Pos.CENTER_LEFT);
        Rectangle textbackdrop = new Rectangle(720, 100, new Color(169/255f,169/255f,169/255f,0.7));
        textbackdrop.getStyleClass().add("ui-rectangle");
        sb.getChildren().add(textbackdrop);
        StackPane.setAlignment(textbackdrop, Pos.CENTER_RIGHT);
        HBox fields = new HBox();
        fields.setMaxHeight(100);
        fields.setMaxWidth(720);
        String s = p.getPlanche_slot() >= 0 ? " - #" + p.getPlanche_slot() + " - " : " - DNF - ";
        fields.getChildren().add(new Label(p.getUsername() + p));
        int value = 1;
        for(var c : p.getShipments()){
            fields.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/"+ShipmentType.fromValue(value)+".png"));
            fields.getChildren().add(new Label(" "+c+" "));
            value++;   
        }
        ImageView moneybag = new ImageView("galaxy_trucker_imgs/piece/money.png");
        moneybag.setPreserveRatio(true);
        moneybag.setFitWidth(60);
        fields.getChildren().add(moneybag);
        fields.getChildren().add(new Label(" "+p.getCredits()));
        sb.getChildren().add(fields);
        StackPane.setAlignment(fields, Pos.CENTER_RIGHT);
        return sb;
    }

    static public Node awaiting(ClientEndgameState e){
        return null;
    }

}
