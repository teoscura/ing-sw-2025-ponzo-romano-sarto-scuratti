package it.polimi.ingsw.view.tui;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents a text message to be shown on the TUI that disappears after a certain {@link Duration}.
 */
public class TUINotification {

	private final String text;
	private final Instant timestamp;
	private final Duration ttl;

	/**
	 * Constructs a {@link TUINotification} object.
	 * 
	 * @param text Text to show.
	 * @param timestamp Time of creation.
	 * @param ttl Time to show the notification for.
	 */
	public TUINotification(String text, Instant timestamp, Duration ttl) {
		if (text == null || timestamp == null || ttl == null) throw new NullPointerException();
		this.text = text;
		this.timestamp = timestamp;
		this.ttl = ttl;
	}

	public Instant getTimestamp() {
		return this.timestamp;
	}

	public Duration getTTL() {
		return this.ttl;
	}

	public String getText() {
		return this.text;
	}

}
