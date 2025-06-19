package it.polimi.ingsw.view.gui;

import java.time.Duration;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * Represents a text message to be shown on the GUI that disappears after a certain {@link Duration}.
 */
public class GUINotification extends StackPane {

	private final int seconds;

	/**
	 * Constructs a {@link GUINotification} object.
	 * 
	 * @param text Text to show.
	 * @param seconds Number of seconds it lasts.
	 */
	public GUINotification(String text, int seconds) {
		if (text == null) throw new NullPointerException();
		this.seconds = seconds;
        Rectangle notifback = new Rectangle(400, 100, new Color(106/255f, 157/255f, 235/255f, 0.8));
        notifback.getStyleClass().add("ui-rectangle");
        this.getChildren().add(notifback);
        StackPane.setAlignment(notifback, Pos.CENTER);
        Label notiftext = new Label(text);
		notiftext.setAlignment(Pos.CENTER);
		notiftext.setTextAlignment(TextAlignment.CENTER);
		StackPane.setAlignment(notiftext, Pos.CENTER);
        notiftext.setWrapText(true);
        notiftext.getStyleClass().add("notif-text");
        this.getChildren().add(notiftext);
	}

	public int seconds(){
		return this.seconds;
	}
    
}