package it.polimi.ingsw.controller.client.state;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

public class TitlesScreenState extends ClientControllerState {

    private String username;

    //XXX orribile.

    public TitlesScreenState(ClientController controller, ClientView view){
        super(controller, view);
    }

    @Override
    public void init(){
        view.showTitleScreen(this);
    }

    @Override
    public ClientControllerState getNext() {
        return new ConnectingState(this.controller, this.view, this.username);
    }

    @Override
    public void nextStep() {
        if(!this.validateUsername(this.username)){
            view.showTextMessage("Username is invalid! Username should only contain letters, numbers or '_','.','-'.");
            return;
        }
        this.transition();
    }

    public void setUsername(String username){
        this.username = username;
        nextStep();
    }

    private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}



}
