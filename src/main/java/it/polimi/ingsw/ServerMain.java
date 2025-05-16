package it.polimi.ingsw;

import it.polimi.ingsw.controller.server.MainServerController;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

public class ServerMain {

	public static void main(String[] args) {
		//XXX rework this into a system of tags where arguments are not ordered, but give variables, makes it easier for us to init everything.
		if (args.length < 2 || args.length > 3) {
			System.out.println("Usage: java -jar <jar name> <tcp address> [tcp port] <rmi port>");
			System.exit(-1);
		}
		Logger l = Logger.getInstance();
		l.setLevel(LoggerLevel.MODEL);
		String address = args[0];
		int tcpport = args.length == 3 ? Integer.parseInt(args[1]) : 0;
		int rmiport = Integer.parseInt(args[args.length == 3 ? 2 : 1]);
		if(rmiport <= 0 || tcpport < 0){
			System.out.println("If ports are specified they must be larger than zero!");
			System.exit(-1);
		}
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
