package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.gui.MainApplication;
import it.polimi.ingsw.view.tui.TUIView;
import javafx.application.Application;

import java.io.IOException;

public class ClientMain {

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 1) {
			System.out.println("Jar must be launched with [gui|tui] as arguments only!");
			System.exit(-1);
		}
		ClientView v = null;
		switch (args[0]) {
			case "gui": /*Start GUI*/
				Application.launch(MainApplication.class);
				break;
			case "tui":
				v = new TUIView();
				ClientController c = new ClientController(v);
				while (!c.getClosed()) {
					Thread.sleep(1000);
				}

				break;
			default:
				System.out.println("Jar must be launched with [gui|tui|cb] as arguments only!");
				System.exit(-1);
		}


	}

}
