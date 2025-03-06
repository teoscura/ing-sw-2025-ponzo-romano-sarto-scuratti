package it.polimi.ingsw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();
        JFrame frame = new JFrame("Label con Immagine e Bottoni");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Layout assoluto


        ImageIcon icon = new ImageIcon("C:\\Users\\utente\\IdeaProjects\\ing-sw-2025-ponzo-romano-sarto-scuratti\\resources\\galaxy_trucker_imgs\\cardboard\\cardboard-1.jpg");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());


        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

        panel.add(label);

        frame.add(panel);
        frame.setVisible(true);
    }
}