package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

	private static final SmallModel model = new SmallModel();

	private static GUIView guiView;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		guiView = new GUIView(primaryStage);
		guiView.setup();


		//mockNetworkMessages();
		//model.setListener(controller);
	}
}
