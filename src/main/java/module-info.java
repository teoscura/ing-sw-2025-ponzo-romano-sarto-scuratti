module it.polimi.ingsw {
	requires javafx.controls;
	requires java.rmi;
	requires org.jline;
    requires javafx.graphics;
    requires javafx.base;

	exports it.polimi.ingsw;
	exports it.polimi.ingsw.controller.client.state;
	exports it.polimi.ingsw.model.client.state;
	opens it.polimi.ingsw to javafx.fxml, javafx.graphics, org.jline;
	opens it.polimi.ingsw.view.gui to javafx.graphics, javafx.fxml;
}





