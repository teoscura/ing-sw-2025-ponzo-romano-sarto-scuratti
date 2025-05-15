package it.polimi.ingsw.view.commandbuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.ingsw.controller.client.state.ConnectedState;

public class InputCommandTask extends Thread {

    private final ConnectedState cc;

    public InputCommandTask(ConnectedState cc){
        this.cc = cc;
    }
    
    public void run(){
        CommandBuilder cb = new CommandBuilder();
        while(true){
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            try {
                while(!r.ready()){};
                String s = r.readLine();
                if(s.equals("exit")) break;
                cc.sendMessage(cb.build(s));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException | IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }

}
