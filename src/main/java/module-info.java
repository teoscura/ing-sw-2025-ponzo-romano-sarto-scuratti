module it.polimi.provafinale {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.provafinale to javafx.fxml;
    exports it.polimi.provafinale;
}