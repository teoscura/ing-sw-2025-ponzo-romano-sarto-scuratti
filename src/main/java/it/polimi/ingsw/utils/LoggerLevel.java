package it.polimi.ingsw.utils;

public enum LoggerLevel {
	OFF  (8, "\033[0m"),              //No messages are ever shown
	ERROR(7, "\u001B[41m\033[1;93m"), //Only errors are shown
	WARN (6, "\033[1;33m"), 		   //Only warnings and up are shown
	NOTIF(5, "\033[1;36m"), 		   //Only status changes and upare shown
	SERVR(4, "\033[0;33m"), 		   //Only info from the network and up is shown
	LOBSL(3, "\033[1;94m"), 		   //Only info from lobby select and up is shown
	LOBCN(3, "\033[0;34m"), 		   //Only info from lobby select and up is shown
	MODEL(2, "\033[0;37m"), 		   //Only info from the model instances and up is shown
	DEBUG(1, "\033[0;37m"),		   //Only debug info and up is shown
	ALL  (0, "\033[0m"); 			   //Every message is shown.

	private final int status;
	private final String color;

	LoggerLevel(int status, String color) {
		this.status = status;
		this.color = color;
	}

	static String reset(){
		return "\033[0m";
	}

	public int status() {
		return this.status;
	}

	public String color(){
		return this.color;
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
