package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientConstructionStateFormatter {

    private static String bottom_line = "━Typed line:━";
    
    static public void format(TerminalWrapper terminal, ClientConstructionState state, PlayerColor color){
        ArrayList<ClientConstructionPlayer> list = new ArrayList<>(state.getPlayerList());
        ClientConstructionPlayer p = list.stream().filter(pl->pl.getColor()==color).findAny().orElse(null);
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

        //XXX Hourglass and discarded piles.
        terminal.print(bottom_line+"━".repeat(128-bottom_line.length()), 30, 0);
        terminal.print(terminal.peekInput(),31,0);
    }
}
