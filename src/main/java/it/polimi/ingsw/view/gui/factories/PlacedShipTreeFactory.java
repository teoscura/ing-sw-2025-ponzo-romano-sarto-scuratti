package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.PutComponentMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.tiles.ConstructionTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTile;
import it.polimi.ingsw.view.gui.tiles.PlacedTileFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Factory that exposes a static method to create all Nodes needed to display a {@link it.polimi.ingsw.model.client.components.ClientSpaceShip} and a Planche.
 */
public class PlacedShipTreeFactory {
    
    static public Node createPlacedShip(GUIView view, String username, ClientSpaceShip ship, int credits, boolean alive, boolean disconnected){
        var res = new GridPane();
        res.setGridLinesVisible(true);
        PlacedTileFactory f = new PlacedTileFactory(view);
        for(int j = 0; j < ship.getType().getHeight(); j++){
            for(int i = 0; i< ship.getType().getWidth(); i++){
                var tmp = new ShipCoords(ship.getType(), i, j);
                res.add(f.createTile(tmp, ship.getComponent(tmp)), i, j, 1, 1);
            }
        }
        res.setMaxWidth(700);
        res.setMaxHeight(500);

        res.setOnDragDropped(event -> {
            var src = event.getGestureSource();
            var node = event.getPickResult().getIntersectedNode();
            if(src==null || node == null) return;
            if(!(src instanceof ConstructionTile)) return;
            if(!(node instanceof PlacedTile)) return;
            event.setDropCompleted(true);
            var comp = (ConstructionTile) src;
            var trgt = (PlacedTile) node;
            ServerMessage msg = new PutComponentMessage(comp.getID(), trgt.getCoords(), comp.getRotation());
            view.sendMessage(msg);
            event.setDropCompleted(true);
        });

        res.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        ImageView bg = null;
        if(ship.getType()==GameModeType.TEST) bg = new ImageView("galaxy_trucker_imgs/ship_background_lv1.png");
        else bg = new ImageView("galaxy_trucker_imgs/ship_background_lv2.png");
        bg.setId("ship-bg-img");
        
        var stats = statsBar(username, ship, credits, disconnected);
        StackPane sp = new StackPane(bg, res, stats);
        if(!alive) {
            sp.getChildren().add(new Rectangle(840, 600, new Color(255/255f, 0, 0, 0.2)));
            Label l = new Label("Retired");
            l.getStyleClass().add("button-label");
            sp.getChildren().add(l);
        }
        
        sp.setMaxWidth(840);
        sp.setMaxHeight(768);
        sp.setAlignment(Pos.CENTER);
        StackPane.setAlignment(stats, Pos.BOTTOM_CENTER);
        sp.setId("ship");
        return sp;
    }

    static public Node statsBar(String username, ClientSpaceShip ship, int credits, boolean disconnected){
        StackPane sb = new StackPane();
        HBox values = new HBox(10);
        values.setMaxWidth(850);
        values.setAlignment(Pos.CENTER);
        sb.setMaxWidth(850);
        sb.setMaxHeight(80);
        var s = new Rectangle(800, 70, new Color(169/255f,169/255f,169/255f,0.45));
        sb.getChildren().add(s);
        s.getStyleClass().add("ui-rectangle");
        int totalcrew = 0;
        for(var i : ship.getCrew()) totalcrew+=i;
        String tail = disconnected ? "| discon." : "";
        ImageView gun = new ImageView("galaxy_trucker_imgs/piece/powerc.png");
        gun.setPreserveRatio(true);
        gun.setFitWidth(50);
        Label gunv = new Label(Double.toString(ship.getCannonPower()));
        gunv.getStyleClass().add("list-label");
        ImageView engine = new ImageView("galaxy_trucker_imgs/piece/powere.png");
        engine.setPreserveRatio(true);
        engine.setFitWidth(50);
        Label enginev = new Label(Integer.toString(ship.getEnginePower()));
        enginev.getStyleClass().add("list-label");
        ImageView crew = new ImageView("galaxy_trucker_imgs/piece/crewt.png");
        crew.setPreserveRatio(true);
        crew.setFitWidth(50);
        Label crewv = new Label(Integer.toString(totalcrew));
        crewv.getStyleClass().add("list-label");
        ImageView brown = new ImageView("galaxy_trucker_imgs/piece/crewb.png");
        brown.setPreserveRatio(true);
        brown.setFitWidth(50);
        Label brownv = new Label(Integer.toString(ship.getCrew()[1]));
        brownv.getStyleClass().add("list-label");
        ImageView purple = new ImageView("galaxy_trucker_imgs/piece/crewp.png");
        purple.setPreserveRatio(true);
        purple.setFitWidth(50);
        Label purplev = new Label(Integer.toString(ship.getCrew()[2]));
        purplev.getStyleClass().add("list-label");
        ImageView money = new ImageView("galaxy_trucker_imgs/piece/money.png");
        money.setPreserveRatio(true);
        money.setFitWidth(50);
        Label moneyv = new Label(Integer.toString(credits));
        moneyv.getStyleClass().add("list-label");
        Label usernamel = new Label(username);
        usernamel.getStyleClass().add("list-label");
        Label label = new Label(tail);
        label.getStyleClass().add("list-label");
        values.getChildren().addAll(usernamel, gun, gunv, engine, enginev, crew, crewv, brown, brownv, purple, purplev, money, moneyv, label);
        sb.getChildren().add(values);
        sb.setAlignment(Pos.CENTER);
        return sb;
    }

    static public Node voyageShipPlanche(GUIView view, ClientVoyageState state, PlayerColor color){
        var p = state.getPlayerList().stream().filter(pl->pl.getColor()==color).findAny().orElse(state.getPlayerList().getFirst());
        var ship = p.getShip();

        var res = new GridPane();
        res.setGridLinesVisible(true);
        PlacedTileFactory f = new PlacedTileFactory(view);
        for(int j = 0; j < ship.getType().getHeight(); j++){
            for(int i = 0; i< ship.getType().getWidth(); i++){
                var tmp = new ShipCoords(ship.getType(), i, j);
                res.add(f.createTile(tmp, ship.getComponent(tmp)), i, j, 1, 1);
            }
        }
        res.setMaxWidth(700);
        res.setMaxHeight(500);

        res.setOnDragDropped(event -> {
            var src = event.getGestureSource();
            var node = event.getPickResult().getIntersectedNode();
            if(src==null || node == null) return;
            if(!(src instanceof ConstructionTile)) return;
            if(!(node instanceof PlacedTile)) return;
            event.setDropCompleted(true);
            var comp = (ConstructionTile) src;
            var trgt = (PlacedTile) node;
            ServerMessage msg = new PutComponentMessage(comp.getID(), trgt.getCoords(), comp.getRotation());
            view.sendMessage(msg);
            event.setDropCompleted(true);
        });

        res.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        });

        ImageView bg = null;
        if(ship.getType()==GameModeType.TEST) bg = new ImageView("galaxy_trucker_imgs/ship_background_lv1.png");
        else bg = new ImageView("galaxy_trucker_imgs/ship_background_lv2.png");
        bg.setId("ship-bg-img");

        var planche = planche(state);
        var stats = statsBar(p.getUsername(), ship, p.getCredits(), p.isDisconnected());
        StackPane sp = new StackPane(planche, bg, res, stats);
        if(p.isRetired()) {
            sp.getChildren().add(new Rectangle(840, 600, new Color(255/255f, 0, 0, 0.2)));
            Label l = new Label("Retired");
            l.getStyleClass().add("button-label");
            sp.getChildren().add(l);
        }
        
        sp.setMaxWidth(840);
        sp.setMaxHeight(768);
        sp.setAlignment(Pos.CENTER);
        StackPane.setAlignment(planche, Pos.TOP_CENTER);
        StackPane.setAlignment(stats, Pos.BOTTOM_CENTER);
        sp.setId("ship");
        return sp;
    }

    static private Node planche(ClientVoyageState state){
        
        HBox b = new HBox(0);
        for(int i = 0; i< state.getType().getLength(); i++){
            int lambdavalue = i;
            PlayerColor c = state.getPlayerList().stream().filter(p->p.getPlancheSlot()%state.getType().getLength()==lambdavalue).map(p->p.getColor()).findAny().orElse(PlayerColor.NONE);
            if(c==PlayerColor.NONE){
                ImageView slot = new ImageView("galaxy_trucker_imgs/piece/triangolo.png");
                slot.setPreserveRatio(true);
                slot.setFitWidth(34);
                b.getChildren().add(slot);
            } else {
                ImageView player = new ImageView("galaxy_trucker_imgs/piece/"+c.toString()+".png");
                player.setPreserveRatio(true);
                player.setFitWidth(34);
                b.getChildren().add(player);
            }
        }
        b.setMaxWidth(850);
        b.setMaxHeight(69);
        b.setAlignment(Pos.CENTER);
        var plancherec = new Rectangle(850, 70, new Color(169/255f,169/255f,169/255f,0.45));
        plancherec.getStyleClass().add("ui-rectangle");
        StackPane sb = new StackPane(plancherec, b);
        sb.setMaxWidth(850);
        sb.setMaxHeight(60);
        sb.setAlignment(Pos.CENTER);
        return sb;
    }

}
