package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.Widget;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.Status;

public class TerminalWrapper {

    private final Terminal terminal;
    private final KeyMap<Widget> keymap;
    private final BindingReader reader;
    private final Status status;

    private Size size;
    private boolean legal;
    private StringBuffer line;
    private String input;
    private ArrayList<AttributedString> status_info;
    
    private String bottom_line = "━Typed line:━";

    public TerminalWrapper() throws IOException{
        Cleaner c = Cleaner.create();
        this.line = new StringBuffer();
        this.status_info = new ArrayList<>();
        this.terminal = TerminalBuilder.builder().name("Galaxy Truckers")
            .system(true)
            .streams(System.in, System.out)
            .encoding(Charset.forName("UTF-8"))
            .build();
        


        Attributes a = this.terminal.enterRawMode();
        c.register(this, ()->cleanUp(a));

        this.reader = new BindingReader(terminal.reader());
        this.status = Status.getStatus(terminal);
        this.keymap = setupBindings();
        this.size = terminal.getSize();
        if(size.getRows()>=32&&size.getColumns()>=128) legal = true;

        terminal.handle(Terminal.Signal.WINCH, signal -> {
            Display display = new Display(terminal, true);
            this.size = terminal.getSize();
            if(size.getRows()<32 || size.getColumns()<128){
                showSmallScreen(size);
                this.legal = false;
                return;
            }
            else display.resize(size.getRows(), size.getColumns());
            legal = true;
            //clearBase();
        });
    }

    public Widget readBinding(){
        return reader.readBinding(this.keymap);
    }

    public boolean isAvailable(){
        return this.input != null;
    }

    public String takeInput(){
        String res = this.input;
        this.input = null;
        return res;
    } 

    public String peekInput(){
        return this.line.toString();
    }


    private KeyMap<Widget> setupBindings(){
        KeyMap<Widget> km = new KeyMap<>();
        for(String s : KeyMap.range("a-z")){
            Widget w = () -> {
                line.append(s);
                return true;
            };
            km.bind(w, s);
        }
        for(String s : KeyMap.range("0-9")){
            Widget w = () -> {
                line.append(s);
                return true;
            };
            km.bind(w, s);
        }
        Widget clearlinew = () -> {
            line.delete(0, line.length());
            return true;
        };
        Widget inserspacw = () -> {
            line.append(" ");
            return true;
        };
        Widget inserdot = () -> {
            line.append(".");
            return true;
        };
        Widget inserdash = () -> {
            line.append("-");
            return true;
        };
        Widget inserunderscore = () -> {
            line.append("_");
            return true;
        };
        Widget backspacew = () -> {
            if(line.length()==0) return true;
            line.deleteCharAt(line.length()-1);
            return true;
        };
        Widget finallinew = () -> {
            if(line.length()==0) return true;
            this.input = line.toString();
            line.delete(0, line.length());
            return true;
        };
        km.bind(inserdot, ".");
        km.bind(inserdash, "-");
        km.bind(inserunderscore, "_");
        km.bind(inserspacw, " ");
        km.bind(backspacew, KeyMap.del());
        km.bind(backspacew, KeyMap.key(terminal, Capability.key_backspace));
        km.bind(clearlinew, KeyMap.esc());
        km.bind(finallinew, "\r");
        return km;
    }

    public void print(String string, int row, int scol){
        if(!legal) return;
        this.terminal.puts(Capability.cursor_address, row, scol);
        this.terminal.writer().print(string);
        this.terminal.writer().flush();
    }

    public void print(Collection<String> lines, int srow, int scol){
        for(String line : lines){
            this.print(line, srow, scol);
            srow++;
        }
    }

    public void printCentered(Collection<String> lines){
        int firstrow = (this.size.getRows()-lines.size())/2;
        for(String line : lines){
            this.print(line, firstrow, (this.size.getColumns()-line.length())/2);
            firstrow++;
        }
    }

    public void updateCommandBar(){
        ArrayList<AttributedString> lines = new ArrayList<>();
        lines.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW)).append("━Typed line:━"+"━".repeat(this.size.getColumns()-"━Typed line:━".length())).toAttributedString());
        lines.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN)).append(this.line.toString()).toAttributedString());
        status.update(lines, legal);
    }

    public void puts(Capability capability, Object... params){
        if(!legal) return;
        terminal.puts(Capability.clear_screen);
        terminal.flush();
    }

    private void cleanUp(Attributes a){
        puts(Capability.clear_screen);
        terminal.setAttributes(a);
    }

    private void showSmallScreen(Size s){
        AttributedString str0 = new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("         WARNING!!         ").toAttributedString();
        AttributedString str1 = new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Terminal size is too small!").toAttributedString();
        AttributedString str2 = new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("  Must be 128x32 minimum!").toAttributedString();
        AttributedString str3 = new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("     Current one is "+s.getColumns()+"x"+s.getRows()+"!").toAttributedString();
        
        puts(Capability.clear_screen);
        int r = s.getRows()/2;
        int c = (s.getColumns()-str1.length())/2;
        print(str0.toAnsi(), r-1, c);
        print(str1.toAnsi(), r, c);
        print(str2.toAnsi(), r+1, c);
        print(str3.toAnsi(), r+2, c);
    }


    // private void clearBase(){
    //     if(!legal) return;
    //     for(int i = 0; i<30;i++){
    //         this.terminal.puts(Capability.cursor_address, i, 0);
    //         this.terminal.puts(Capability.clr_eol);
    //     }
    //     print(bottom_line, 30, 0);
    //     this.terminal.puts(Capability.cursor_address, 31, 0);
    //     this.terminal.puts(Capability.clr_eol);
    //     print(updateStatus().toAnsi(terminal), 31, 0);

    // }

    public void updateStatusTopLines(Collection<AttributedString> extra_lines){
        this.status_info = new ArrayList<>(extra_lines);
    }

    public void updateStatus(){
        ArrayList<AttributedString> newstatus = new ArrayList<>(status_info);
        newstatus.add(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
            .append(bottom_line + "━".repeat(size.getColumns()-bottom_line.length()))
            .toAttributedString());
        newstatus.add(new AttributedStringBuilder()
            .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
            .append(line.toString())
            .toAttributedString());
        status.update(newstatus);
    }

}
