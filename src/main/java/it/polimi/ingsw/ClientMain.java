package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.commandbuilder.CBView;

public class ClientMain {

	public static void main(String[] args) {
		if (args.length != 1) {
			/*XXX*/System.out.println("Jar must be launched with [gui|tui|cb] as arguments only!");
			System.exit(-1);
		}
		ClientView v = null;
		switch (args[0]) {
			case "gui": /*Start GUI*/
				break;
			case "tui": /*start TUI*/
				break;
			case "cb":
				v = new CBView();
				break;
		}
		ClientController c = new ClientController(v);
		while (!c.getClosed()) {
		}
	}

}
