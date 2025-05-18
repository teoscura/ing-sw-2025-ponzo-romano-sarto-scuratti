package it.polimi.ingsw;

import java.io.IOException;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.TUIView;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Jar must be launched with [gui|tui] as arguments only!");
			System.exit(-1);
		}
		ClientView v = null;
		switch (args[0]) {
			case "gui": /*Start GUI*/
				break;
			case "tui":
				v = new TUIView();
				break;
			default:
				System.out.println("Jar must be launched with [gui|tui|cb] as arguments only!");
				System.exit(-1);
		}
		ClientController c = new ClientController(v);
		while (!c.getClosed()) {
		}
	}

}
