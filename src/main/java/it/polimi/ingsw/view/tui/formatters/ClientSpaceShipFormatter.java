package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;
import java.util.List;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class ClientSpaceShipFormatter {
    
    static public List<String> formatLarge(ClientSpaceShip ship, String username, PlayerColor color, int credits, boolean retired){
        ArrayList<StringBuffer> tmp = new ArrayList<>(); 
        ClientLargeComponentPrinter p = new ClientLargeComponentPrinter();
        ClientSmallComponentPrinter ps = new ClientSmallComponentPrinter();
        int shipwidth = 6*7;
        String displayname = trimName(username, color, shipwidth);
        int index = (shipwidth-displayname.length())/2;
        String top =    "┌"+"─".repeat(shipwidth)+"┬"+"─".repeat(7)+"┐";
        String bottom = "└──0^────1^────2^────3^────4^────5^────6^──┴"+"─".repeat(7)+"┘";
        tmp.add(new StringBuffer(top).replace(index, index+displayname.length(), displayname));
        for(int i = 0; i<15; i++){
            if((i-1)%3==0) tmp.add(new StringBuffer(Integer.toString(i/3)));
            else tmp.add(new StringBuffer("│"));
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
        tmp.add(new StringBuffer(bottom));
        int totalcrew = 0;
        for(int i : ship.getCrew()) totalcrew+=i;
        tmp.get(1).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append("🔫: ")
            .append(String.format("%3s", ship.getCannonPower()))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(2).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
            .append("🚀: ")
            .append(String.format("%3s", ship.getEnginePower()))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(3).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
            .append("🔋: ")
            .append(String.format("%3s", ship.getContainers()[0]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(4).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
            .append("🧍: ")
            .append(String.format("%3s", totalcrew))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(5).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
            .append("🐻: ")
            .append(String.format("%3s", ship.getCrew()[1]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(6).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.WHITE))
            .append("😈: ")
            .append(String.format("%3s", ship.getCrew()[2]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(7).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
            .append("🟦: ")
            .append(String.format("%3s", ship.getContainers()[1]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(8).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
            .append("🟩: ")
            .append(String.format("%3s", ship.getContainers()[2]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(9).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append("🟨: ")
            .append(String.format("%3s", ship.getContainers()[3]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(10).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
            .append("🟥: ")
            .append(String.format("%3s", ship.getContainers()[4]))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(11).append(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append("💰: ")
            .append(String.format("%3s", credits))
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(12).append(new AttributedStringBuilder()
            .append("🛡️: ")
            .style(AttributedStyle.BOLD.foreground(ship.getShielded()[0] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
            .append("N")
            .style(AttributedStyle.BOLD.foreground(ship.getShielded()[1] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
            .append("E")
            .style(AttributedStyle.BOLD.foreground(ship.getShielded()[2] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
            .append("S")
            .style(AttributedStyle.BOLD.foreground(ship.getShielded()[3] ? AttributedStyle.GREEN : AttributedStyle.BLACK))
            .append("W")
            .style(AttributedStyle.DEFAULT)
            .append("│").toAnsi());
        tmp.get(13).append((retired?"retired":" alive ")+"│");
        tmp.get(14).append("       │");
        tmp.get(15).append("       │");
        return tmp.stream().map(sb->sb.toString()).toList();
    }

    static public List<String> formatSmall(ClientSpaceShip ship, String username, PlayerColor color, int credits, boolean retired){
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
        tmp.get(5).append(String.format("💰: %3d│"+(retired?"retired":" alive "), credits));
        for(int i = 0; i<5; i++){
            tmp.get(i+1).append("│"); 
        }
        return tmp.stream().map(sb->sb.toString()).toList();
    }

    private static String trimName(String username, PlayerColor color, int space){
        if((username+" - "+color.toString()).length()<=space) return username+" - "+color.toString();
        int trimsize = space - 3 - color.toString().length();
        return username.substring(0, trimsize)+"... - "+color.toString();
    }

    static public ArrayList<String> getHelpCorner(){
        ArrayList<String> res = new ArrayList<>();
        res.add("┌"+"─".repeat(30)+"┐");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+"  For more help type 'help'!  "+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("└"+"─".repeat(30)+"┘");
        return res;
    }
    
    static public ArrayList<String> getEmptyShipSmall(){
        ArrayList<String> res = new ArrayList<>();
        res.add("┌"+"─".repeat(30)+"┐");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+"            No Ship.          "+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("│"+" ".repeat(30)+"│");
        res.add("└"+"─".repeat(30)+"┘");
        return res;
    }

    static public ArrayList<String> getEmptyShipLarge(){
        ArrayList<String> res = new ArrayList<>();
        res.add("┌"+"─".repeat(52)+"┐");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(22)+"No Ship."+" ".repeat(22)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("│"+" ".repeat(52)+"│");
        res.add("└"+"─".repeat(52)+"┘");
        return res;
    }

}

