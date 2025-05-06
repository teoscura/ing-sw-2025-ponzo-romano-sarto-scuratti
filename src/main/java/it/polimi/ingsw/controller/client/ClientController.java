package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ClientControllerState;
import it.polimi.ingsw.controller.client.state.TitlesScreenState;
import it.polimi.ingsw.view.ClientView;

public class ClientController {

	private ClientControllerState state;

	public ClientController(ClientView view) {
		this.state = new TitlesScreenState(this, view);
	}

    public void setState(ClientControllerState next) {
        this.state = next;
		this.state.init();
    }

}
