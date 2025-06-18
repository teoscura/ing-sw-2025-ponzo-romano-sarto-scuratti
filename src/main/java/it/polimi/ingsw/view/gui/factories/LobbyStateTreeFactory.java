package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.EnterLobbyMessage;
import it.polimi.ingsw.message.server.EnterSetupMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class LobbyStateTreeFactory {

	public static Node createLobbyScreen(ClientLobbySelectState state, GUIView view) {
		ScrollPane list = new ScrollPane();
        VBox contents = new VBox();
        list.setContent(contents);
        list.setMaxWidth(600);
        list.setMaxHeight(550);
        list.setHbarPolicy(ScrollBarPolicy.NEVER);
        list.setVbarPolicy(ScrollBarPolicy.NEVER);
		for (var e : state.getLobbyList()) {
			contents.getChildren().add(getEntry(e.getModelId(), e.getType(), e.getPlayers(), view));
		}
        list.getStyleClass().add("entry-list");
		Button confirm = new Button("Enter Setup");
		confirm.setOnAction(event -> {
			view.sendMessage(new EnterSetupMessage());
		});
		var res = new VBox(10.0, list, confirm);
		res.setAlignment(Pos.CENTER);
		res.setMaxHeight(800);
		return res;
        
	}

    static public StackPane getEntry(int id, GameModeType t, List<String> usernames, GUIView view){
        StackPane sb = new StackPane();
        sb.setMaxWidth(540);
        sb.setMaxHeight(120);
        Rectangle base = new Rectangle(690, 120);
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
        Rectangle textbackdrop = new Rectangle(570, 100, new Color(169/255f,169/255f,169/255f,0.7));
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
        text.setAlignment(Pos.CENTER);
        text.setWrapText(true);
        text.getStyleClass().add("list-label");
        sb.getChildren().add(text);

        sb.setOnMouseClicked(event -> {
            view.sendMessage(new EnterLobbyMessage(id));
        });

        return sb;
    }

}
