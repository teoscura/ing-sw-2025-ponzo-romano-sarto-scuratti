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
        bg.setOpacity(0.4);

        
        var stats = statsBar(username, ship, credits, alive, disconnected);
        StackPane sp = new StackPane(bg, res, stats);
        sp.setMaxWidth(840);
        sp.setMaxHeight(768);
        sp.setAlignment(Pos.CENTER);
        StackPane.setAlignment(stats, Pos.BOTTOM_CENTER);
        sp.setId("ship");
        return sp;
    }

    static public Node statsBar(String username, ClientSpaceShip ship, int credits, boolean alive, boolean disconnected){
        //TODO: immagini al posto delle emoji.
        StackPane sb = new StackPane();
        HBox values = new HBox(10);
        values.setMaxWidth(700);
        sb.setMaxWidth(700);
        sb.setMaxHeight(80);
        var s = new Rectangle(700, 70, new Color(169/255f,169/255f,169/255f,0.45));
        sb.getChildren().add(s);
        s.getStyleClass().add("ui-rectangle");
        int totalcrew = 0;
        for(var i : ship.getCrew()) totalcrew+=i;
        String tail = "";
        tail += disconnected ? "disconnected - " : "";
        tail += alive ? "alive" : "retired";
        ImageView gun = new ImageView("galaxy_trucker_imgs/powerc.png");
        gun.setPreserveRatio(true);
        gun.setFitWidth(50);
        Label gunv = new Label(Double.toString(ship.getCannonPower()));
        ImageView engine = new ImageView("galaxy_trucker_imgs/powere.png");
        engine.setPreserveRatio(true);
        engine.setFitWidth(50);
        Label enginev = new Label(Integer.toString(ship.getEnginePower()));

        ImageView crew = new ImageView("galaxy_trucker_imgs/crewt.png");
        crew.setPreserveRatio(true);
        crew.setFitWidth(50);
        Label crewv = new Label(Integer.toString(totalcrew));

        ImageView brown = new ImageView("galaxy_trucker_imgs/crewb.png");
        brown.setPreserveRatio(true);
        brown.setFitWidth(50);
        Label brownv = new Label(Integer.toString(ship.getCrew()[1]));

        ImageView purple = new ImageView("galaxy_trucker_imgs/crewp.png");
        purple.setPreserveRatio(true);
        purple.setFitWidth(50);
        Label purplev = new Label(Integer.toString(ship.getCrew()[2]));

        ImageView money = new ImageView("galaxy_trucker_imgs/money.png");
        money.setPreserveRatio(true);
        money.setFitWidth(50);
        Label moneyv = new Label(Integer.toString(credits));
        Label usernamel = new Label(username);
        Label label = new Label(tail);
        values.getChildren().addAll(usernamel, gun, gunv, engine, enginev, crew, crewv, brown, brownv, purple, purplev, money, moneyv, label);
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
        bg.setOpacity(0.4);

        var planche = planche(state);
        var stats = statsBar(p.getUsername(), ship, p.getCredits(), !p.isRetired(), p.isDisconnected());
        StackPane sp = new StackPane(planche, bg, res, stats);
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
