package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

	//private static final SmallModel model = new SmallModel();

	private static GUIView view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException, InterruptedException {

		// roba da riaggiungere
		view = new GUIView(primaryStage);
		ClientController c = new ClientController(view);
		/*while (!c.getClosed()) {
			Thread.sleep(1000);
		}*/
		//TitleScreenController controller = new TitleScreenController(state, this);
		//mockNetworkMessages();
		//model.setListener(controller);

		/*FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/TitleScreenView.fxml"));
		loader.setControllerFactory(f -> {return new TitleScreenController(view);});
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.show();*/

	}
}
