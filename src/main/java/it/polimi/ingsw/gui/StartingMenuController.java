package it.polimi.ingsw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingMenuController extends Controller {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button confirm_button;
    @FXML
    private TextField name_field;
    @FXML
    private Button connect_button;
    @FXML
    private TextField connection_field;
    @FXML
    private Text connection_prompt;

    String username;

    public void confirm(ActionEvent event) throws IOException {
        username = name_field.getText();
        System.out.println("Username: " + username);
        name_field.setDisable(true);
        confirm_button.setDisable(true);
        connect_button.setDisable(false);
        connect_button.setVisible(true);
        connection_prompt.setVisible(true);
        connection_field.setDisable(false);
        connection_field.setVisible(true);

    }

    public void connect(){

    }
}
