package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;
import java.util.List;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientVerifyStateFormatter {

    private static String bottom_line = "━Typed line:━";
    
    static public void format(TerminalWrapper terminal, ClientVerifyState state, PlayerColor color){
        ArrayList<ClientVerifyPlayer> list = new ArrayList<>(state.getPlayerList());
        ClientVerifyPlayer p = list.stream().filter(pl->pl.getColor()==color).findAny().orElse(null);
        if(p!=null) terminal.print(ClientSpaceShipFormatter.formatLarge(p.getShip(), p.getUsername(), p.getColor(), 0, false),2, 5);
        else terminal.print(ClientSpaceShipFormatter.getEmptyShipLarge(), 2, 4);
        list.remove(p);
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false), 3, 60);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 60);
        }
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false), 11, 60);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 11, 60);
        }
        if(list.size()>0) {
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), 0, false), 3, 94);    
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 94);
        }
        terminal.print(ClientSpaceShipFormatter.getHelpCorner(), 11, 94);
    }

    public static void formatStatus(TerminalWrapper terminal, ClientVerifyState state){
        terminal.print(getMissingLine(state).toAnsi(), 29, 0);
        terminal.print(new AttributedStringBuilder().append(bottom_line+"━".repeat(128-bottom_line.length())).toAttributedString().toAnsi(), 30, 0);
        terminal.setStatus(List.of(new AttributedStringBuilder().append(terminal.peekInput()).toAttributedString()));
    }

    static private AttributedString getMissingLine(ClientVerifyState state){
        AttributedStringBuilder res = new AttributedStringBuilder();
        boolean ongoing = true;
        res.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
                .append("| Current finishing order: ");
        ArrayList<ClientVerifyPlayer> list = new ArrayList<>(state.getPlayerList());
        list.stream().sorted((p1,p2)->Integer.compare(p1.getOrder(), p2.getOrder()));
        for(ClientVerifyPlayer p : list){
            if(!p.isFinished()) continue;
            ongoing = false;
            res.style(AttributedStyle.BOLD.foreground(getColor(p.getColor())))
                .append(p.getColor().toString())
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
                .append(" | ");
        }
        if(ongoing) res.append("none yet.");
        return res.toAttributedString();
    }

    static private int getColor(PlayerColor color){
        switch(color){
            case BLUE:
                return AttributedStyle.BLUE;
            case GREEN:
                return AttributedStyle.GREEN;
           case RED:
                return AttributedStyle.RED;
            case YELLOW:
                return AttributedStyle.YELLOW;
            default:
                return AttributedStyle.WHITE;
        }
    }

}
