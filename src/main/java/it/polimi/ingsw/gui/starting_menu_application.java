package it.polimi.ingsw.gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

        /*GridPane pane = new GridPane();
        Scene scene = new Scene(pane);*/
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/starting_menu_view.fxml")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
