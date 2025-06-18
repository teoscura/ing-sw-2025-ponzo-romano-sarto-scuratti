package it.polimi.ingsw.utils;

import java.io.PrintStream;

/**
 * Singleton class in charge of Logging any message to chat.
 */
public class Logger {

	static private Logger instance = null;

	private final Object stream_lock;
	private PrintStream out;
	private LoggerLevel level = LoggerLevel.MODEL;

	private Logger() {
		this.out = System.out;
		this.stream_lock = new Object();
	}

	static public Logger getInstance() {
		if (instance == null) instance = new Logger();
		return instance;
	}

	static public void reset() {
		instance = null;
	}

	public void setLevel(LoggerLevel level) {
		this.level = level;
	}

	public void setStream(PrintStream stream) {
		this.out = stream;
	}

	/**
	 * Print to the set {@link PrintStream}.
	 * @param level {@link LoggerLevel} Message level.
	 * @param message Message to be written.
	 */
	public void print(LoggerLevel level, String message) {
		if (level.status() < this.level.status()) return;
		synchronized (stream_lock) {
			out.println(level.color() + level + message + LoggerLevel.reset());
		}
	}

}
