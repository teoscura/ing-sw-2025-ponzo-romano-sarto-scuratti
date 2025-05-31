module it.polimi.ingsw {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.rmi;
	requires org.jline;

	opens it.polimi.ingsw to javafx.fxml;
	opens it.polimi.ingsw.view.gui to javafx.graphics, javafx.fxml;
	exports it.polimi.ingsw;
	exports it.polimi.ingsw.view.gui;
	exports it.polimi.ingsw.controller.client.state;
	exports it.polimi.ingsw.view.gui.controllers;
	opens it.polimi.ingsw.view.gui.controllers to javafx.fxml, javafx.graphics;
}
