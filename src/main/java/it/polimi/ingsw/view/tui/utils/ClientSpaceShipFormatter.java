package it.polimi.ingsw.view.tui.utils;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class ClientSpaceShipFormatter {
    
    static public ArrayList<StringBuffer> formatLarge(ClientSpaceShip ship, String username, PlayerColor color, int score, int credits){
        ArrayList<StringBuffer> tmp = new ArrayList<>(); 
        ClientLargeComponentPrinter p = new ClientLargeComponentPrinter();
        ClientSmallComponentPrinter ps = new ClientSmallComponentPrinter();
        int finallength = 6*7;
        String displayname = trimName(username, color, finallength);
        int index = (finallength-displayname.length())/2;
        String top =    "┌"+"─".repeat(finallength)+"┐ ";
        String bottom = "└"+"─".repeat(finallength)+"┘ ";
        tmp.add(new StringBuffer(top).replace(index, index+displayname.length(), displayname));
        for(int i = 0; i<15; i++){
            tmp.add(new StringBuffer("│"));
        }
        for(int i = 0; i<5; i++){
            for(int j = 0; j<7; j++){
                ShipCoords c = new ShipCoords(ship.getType(), j, i);
                p.reset();
                ship.getComponent(c).showComponent(ps);
                p.setCenter(ps.getComponentStringSmall());
                ship.getComponent(c).showComponent(p);
                List<String> component = ship.getType().isForbidden(c) ? p.getForbidden() : p.getComponentStringsLarge();
                for(int k = 0; k<3; k++){
                    tmp.get(i*3+k+1).append(component.get(k));
                }
            }
            for(int j = 0; j<3; j++){
                tmp.get(i*3+1+j).append("│");
            }
        }
        //FIXME: show ship info on side
        tmp.add(new StringBuffer(bottom));
        return tmp;
    }

    static public ArrayList<StringBuffer> formatSmall(ClientSpaceShip ship, String username, PlayerColor color, int score, int credits){
        ArrayList<StringBuffer> tmp = new ArrayList<>();    
        int finallength = 30;
        String top =    "┌"+"─".repeat(finallength)+"┐";
        String bottom = "└"+"─".repeat(14)+"┴"+"─".repeat(7)+"┴"+"─".repeat(7)+"┘";
        String displayname = trimName(username, color, finallength);
        int index = (finallength-displayname.length())/2 + 1;
        ClientSmallComponentPrinter p = new ClientSmallComponentPrinter();
        tmp.add(new StringBuffer(top).replace(index, index+displayname.length(), displayname));
        //Display the components.
        for(int i = 0; i<5;i++){
            tmp.add(new StringBuffer("│"));
            for(int j = 0; j<7;j++){
                ShipCoords c = new ShipCoords(ship.getType(), j, i);
                p.reset();
                ship.getComponent(c).showComponent(p);
                if(!ship.getType().isForbidden(c) )tmp.get(i+1).append(p.getComponentStringSmall());
                else tmp.get(i+1).append("＃");
            }
        }
        tmp.add(new StringBuffer(bottom));
        for(int i = 0; i<5; i++){
            tmp.get(i+1).append("│"); 
        }
        int totalcrew = 0;
        for(int i : ship.getCrew()) totalcrew+=i;
        tmp.get(1).append(String.format("🔫: %3s│", ship.getCannonPower()));
        tmp.get(1).append(String.format("🚀: %3d", ship.getEnginePower()));
        tmp.get(2).append(String.format("🔋: %3d│", ship.getContainers()[0]));
        tmp.get(2).append(String.format("🧍: %3d", totalcrew));
        tmp.get(3).append(String.format("🟦: %3d│", ship.getContainers()[1]));
        tmp.get(3).append(String.format("🟩: %3d", ship.getContainers()[2]));
        tmp.get(4).append(String.format("🟨: %3d│", ship.getContainers()[3]));
        tmp.get(4).append(String.format("🟥: %3d", ship.getContainers()[4]));
        tmp.get(5).append(String.format("💰: %3d│", score));
        tmp.get(5).append(String.format("✨: %3d", credits));
        for(int i = 0; i<5; i++){
            tmp.get(i+1).append("│"); 
        }
        return tmp;
    }

    private static String trimName(String username, PlayerColor color, int space){
        if((username+" - "+color.toString()).length()<=space) return username+" - "+color.toString();
        int trimsize = space - 3 - color.toString().length();
        return username.substring(0, trimsize)+"... - "+color.toString();
    }

    // private static String bottomInfo(ClientSpaceShip ship, int score, int credits){
    //     int totalcrew = 0;
    //     for(int i : ship.getCrew()) totalcrew+=i;
    //     return new AttributedStringBuilder()
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🔫: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
    //         .append(String.format("%3s│", ship.getCannonPower()))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🚀: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
    //         .append(String.format("%3d│", ship.getEnginePower()))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🔋: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
    //         .append(String.format("%3d│", ship.getContainers()[0]))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🧍: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
    //         .append(String.format("%3d", totalcrew))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🟦: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
    //         .append(String.format("%3d│", ship.getContainers()[1]))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🟩: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
    //         .append(String.format("%3d│", ship.getContainers()[2]))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🟨: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
    //         .append(String.format("%3d│", ship.getContainers()[3]))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🟥: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
    //         .append(String.format("%3d│", ship.getContainers()[4]))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("💰: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
    //         .append(String.format("%3d│", score))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("✨: ")
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
    //         .append(String.format("%3d│", credits))
    //         .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
    //         .append("🛡️: ")
    //         .style(AttributedStyle.BOLD.foreground(ship.getShielded()[0] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
    //         .append("N")
    //         .style(AttributedStyle.BOLD.foreground(ship.getShielded()[1] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
    //         .append("E")
    //         .style(AttributedStyle.BOLD.foreground(ship.getShielded()[2] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
    //         .append("S")
    //         .style(AttributedStyle.BOLD.foreground(ship.getShielded()[3] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
    //         .append("W").toAttributedString().toAnsi();
    // }

}
