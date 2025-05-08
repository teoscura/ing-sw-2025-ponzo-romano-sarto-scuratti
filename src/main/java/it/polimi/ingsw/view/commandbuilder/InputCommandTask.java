package it.polimi.ingsw.view.commandbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.ingsw.controller.client.ClientController;

public class InputCommandTask extends Thread {

    private final ClientController cc;

    public InputCommandTask(ClientController cc){
        this.cc = cc;
    }
    
    public void run(){
        CommandBuilder cb = new CommandBuilder();
        while(true){
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            try {
                while(!r.ready()){};
                String s = r.readLine();
                cc.getState().sendMessage(cb.build(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
