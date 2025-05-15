package it.polimi.ingsw;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

public class ServerMain {

	public static void main(String[] args) {
		if(args.length != 2){
            System.out.println("Jar must be launched with \"*jar name* [tcp address] [rmiport]\"!");
            System.exit(-1);
        }
		//Setup the logger.
		//XXX change logger detail level to NOTIF when turning it project.
		Logger l = Logger.getInstance();
		l.setLevel(LoggerLevel.DEBUG);

		String address = args[0];
		int rmiport = Integer.parseInt(args[1]);

		MainServerController controller = MainServerController.getInstance();
		controller.init(address, rmiport);
		controller.start();
		String line = null;
		do {
			line = System.console().readLine();
		} while (!line.trim().equalsIgnoreCase("stop"));

		controller.interrupt();

		System.exit(0);
	}

}
