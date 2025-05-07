package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ClientControllerState;
import it.polimi.ingsw.controller.client.state.TitlesScreenState;
import it.polimi.ingsw.view.ClientView;

public class ClientController {

	private ClientControllerState state;
	private boolean closed = false;

	public ClientController(ClientView view) {
		this.state = new TitlesScreenState(this, view);
	}

	public ClientControllerState getState(){
		return this.state;
	}

    public void setState(ClientControllerState next) {
        this.state = next;
		this.state.init();
    }

	public void close(){
		this.closed = true;
	}

	public boolean getClosed(){
		return this.closed;
	}

}
