package it.polimi.ingsw;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiMain extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Galaxy Trucker");
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
