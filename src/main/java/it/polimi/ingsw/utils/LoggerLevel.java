package it.polimi.ingsw.utils;

public enum LoggerLevel {
	OFF  (8), //No messages are ever shown
	ERROR(7), //Only errors are shown
	WARN (6), //Only warnings and up are shown
	NOTIF(5), //Only status changes and upare shown
	SERVR(4), //Only info from the network and up is shown
	LOBSL(3), //Only info from lobby select and up is shown
	LOBCN(3), //Only info from lobby select and up is shown
	MODEL(2), //Only info from the model instances and up is shown
	DEBUG(1), //Only debug info and up is shown
	ALL  (0); //Every message is shown.

	private final int status;

	LoggerLevel(int status) {
		this.status = status;
	}

	public int status() {
		return this.status;
	}

	@Override
	public String toString() {
		switch (this) {
			case ALL:
				return "[ALL--] ";
			case DEBUG:
				return "[DEBUG] ";
			case MODEL:
				return "[MODEL] ";
			case LOBCN:
				return "[LOBCN] ";
			case LOBSL:
				return "[LOBSL] ";
			case SERVR:
				return "[SERVR] ";
			case NOTIF:
				return "[NOTIF] ";
			case WARN:
				return "[WARN-] ";
			case ERROR:
				return "[ERROR] ";
			default:
				return null;
		}
	}

}
