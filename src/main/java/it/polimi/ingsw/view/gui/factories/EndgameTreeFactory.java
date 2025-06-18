package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
        Button leave = new Button("Motion to leave.");
        leave.setOnAction(event -> {
            view.sendMessage(new SendContinueMessage());
        });
        list.getChildren().add(leave);
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
        fields.setAlignment(Pos.CENTER);
        String s = p.getPlanche_slot() >= 0 ? " - #" + (p.getPlanche_slot()+1) + " - " : " - DNF - ";
        var name = new Label(p.getUsername() + s);
        fields.getChildren().add(name);
        name.getStyleClass().clear();
        name.getStyleClass().add("list-label");
        int value = 0;
        for(var c : p.getShipments()){
            if(value==0){
                value++;
                continue;
            }
            fields.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/"+ShipmentType.fromValue(value).toString().toLowerCase()+".png"));
            var lab = new Label(" "+c+" ");
            lab.getStyleClass().clear();
            lab.getStyleClass().add("list-label");
            fields.getChildren().add(lab);
            value++;   
        }
        ImageView moneybag = new ImageView("galaxy_trucker_imgs/piece/money.png");
        moneybag.setPreserveRatio(true);
        moneybag.setFitWidth(60);
        fields.getChildren().add(moneybag);
        var lab = new Label(" "+p.getCredits());
        fields.getChildren().add(lab);
        lab.getStyleClass().clear();
        lab.getStyleClass().add("list-label");
        sb.getChildren().add(fields);
        StackPane.setAlignment(fields, Pos.CENTER_RIGHT);
        return sb;
    }

    static public Node awaiting(ClientEndgameState s){
        VBox res = new VBox(10);
        res.setMaxWidth(333);
        Label awaiting_lab = new Label("Awaiting: ");
        HBox awaiting = new HBox(8);
        awaiting.setAlignment(Pos.CENTER);
        awaiting.getStyleClass().add("verify-list");
        for(var e : s.awaiting()){
            awaiting.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/"+e.toString()+".png"));
        }
        awaiting.setMaxWidth(333);
        res.getChildren().add(awaiting_lab);
        res.getChildren().add(awaiting);
        res.setAlignment(Pos.CENTER);
        return res;
    }

}
