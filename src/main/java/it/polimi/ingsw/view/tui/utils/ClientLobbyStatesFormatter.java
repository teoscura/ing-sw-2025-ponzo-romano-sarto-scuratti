package it.polimi.ingsw.view.tui.utils;

import java.util.ArrayList;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.ClientGameListEntry;

public class ClientLobbyStatesFormatter {
    
    public static ArrayList<String> format(ClientLobbySelectState state){
        ArrayList<String> res = new ArrayList<>();
        res.add("Available lobbies:");
        for(ClientGameListEntry e : state.getLobbyList()){
            boolean full = e.getCount().getNumber() == e.getPlayers().size();
            AttributedStringBuilder t = new AttributedStringBuilder()
                .style(AttributedStyle.BOLD.foreground(full ? AttributedStyle.BLUE : AttributedStyle.GREEN))
                .append(Integer.toString(e.getModelId()))
                .style(AttributedStyle.DEFAULT)
                .append(" - ")
                .style(AttributedStyle.BOLD.foreground(e.getType() == GameModeType.LVL2 ? AttributedStyle.RED : AttributedStyle.GREEN))
                .append(e.getType().toString())
                .style(AttributedStyle.BOLD.foreground(full ? AttributedStyle.BLUE : AttributedStyle.GREEN))
                .append("| "+e.getPlayers().size()+"/"+e.getCount().getNumber())
                .style(AttributedStyle.DEFAULT)
                .append(" - ");
            for(String s : e.getPlayers()){
                t.append(s+" ");
            }
            res.add(t.toAttributedString().toAnsi());
        }
        return res;
    }

    public static ArrayList<String> format(ClientSetupState state){
        ArrayList<String> res = new ArrayList<>();
        res.add("Unfinished games:");
        for(ClientGameListEntry e : state.getUnfinishedList()){
            AttributedStringBuilder t = new AttributedStringBuilder()
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
                .append(""+e.getModelId())
                .style(AttributedStyle.DEFAULT)
                .append(" - ")
                .style(AttributedStyle.BOLD.foreground(e.getType() == GameModeType.LVL2 ? AttributedStyle.RED : AttributedStyle.GREEN))
                .append(e.getType().toString())
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
                .append("| "+e.getCount())
                .style(AttributedStyle.DEFAULT)
                .append(" - ");
            for(String s : e.getPlayers()){
                t.append(s+" ");
            }
            res.add(t.toAttributedString().toAnsi());
        }
        return res;
    }

}
