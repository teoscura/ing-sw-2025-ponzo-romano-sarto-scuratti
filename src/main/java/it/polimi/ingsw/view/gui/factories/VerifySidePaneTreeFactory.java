package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.CrewSetPiece;
import it.polimi.ingsw.view.gui.tiles.piece.RemoveComponentPiece;
import it.polimi.ingsw.view.gui.tiles.piece.SelectBlobPiece;
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
import javafx.scene.text.Font;

public class VerifySidePaneTreeFactory {
    
    static public Node createSidePane(GUIView view, ClientVerifyState state, PlayerColor color){
        StackPane sp = new StackPane();
        sp.setMaxWidth(333);
        sp.getChildren().add(new Rectangle(333, 10000, new Color(169/255f,169/255f,169/255f,0.7)));
        VBox res = new VBox(25);
        res.setId("verify-pane-base");
        //Add destroy
        Label remove_lab = new Label("Drag to remove:");
        remove_lab.setFont(new Font(27.0));
        remove_lab.getStyleClass().add("verify_hint_label");
        RemoveComponentPiece removep  = new RemoveComponentPiece();
        //Add select blob
        Label select_lab = new Label("Drag to select blob:");
        select_lab.setFont(new Font(27.0));
        remove_lab.getStyleClass().add("verify_hint_label");
        SelectBlobPiece selectp = new SelectBlobPiece();
        //Add the three crew types
        Label crew_lab = new Label("Drag to change crew type:");
        crew_lab.setFont(new Font(27.0));
        remove_lab.getStyleClass().add("verify_hint_label");
        HBox box = new HBox(40);
        box.getChildren().add(new CrewSetPiece(AlienType.HUMAN));
        box.getChildren().add(new CrewSetPiece(AlienType.BROWN));
        box.getChildren().add(new CrewSetPiece(AlienType.PURPLE));
        box.setAlignment(Pos.CENTER);
        //Bottom
        Button confirm = new Button("Finish verifying!");
        confirm.setId("constr-confirm-button");
        confirm.setOnMouseClicked(event -> {
            view.sendMessage(new SendContinueMessage());
        });
        var await = createAwaitingTree(state);
        res.getChildren().addAll(remove_lab, removep, select_lab, selectp, crew_lab, box, confirm, await);
        StackPane.setAlignment(await, Pos.CENTER);
        res.getChildren().add(createColorSwitchTree(view, state, color));
        //Final alignment
        res.setAlignment(Pos.CENTER);
        res.setPrefHeight(10000);
        res.setMaxWidth(333);

        sp.getChildren().add(res);
        return sp;
    }

    static public Node createAwaitingTree(ClientVerifyState state){
        HBox res = new HBox(20);
        res.setId("verify-player-lists");
        res.setMaxWidth(333);
        VBox awaiting = new VBox(15);
        awaiting.setMaxWidth(333);
        VBox finished = new VBox(15);
        finished.setMaxWidth(333);
        var list_finished = state.getPlayerList().stream()
            .filter(p->p.isValid())
            .sorted((p1, p2) -> -Integer.compare(p1.getOrder(), p1.getOrder()))
            .toList();
        var list_to_finish = state.getPlayerList().stream()
            .filter(p->!p.hasProgressed())
            .toList();
        //Todo: rifarlo bene.
        Label awaiting_lab = new Label("Awaiting: ");
        Label finished_lab = new Label("Finish order:");
        awaiting_lab.setFont(new Font(25));
        awaiting_lab.getStyleClass().add("verify_player_label_title");
        finished_lab.setFont(new Font(25));
        finished_lab.getStyleClass().add("verify_player_label_title");
        awaiting.getChildren().add(awaiting_lab);
        finished.getChildren().add(finished_lab);
        for(var p : list_to_finish){
            var l = new Label(p.getColor().toString()+" - "+p.getUsername());
            l.getStyleClass().add("verify_player_label");
            awaiting.getChildren().add(l);
        }
        for(var p : list_finished){
            var l = new Label(p.getColor().toString()+" - "+p.getUsername());
            l.getStyleClass().add("verify_player_label");
            finished.getChildren().add(l);
        }
        res.getChildren().addAll(awaiting, finished);
        res.setAlignment(Pos.CENTER);
        return res;
    }

    static public Node createColorSwitchTree(GUIView view, ClientVerifyState state, PlayerColor color){
        HBox res = new HBox(20);
        Label lab = new Label("View: ");
        lab.setFont(new Font(18));
        res.getChildren().add(lab);
        res.setId("constr-color-switch");
        for(var p : state.getPlayerList()){
            if(p.getColor()==color) continue;
            ImageView v = new ImageView("galaxy_trucker_imgs/piece/"+p.getColor()+".png");
            v.setOnMouseClicked(event->{
                view.selectColor(p.getColor());
            });
            res.getChildren().add(v);
        }
        res.setAlignment(Pos.CENTER);
        return res;
    }

}
