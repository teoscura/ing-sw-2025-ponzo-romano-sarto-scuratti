package it.polimi.ingsw.view.tui;

import java.time.Duration;
import java.time.Instant;

public class TUINotification {

	private final String text;
	private final Instant timestamp;
	private final Duration ttl;

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
