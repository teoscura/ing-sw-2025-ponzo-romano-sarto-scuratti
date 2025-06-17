package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.piece.CargoPiece;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class VoyageSidePaneTreeFactory implements ClientCardStateVisitor {

    private VBox cstatetree = null;
    private GUIView view;

    public VoyageSidePaneTreeFactory(GUIView view){
        this.view = view;
    }

    public Node createSidePane(GUIView view, ClientVoyageState state, PlayerColor color){
        ClientVoyagePlayer you = state.getPlayerList().stream().filter(p->p.getColor()==color).findAny().orElse(state.getPlayerList().getFirst());
        StackPane sp = new StackPane();
        sp.setMaxWidth(333);
        this.cstatetree = new VBox(10);
        this.cstatetree.setMaxWidth(333);
        Label card = new Label("Card: "+(state.getType().getLenght()-state.getCardsLeft()+1)+"/"+state.getType().getLenght());
        this.cstatetree.getChildren().add(card);
        state.getCardState().showCardState(this);
        cstatetree.setId("voyage-card-state-pane");
        return sp;
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        HBox awaiting = new HBox(10);
        awaiting.setAlignment(Pos.CENTER);
        awaiting.getStyleClass().add("voyage-awaiting-list");
        Label label = new Label("Awaiting continue from:");
        awaiting.getChildren().add(label);
        for(var e : state.getAwaiting()){
            awaiting.getChildren().add(new ImageView("galaxy_trucker_imgs/piece/"+e.toString()+".png"));
        }
        awaiting.setMaxWidth(333);
        this.cstatetree.getChildren().add(awaiting);
    }

    @Override
    public void show(ClientBaseCardState state) {
        var e = new ImageView("galaxy_trucker_imgs/cards/Gt-card-"+state.getID()+".jpg");
        e.getStyleClass().add("voyage-card-image");
        this.cstatetree.getChildren().add(e);
    }

    @Override
    public void show(ClientCargoPenaltyCardStateDecorator state) {
        HBox awaiting = new HBox(10);
        awaiting.setAlignment(Pos.CENTER);
        awaiting.setMaxWidth(333);
        awaiting.getStyleClass().add("voyage-cargo-pen-label");
        Label message = new Label("Cargo penalty: ");
        ImageView piece = new ImageView("galaxy_trucker_imgs/piece/"+state.getTurn().toString()+".png");
        awaiting.getChildren().addAll(message, piece);
        this.cstatetree.getChildren().add(awaiting);
        HBox required = new HBox(10);
        required.setAlignment(Pos.CENTER);
        required.setMaxWidth(333);
        required.getStyleClass().add("voyage-cargo-pen-required");
        int k = 0;
        for(int i : state.getShipments()){
            for(int j = 0; j < i; j++){
                required.getChildren().add(new CargoPiece(view, null, ShipmentType.fromValue(k+1)));
            }
            k++;
        }
        this.cstatetree.getChildren().add(required);
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        HBox awaiting = new HBox(10);
        awaiting.setAlignment(Pos.CENTER);
        awaiting.setMaxWidth(333);
        awaiting.getStyleClass().add("voyage-cargo-rew-label");
        Label message = new Label("Cargo Reward ["+state.getDaysTaken()+" DAYS]: ");
        ImageView piece = new ImageView("galaxy_trucker_imgs/piece/"+state.getTurn().toString()+".png");
        awaiting.getChildren().addAll(message, piece);
        this.cstatetree.getChildren().add(awaiting);
        HBox required = new HBox(10);
        required.setAlignment(Pos.CENTER);
        required.setMaxWidth(333);
        required.getStyleClass().add("voyage-cargo-rew-available");
        int k = 0;
        for(int i : state.getShipments()){
            for(int j = 0; j < i; j++){
                required.getChildren().add(new CargoPiece(view, null, ShipmentType.fromValue(k+1)));
            }
            k++;
        }
        this.cstatetree.getChildren().add(required);
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        Label index = new Label("Combat zone section: "+state.getIndex()+1);
        index.setId("combat-zone-index");
        this.cstatetree.getChildren().add(index);
    }

    @Override
    public void show(ClientCreditsRewardCardStateDecorator state) {
        HBox awaiting = new HBox(10);
        awaiting.setAlignment(Pos.CENTER);
        awaiting.setMaxWidth(333);
        awaiting.getStyleClass().add("voyage-credits-rew-label");
        Label message = new Label("Credits Reward ["+state.getDaysTaken()+" DAYS]: ");
        ImageView piece = new ImageView("galaxy_trucker_imgs/piece/"+state.getTurn().toString()+".png");
        awaiting.getChildren().addAll(message, piece);
        this.cstatetree.getChildren().add(awaiting);
        HBox selection = new HBox(20);
        selection.setAlignment(Pos.CENTER);
        selection.setMaxWidth(333);
        selection.setId("voyage-credits-rew-selection");
        Button accept = new Button("Take reward");
        accept.setOnAction(event -> {
            
        });
    }

    @Override
    public void show(ClientCrewPenaltyCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientEnemyCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

}
