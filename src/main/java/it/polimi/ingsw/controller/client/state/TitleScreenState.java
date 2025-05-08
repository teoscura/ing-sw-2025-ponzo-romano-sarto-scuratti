package it.polimi.ingsw.controller.client.state;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

public class TitleScreenState extends ClientControllerState {

    private String username;

    public TitleScreenState(ClientController controller, ClientView view){
        super(controller, view);
    }

    @Override
    public void init(){
        view.showTitleScreen(this);
    }

    @Override
    public ClientControllerState getNext() {
        if (!validateUsername(username)){
            view.showTextMessage("Invalid username.");
            return new TitleScreenState(controller, view);
        }
        return new ConnectingState(this.controller, this.view, this.username);
    }

    public void setUsername(String username){
        this.username = username;
        this.transition();
    }

    public void exit(){
        this.controller.close();
    }

    private boolean validateUsername(String username) {
		Pattern allowed = Pattern.compile("^[a-zA-Z0-9_.-]*$");
		Matcher matcher = allowed.matcher(username);
		return matcher.matches();
	}

}
