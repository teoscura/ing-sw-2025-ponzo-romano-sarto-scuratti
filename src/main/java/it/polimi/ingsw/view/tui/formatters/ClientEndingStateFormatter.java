package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import org.jline.utils.AttributedStringBuilder;

import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientEndingStateFormatter {
    
    private static String bottom_line = "━Typed line:━";

    public static void format(TerminalWrapper terminal, ClientEndgameState state){
        ArrayList<String> res = new ArrayList<>();
        ArrayList<ClientEndgamePlayer> list = new ArrayList<>(state.getPlayerList());
        res.add("Game Results:");
        list.stream().sorted((p1, p2)->Integer.compare(p1.getScore(), p2.getScore()));
        int finished = (int) list.stream().filter(p->p.getPlanche_slot()>=0).count();
        for(var e : state.getPlayerList()){
            AttributedStringBuilder b = new AttributedStringBuilder()
                .append(e.getUsername())
                .append(" - "+e.getColor())
                .append(e.getPlanche_slot()>=0 ? " - #"+(finished-list.indexOf(e))+" - " : " - DNF- ")
                .append(String.format("🟦: %3d | ", e.getShipments()[1]))
                .append(String.format("🟩: %3d | ", e.getShipments()[2]))
                .append(String.format("🟨: %3d | ", e.getShipments()[3]))
                .append(String.format("🟥: %3d | ", e.getShipments()[4]))
                .append(String.format("💰: %3d | "+(e.getPlanche_slot()==-1?"retired":" alive "), e.getCredits()))
                .append(String.format("✨: %3d | ", e.getScore()));
            res.add(b.toAnsi());
        }
        terminal.printCentered(res);
    }

    //XXX add list of people missing to end game.

    public static void formatStatus(TerminalWrapper terminal, ClientEndgameState state){
        terminal.print(" ".repeat(128), 30, 0);
        terminal.print(" ".repeat(128), 31, 0);
        terminal.print(bottom_line+"━".repeat(128-bottom_line.length()), 30, 0);
        terminal.print(terminal.peekInput(), 31, 0);
    }

}
