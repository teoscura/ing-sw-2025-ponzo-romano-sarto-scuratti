package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.message.server.EnterLobbyMessage;
import it.polimi.ingsw.message.server.EnterSetupMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.view.gui.GUIView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.List;

public class LobbyStateTreeFactory {

	public static Node createLobbyScreen(ClientLobbySelectState state, GUIView view) {
		ListView<Node> list = new ListView<>();
		ObservableList<Node> l = FXCollections.observableArrayList();
		for (var e : state.getLobbyList()) {
			l.add(getEntry(e.getModelId(), e.getType(), e.getPlayers(), view));
		}
		list.setItems(l);
		list.setMaxWidth(700);
		Button confirm = new Button("Enter Setup");
		confirm.setOnMouseClicked(event -> {
			view.sendMessage(new EnterSetupMessage());
		});
		var res = new VBox(10.0, list, confirm);
		res.setAlignment(Pos.CENTER);
		res.setMaxHeight(800);
		return res;
	}

    static public StackPane getEntry(int id, GameModeType t, List<String> usernames, GUIView view){
        StackPane res = new StackPane();
        res.setMaxWidth(400);
        res.setMaxHeight(90);
         
        Color fill = t.getLevel()==2 ? new Color(163/255f, 5/255f, 63/255f, 1.0): new Color(13/255f, 88/255f, 209/255f, 1.0);
        Rectangle icon = new Rectangle(450, 150, fill);
        icon.getStyleClass().add("ui-rectangle");
        String emoji = t.getLevel() == 2 ? "  🚀  " : "  🛰️  ";
        Label decorator = new Label(emoji);
        decorator.setFont(new Font(30.0));
        String f = ""; int i = 0;
        for(var u : usernames){
            if(i==2) f+= "\n";
            f += u;
            f += " ";
            i++;
        }
        Label l = new Label(f);
        l.setFont(new Font(20.0));  
        res.getChildren().addAll(icon, decorator, l);
        StackPane.setAlignment(icon, Pos.CENTER);
        StackPane.setAlignment(decorator, Pos.CENTER_LEFT);
        res.setOnMouseClicked(event -> {
            view.sendMessage(new EnterLobbyMessage(id));
        });
        return res;
    }

}
