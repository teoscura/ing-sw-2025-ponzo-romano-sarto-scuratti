package it.polimi.ingsw;

import it.polimi.ingsw.controller.server.ServerController;

public class ServerMain {

	public static void main(String[] args) {
		//TODO: ip configuration.
		ServerController controller = new ServerController();
		controller.start();
		String line = null;
		do {
			line = System.console().readLine();
		} while (!line.trim().equalsIgnoreCase("stop"));

		controller.interrupt();

		System.exit(0);
	}

}
