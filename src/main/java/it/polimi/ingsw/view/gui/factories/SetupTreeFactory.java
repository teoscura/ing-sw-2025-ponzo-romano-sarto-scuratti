package it.polimi.ingsw.view.gui.factories;


import java.util.List;

import it.polimi.ingsw.message.server.LeaveSetupMessage;
import it.polimi.ingsw.message.server.OpenUnfinishedMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.SetupOptionsContainer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * Factory that exposes a static method to create all Nodes needed to display a {@link it.polimi.ingsw.model.client.state.ClientSetupState}.
 */
public class SetupTreeFactory {

	static public Node createSetupScreen(ClientSetupState state, GUIView view) {
		SetupOptionsContainer options = new SetupOptionsContainer();

		Node p2 = createButton(options, PlayerCount.TWO);
		Node p3 = createButton(options, PlayerCount.THREE);
		Node p4 = createButton(options, PlayerCount.FOUR);
		HBox playerNumber = new HBox(30, p2, p3, p4);
		playerNumber.setAlignment(Pos.CENTER);

		Node tf = createButton(options, GameModeType.TEST);
		Node l2 = createButton(options, GameModeType.LVL2);

		HBox gameMode = new HBox(30, tf, l2);
		gameMode.setAlignment(Pos.CENTER);
		
		HBox bottom = new HBox(25);
		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(leaveButton(view), confirmButton(view, options));

		options.setMode(GameModeType.TEST, tf);
		options.setCount(PlayerCount.TWO, p2);

		VBox res = new VBox(20.0, gameMode, playerNumber);
		res.setAlignment(Pos.CENTER);
		
		var list = createSavedList(state, view);
		HBox res2 = new HBox(30.0, res, list);
		res2.setAlignment(Pos.CENTER);
		VBox align = new VBox(10);
		align.setAlignment(Pos.CENTER);
		align.getChildren().addAll(res2, bottom);
		return align;
	}

	static public Node createButton(SetupOptionsContainer options, PlayerCount c){
		StackPane p = new StackPane();
		p.setMaxWidth(195);
		p.setMaxHeight(150);
		p.setAlignment(Pos.CENTER);
		Label number = new Label(Integer.toString(c.getNumber()));
		number.getStyleClass().add("button-label");
		Rectangle rect = new Rectangle(200, 150, new Color(136/255f, 184/255f, 153/255f, 0.92));
		rect.getStyleClass().add("ui-rectangle");
		p.getChildren().addAll(rect, number);
		p.setOnMouseClicked(event -> {
			options.setCount(c, p);
		});
		return p;
	}

	static public Node createButton(SetupOptionsContainer options, GameModeType type){
		StackPane p = new StackPane();
		p.setMaxWidth(310);
		p.setMaxHeight(310);
		p.setAlignment(Pos.CENTER);
		ImageView logo = new ImageView("galaxy_trucker_imgs/logos/"+type.toString().toLowerCase()+".png");
		logo.setPreserveRatio(true);
		logo.setFitWidth(310);
		Label typel = new Label(type.toString());
		typel.getStyleClass().add("button-label-yellow");
		
		
		Rectangle rect = new Rectangle(310, 310);
		if(type == GameModeType.LVL2) rect.getStyleClass().add("list-red-bg");
		else rect.getStyleClass().add("list-blue-bg");
		rect.getStyleClass().add("ui-rectangle");

		p.getChildren().addAll(rect, logo, typel);
		p.setOnMouseClicked(event -> {
			options.setMode(type, p);
		});
		StackPane.setAlignment(typel, Pos.BOTTOM_CENTER);
		StackPane.setMargin(typel, new Insets(0, 0, 10, 0));
		return p;
	}

	static public Node confirmButton(GUIView view, SetupOptionsContainer options){
		StackPane p = new StackPane();
		p.setMaxHeight(120);
		p.setMaxWidth(250);
		p.setAlignment(Pos.CENTER);
		Rectangle rect = new Rectangle(250, 120);
		rect.getStyleClass().addAll("list-green-bg", "ui-rectangle");
		Label confirml = new Label("Open lobby");
		confirml.getStyleClass().add("button-label-small");

		p.getChildren().addAll(rect, confirml);
		p.setOnMouseClicked(event -> {
			options.sendSetup(view);
		});
		return p;
	}

	static public Node leaveButton(GUIView view){
		StackPane p = new StackPane();
		p.setMaxHeight(120);
		p.setMaxWidth(250);
		p.setAlignment(Pos.CENTER);
		Rectangle rect = new Rectangle(250, 120);
		rect.getStyleClass().addAll("list-red-faint", "ui-rectangle");
		Label confirml = new Label("Leave Setup");
		confirml.getStyleClass().add("button-label-small");

		p.getChildren().addAll(rect, confirml);
		p.setOnMouseClicked(event -> {
			view.sendMessage(new LeaveSetupMessage());
		});
		return p;
	}

	public static Node createSavedList(ClientSetupState state, GUIView view) {
		ScrollPane list = new ScrollPane();
        VBox contents = new VBox();
        list.setContent(contents);
        list.setMaxWidth(445);
        list.setMaxHeight(490);
        list.setHbarPolicy(ScrollBarPolicy.NEVER);
        list.setVbarPolicy(ScrollBarPolicy.NEVER);
		for (var e : state.getUnfinishedList()) {
			contents.getChildren().add(getEntry(e.getModelId(), e.getType(), e.getState(), e.getPlayers(), view));
		}
        list.getStyleClass().add("saved-list");
		contents.getStyleClass().add("saved-list");
		return list;
        
	}

    static public StackPane getEntry(int id, GameModeType t, String state, List<String> usernames, GUIView view){
        StackPane sb = new StackPane();
        sb.setMaxWidth(440);
        sb.setMaxHeight(600);
        Rectangle base = new Rectangle(440, 120);
        base.getStyleClass().add("ui-rectangle");
        switch(t){
            case LVL2:
                base.getStyleClass().add("list-red-bg");
                break;
            case TEST:
                base.getStyleClass().add("list-blue-bg");
                break;
            default:
                base.getStyleClass().add("list-blue-bg");
                break;
        }

        sb.getChildren().add(base);
        StackPane.setAlignment(base, Pos.CENTER);
        Rectangle flourish = new Rectangle(120,120);
        flourish.getStyleClass().add("ui-rectangle");
        switch(t){
            case LVL2:
                base.getStyleClass().add("list-red-faint");
                break;
            case TEST:
                base.getStyleClass().add("list-blue-faint");
                break;
            default:
                base.getStyleClass().add("list-blue-faint");
                break;
        }
        sb.getChildren().add(flourish);
        StackPane.setAlignment(flourish, Pos.CENTER_LEFT);
        ImageView piece = new ImageView("galaxy_trucker_imgs/logos/"+t.toString().toLowerCase()+".png");
        piece.setPreserveRatio(true);
        piece.setFitWidth(120);
        sb.getChildren().add(piece);
        StackPane.setAlignment(piece, Pos.CENTER_LEFT);
        Rectangle textbackdrop = new Rectangle(320, 100, new Color(169/255f,169/255f,169/255f,0.7));
        textbackdrop.getStyleClass().add("ui-rectangle");
        sb.getChildren().add(textbackdrop);
        StackPane.setAlignment(textbackdrop, Pos.CENTER_RIGHT);
        String f = ""; int i = 0;
        for(var u : usernames){
            if(i==2) f+= "\n";
            f += u;
            f += " ";
            i++;
        }
        Label text = new Label(f);
		Label statel = new Label(state);
        text.setAlignment(Pos.CENTER);
        text.setWrapText(true);
		text.setTextAlignment(TextAlignment.CENTER);
        text.getStyleClass().add("list-label");
		statel.setAlignment(Pos.CENTER);
        statel.setWrapText(true);
        statel.getStyleClass().add("list-label");
		VBox labels = new VBox(1.0, text, statel);
		labels.setAlignment(Pos.CENTER);
		labels.setMaxHeight(110);
        sb.getChildren().add(labels);
	
		StackPane.setMargin(labels, new Insets(0, 0, 0, 120));

        sb.setOnMouseClicked(event -> {
            view.sendMessage(new OpenUnfinishedMessage(id));
        });

        return sb;
    }


}