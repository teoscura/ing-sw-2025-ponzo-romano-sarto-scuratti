package it.polimi.ingsw.view.tui.formatters;

import java.util.ArrayList;

import it.polimi.ingsw.view.tui.TerminalWrapper;

public class TextMessageFormatter {

    public static void format(TerminalWrapper terminal, String message){
        ArrayList<String> res = new ArrayList<>();
        int wraplength = 32;
        res.add("╭"+"─".repeat(8)+"  New Message!  "+"─".repeat(8)+"╮");
        StringBuffer remaining = new StringBuffer(message);
        while(remaining.length()>0){
            String line = remaining.substring(0, remaining.length() > wraplength ? wraplength : remaining.length());
            line = line.length() == wraplength ? line : line+" ".repeat(wraplength-line.length());
            res.add("│"+line+"│");
            remaining.delete(0, remaining.length() > wraplength ? wraplength : remaining.length());
        }
        res.add("╰"+"─".repeat(wraplength)+"╯");
        terminal.print(res, 1, 94);
    }
}