module it.polimi.ingsw {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.rmi;
	requires org.jline;

	opens it.polimi.ingsw to javafx.fxml;
	opens it.polimi.ingsw.gui to javafx.graphics;
	exports it.polimi.ingsw;
	exports it.polimi.ingsw.gui;
}
