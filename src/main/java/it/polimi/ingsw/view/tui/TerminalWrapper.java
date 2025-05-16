package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.Widget;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.Status;

public class TerminalWrapper extends Thread {

    private final Terminal terminal;
    private final Size size;
    private final BindingReader reader;
    private final Status status;
    private StringBuffer command;
    
    private static String bottom_line = "━Typed command:━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

    public TerminalWrapper() throws IOException{
        //Cleaner c = Cleaner.create();
        this.command = new StringBuffer();
        this.terminal = TerminalBuilder.builder().name("Galaxy Truckers")
            .system(true)
            .streams(System.in, System.out)
            .encoding(Charset.forName("UTF-8"))
            .build();
        /*Attributes a = */this.terminal.enterRawMode();
        this.reader = new BindingReader(terminal.reader());
        this.status = Status.getStatus(terminal);
        // TODO cleaner that closes terminal and replaces attributes.
        // TODO resize hook that resets size to 128 32
        this.size = new Size(128, 32);
        terminal.setSize(size);  
        this.start();
    }

    @Override
    public void run(){
        KeyMap<Widget> km = setupBindings();
        while(command.length()<100){
            clearBase();
            print("WOW "+command.length(), 0, 0);
            reader.readBinding(km).apply();
            updateStatus();
        }
        System.exit(0);
    }

    private void clearBase(){
        for(int i = 0; i<30;i++){
            this.terminal.puts(Capability.cursor_address, i, 0);
            this.terminal.puts(Capability.clr_eol);
        }
        print(bottom_line, 30, 0);

    }

    private void updateStatus(){
        if (status != null) status.update(Collections.singletonList(new AttributedStringBuilder()
                                    .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
                                    .append(command)
                                    .toAttributedString()));
    }


    private KeyMap<Widget> setupBindings(){
        KeyMap<Widget> km = new KeyMap<>();
        for(String s : KeyMap.range("a-z")){
            Widget w = () -> {
                command.append(s);
                return true;
            };
            km.bind(w, s);
        }
        for(String s : KeyMap.range("0-9")){
            Widget w = () -> {
                command.append(s);
                return true;
            };
            km.bind(w, s);
        }
        Widget clearcommandw = () -> {
            command.delete(0, command.length());
            return true;
        };
        Widget insert_spacew = () -> {
            command.append(" ");
            return true;
        };
        Widget backspacew = () -> {
            if(command.length()==0) return true;
            command.deleteCharAt(command.length()-1);
            return true;
        };
        km.bind(insert_spacew, " ");
        km.bind(backspacew, KeyMap.del());
        km.bind(backspacew, KeyMap.key(terminal, Capability.key_backspace));
        /*FIXME*/ km.bind(clearcommandw, KeyMap.esc());
        km.bind(clearcommandw, "\r");
        return km;
    }

    public void print(String string, int row, int scol){
        this.terminal.puts(Capability.cursor_address, row, scol);
        this.terminal.writer().print(string);
        this.terminal.writer().flush();
    }

    public void puts(Capability capability, Object... params){
        terminal.puts(Capability.clear_screen);
        terminal.flush();
    }

}
