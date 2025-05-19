package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientWaitingStateFormatter {

    private static String bottom_line = "━Typed line:━";

    public static void format(TerminalWrapper terminal, ClientWaitingRoomState state){
        ArrayList<String> res = new ArrayList<>();
        res.add("Waiting Room");
        res.add("Game type: "+state.getType()+" | Size: "+state.getCount());

        for(var e : state.getPlayerList()){
            res.add(e.getColor()+": "+e.getUsername());
        }
        int i = state.getCount().getNumber()-state.getPlayerList().size();
        while(i>0){
            res.add("..");
            i--;
        }
        terminal.printCentered(res);

        terminal.print(bottom_line+"━".repeat(128-bottom_line.length()), 30, 0);
        String s = terminal.peekInput();
        s = s.length()<128 ? s.concat(" ".repeat(128-s.length())) : s;
        terminal.print(s,31,0);
    }

}
