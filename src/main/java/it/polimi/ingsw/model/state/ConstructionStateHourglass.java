package it.polimi.ingsw.model.state;

import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class ConstructionStateHourglass implements Serializable {

	private final Duration period;
	private Instant toggled = null;
	private final int total;
	private int times;

	public ConstructionStateHourglass(int seconds, int times) {
		if (seconds <= 0 || times < 0) throw new IllegalArgumentException();
		this.total = times;
		this.times = times;
		this.period = Duration.ofSeconds(seconds);
	}

	public int timesTotal(){
		return this.total;
	}

	public int timesLeft() {
		return this.times;
	}

	public Duration getDuration(){
		return this.period;
	}

	public void start() {
		this.toggled = Instant.now();
		this.times--;
	}

	public void toggle() throws ForbiddenCallException {
		if (isRunning() || times <= 0) throw new ForbiddenCallException();
		this.times--;
		this.toggled = Instant.now();
	}

	public boolean canAct() {
		boolean canact = this.times >= 1 || Duration.between(toggled, Instant.now()).compareTo(period)<0;
		return canact;
	}

	public boolean isRunning() {
		if (this.toggled == null) return true;
		return Duration.between(Instant.now(), toggled).compareTo(period) >= 0;
	}

	public Instant getInstant() {
		return this.toggled;
	}

}
