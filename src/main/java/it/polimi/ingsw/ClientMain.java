package it.polimi.ingsw;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.view.ClientView;

public class ClientMain {
    
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Jar must be launched with [gui|tui|cb] as arguments only!");
            System.exit(-1);
        }
        ClientView v = null;
        switch(args[0]){
            case "gui": /*Start GUI*/ ; break;
            case "tui": /*start TUI*/ ; break;
            /*XXX remove after testing done*/case "cb": /*start commandbuilder*/; break;
        }
        ClientController c = new ClientController(v);
        while(!c.getClosed()){}
        return;
    }

}
