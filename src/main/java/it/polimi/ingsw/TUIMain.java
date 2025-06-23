package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.tui.TUIView;

import java.io.IOException;

public class TUIMain {

	public static void main(String[] args) throws IOException, InterruptedException {
		ClientController c = new ClientController(new TUIView());
		while (!c.getClosed()) {
			Thread.sleep(1000);
		}
	}

}
