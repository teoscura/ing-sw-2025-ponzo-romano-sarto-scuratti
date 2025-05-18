package it.polimi.ingsw.view.tui.utils;

import java.util.ArrayList;

import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;

public class ClientWaitingStateFormatter {

    public static ArrayList<String> format(ClientWaitingRoomState state){
        ArrayList<String> res = new ArrayList<>();
        res.add("Waiting Room");
        res.add("Game type: "+state.getType()+" size: "+state.getPlayerList().size());

        for(var e : state.getPlayerList()){
            res.add(e.getColor()+": "+e.getUsername());
        }
        int i = state.getCount().getNumber()-state.getPlayerList().size();
        while(i>0){
            res.add("..");
            i--;
        }
        return res;
    }

}
