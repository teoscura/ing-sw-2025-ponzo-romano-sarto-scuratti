package it.polimi.ingsw.gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class starting_menu_application extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
