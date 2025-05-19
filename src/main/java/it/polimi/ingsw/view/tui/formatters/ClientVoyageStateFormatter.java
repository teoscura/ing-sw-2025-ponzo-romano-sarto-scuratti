package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientVoyageStateFormatter {

    private static String bottom_line = "━Typed line:━";
    
    static public void format(TerminalWrapper terminal, ClientVoyageState state, PlayerColor color){
        ArrayList<String> res = new ArrayList<>();
        ArrayList<ClientVoyagePlayer> list = new ArrayList<>(state.getPlayerList());
        ClientVoyagePlayer p = list.stream().filter(pl->pl.getColor()==color).findAny().orElse(null);
        if(p!=null) terminal.print(ClientSpaceShipFormatter.formatLarge(p.getShip(), p.getUsername(), p.getColor(), p.getCredits(), p.getRetired()),2, 4);
        else terminal.print(ClientSpaceShipFormatter.getEmptyShipLarge(), 2, 4);
        list.remove(p);
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 2, 62);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 2, 62);
        }
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 12, 62);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 12, 62);
        }
        if(list.size()>0) {
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 2, 95);    
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 2, 95);
        }
        terminal.print(ClientSpaceShipFormatter.getHelpCorner(), 12, 95);

        


        ClientCardStateFormatter csf = new ClientCardStateFormatter();
        state.getCardState().showCardState(csf);
        terminal.printCentered(res);
        terminal.print(csf.getFormatted().toAnsi(), 29, 0);
        terminal.print(bottom_line+"━".repeat(128-bottom_line.length()), 30, 0);
        terminal.print(terminal.peekInput(),31,0);
    }

}
