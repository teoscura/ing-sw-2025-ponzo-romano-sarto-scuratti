package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.client.state.ClientControllerState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.view.ClientView;

public class ClientController {

	private final ClientView view;
	private ClientControllerState state;
	private boolean closed = false;

	public ClientController(ClientView view) {
		this.view = view;
		this.setState(new TitleScreenState(this, view));
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

	public void reset(){
		this.state.onClose();
		this.setState(new TitleScreenState(this, view));
	}

}
