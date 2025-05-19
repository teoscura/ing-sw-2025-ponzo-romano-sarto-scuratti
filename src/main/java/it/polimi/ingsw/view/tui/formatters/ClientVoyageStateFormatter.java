package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.tui.TerminalWrapper;

public class ClientVoyageStateFormatter {

    private static String bottom_line = "━Typed line:━";
    
    static public void format(TerminalWrapper terminal, ClientVoyageState state, PlayerColor color){
        ArrayList<ClientVoyagePlayer> list = new ArrayList<>(state.getPlayerList());
        ClientVoyagePlayer p = list.stream().filter(pl->pl.getColor()==color).findAny().orElse(null);
        if(p!=null) terminal.print(ClientSpaceShipFormatter.formatLarge(p.getShip(), p.getUsername(), p.getColor(), p.getCredits(), p.getRetired()),2, 5);
        else terminal.print(ClientSpaceShipFormatter.getEmptyShipLarge(), 2, 4);
        list.remove(p);
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 3, 60);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 60);
        }
        if(list.size()>0){
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 11, 60);
            list.removeFirst();
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 11, 60);
        }
        if(list.size()>0) {
            terminal.print(ClientSpaceShipFormatter.formatSmall(list.getFirst().getShip(), list.getFirst().getUsername(), list.getFirst().getColor(), list.getFirst().getCredits(), list.getFirst().getRetired()), 3, 94);    
        } else {
            terminal.print(ClientSpaceShipFormatter.getEmptyShipSmall(), 3, 94);
        }
        terminal.print(ClientSpaceShipFormatter.getHelpCorner(), 11, 94);

        terminal.print(printPlanche(state, color), 22, 4);

        ClientCardStateFormatter csf = new ClientCardStateFormatter();
        state.getCardState().showCardState(csf);

        terminal.print(csf.getFormatted().toAnsi(), 29, 0);
        terminal.print(bottom_line+"━".repeat(128-bottom_line.length()), 30, 0);
        terminal.print(terminal.peekInput(),31,0);
    }

    static private List<String> printPlanche(ClientVoyageState state, PlayerColor color){
        ArrayList<StringBuffer> res = new ArrayList<>();
        ArrayList<ClientVoyagePlayer> list = new ArrayList<>(state.getPlayerList());
        for(int i=0; i<3; i++) res.add(new StringBuffer("│"));
        for(int i=0 ; i<state.getType().getLength(); i++){
            int lambdavalue = i;
            ClientVoyagePlayer pl = list.stream().filter(p->p.getPlancheSlot()%state.getType().getLength()==lambdavalue).findAny().orElse(null);
            res.get(0).append(pl==null?"    ":" "+getColorEmoji(pl.getColor())+" ");
            res.get(1).append("["+String.format("%02d", i)+"]");
            res.get(2).append(" "+(pl!=null?pl.getColor()==color?"^^":"  ":"  ")+" ");
        }
        int padding = (120-res.get(0).length())/2;
        for(int i=0; i<3; i++){
            res.get(i).append("│");
            
            res.get(i).insert(0, " ".repeat(padding));   
        }
        StringBuffer top = new StringBuffer(" ".repeat(padding)+"┌"+"─".repeat(res.get(0).length()-2-padding)+"┐");
        StringBuffer bot = new StringBuffer(" ".repeat(padding)+"└"+"─".repeat(res.get(0).length()-2-padding)+"┘");
        res.addFirst(top);
        res.addLast(bot);
        return res.stream().map(sb->sb.toString()).toList();
    }

    static private String getColorEmoji(PlayerColor color){
        switch(color){
            case BLUE:  return "🟦";
            case GREEN: return "🟩";
            case RED:   return "🟥";
            case YELLOW:return "🟨";
            default:    return "  ";
        }
    }

}
