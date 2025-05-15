package it.polimi.ingsw;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

public class ServerMain {

	public static void main(String[] args) {
		if(args.length < 2 || args.length > 3){
            System.out.println("Jar must be launched with \"<jar name> <tcp address> [tcp port] <rmi port>\" !");
            System.exit(-1);
        }
		Logger l = Logger.getInstance();
		l.setLevel(LoggerLevel.DEBUG);
		String address = args[0];
		int tcpport = args.length == 3 ? Integer.parseInt(args[1]) : 0;
		int rmiport = Integer.parseInt(args[args.length == 3? 2 : 1]);
		MainServerController controller = MainServerController.getInstance();
		controller.init(address, tcpport, rmiport);
		controller.start();
		String line = null;
		do {
			line = System.console().readLine();
		} while (!line.trim().equalsIgnoreCase("stop"));

		controller.interrupt();

		System.exit(0);
	}

}
