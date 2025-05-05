package it.polimi.ingsw;

import it.polimi.ingsw.controller.server.MainServerController;

public class ServerMain {

	public static void main(String[] args) {
		//TODO: ip configuration.
		
		MainServerController controller = MainServerController.getInstance();
		controller.start();
		String line = null;
		do {
			line = System.console().readLine();
		} while (!line.trim().equalsIgnoreCase("stop"));

		controller.interrupt();

		System.exit(0);
	}

}
