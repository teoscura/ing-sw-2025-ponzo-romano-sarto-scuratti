package it.polimi.ingsw.view.gui.factories;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.utils.SetupOptionsContainer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SetupTreeFactory {
	static public Node createSetupScreen(ClientSetupState state, GUIView view) {
		SetupOptionsContainer setupOptionsContainer = new SetupOptionsContainer();


		Button p2 = new Button("2");
		p2.setOnAction(event -> setupOptionsContainer.setCount(PlayerCount.TWO, p2));

		Button p3 = new Button("3");
		p3.setOnAction(event -> setupOptionsContainer.setCount(PlayerCount.THREE, p3));

		Button p4 = new Button("4");
		p4.setOnAction(event -> setupOptionsContainer.setCount(PlayerCount.FOUR, p4));

		HBox playerNumber = new HBox(10, p2, p3, p4);
		playerNumber.setAlignment(Pos.CENTER);

		Button tf = new Button("Test Flight");
		tf.setOnAction(event -> setupOptionsContainer.setMode(GameModeType.TEST, tf));

		Button l2 = new Button("Level 2");
		l2.setOnAction(event -> setupOptionsContainer.setMode(GameModeType.LVL2, l2));

		HBox gameMode = new HBox(10, tf, l2);
		gameMode.setAlignment(Pos.CENTER);

		Button confirm = new Button("Create lobby");
		confirm.setOnAction(event -> setupOptionsContainer.sendSetup(view));

		VBox res = new VBox(20.0, playerNumber, gameMode, confirm);
		res.setAlignment(Pos.CENTER);
		return res;
	}

}