package it.polimi.ingsw.view.tui;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.Widget;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.*;
import org.jline.utils.InfoCmp.Capability;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Wrapper class around a {@link Terminal} object made available by the JLine3 library.
 */
public class TerminalWrapper {

	private final Display display;
	private final Terminal terminal;
	private final KeyMap<Widget> keymap;
	private final BindingReader reader;
	private final Attributes previous;
	private final Object termlock;
	private Size size;
	private final StringBuffer line;
	private String input;
	private boolean legal;

	/**
	 * Constructs a {@link TerminalWrapper} object tied to a {@link TUIView}.
	 * @param view {@link TUIView} View bound to the object.
	 * @throws IOException if the Terminal couldn't be created.
	 */
	public TerminalWrapper(TUIView view) throws IOException {
		this.terminal = TerminalBuilder.builder()
				.system(true)
				.encoding(Charset.forName("UTF-8"))
				.ffm(true)
				.build();
		this.display = new Display(terminal, false);
		this.termlock = new Object();
		previous = this.terminal.enterRawMode();
		this.terminal.puts(Capability.enter_ca_mode);
		this.terminal.puts(Capability.clear_screen);
		this.terminal.puts(Capability.cursor_invisible);
		Cleaner c = Cleaner.create();
		c.register(this, () -> cleanUp(previous));
		this.line = new StringBuffer();
		this.keymap = setupBindings(view);
		this.reader = new BindingReader(terminal.reader());
		setupHooks();

		legal = true;
		this.size = terminal.getSize();
		if (size.getRows() < 32 || size.getColumns() < 128) {
			System.out.println("Terminal requires a starting size of 128r by 32c! You have: "+size.getColumns()+"c by "+size.getRows()+"r!");
			System.exit(0);
		}
	}

	/**
	 * Sets up the hooks to the system signals that could affect the terminal object.
	 */
	private void setupHooks() {
		this.size = terminal.getSize();
		if (size.getRows() < 32 || size.getColumns() < 128) showSmallScreen(size);
		terminal.handle(Terminal.Signal.WINCH, signal -> {
			this.size = terminal.getSize();
			this.terminal.puts(Capability.clear_screen);
			display.resize(this.size.getRows(), this.size.getColumns());
			if (size.getRows() < 32 || size.getColumns() < 128) {
				showSmallScreen(size);
				this.legal = false;
				return;
			}
			this.legal = true;
		});

		//Makes the JVM close gracefully.
		terminal.handle(Terminal.Signal.INT, signal -> {
			this.cleanUp(previous);
			System.exit(0);
		});

		//Makes the JVM close gracefully.
		terminal.handle(Terminal.Signal.QUIT, signal -> {
			this.cleanUp(previous);
			System.exit(0);
		});
	}

	/**
	 * Returns a keymap properly set up for the inputs the {@link TUIView} requires.
	 * 
	 * @param view {@link TUIView} View that will be notified of ESC/Enter keypresses.
	 * @return A properly set-up keymap.
	 */
	private KeyMap<Widget> setupBindings(TUIView view) {
		KeyMap<Widget> km = new KeyMap<>();
		km.setNomatch(()->{return false;});
		for (String s : KeyMap.range("a-z")) {
			Widget w = () -> {
				line.append(s);
				return true;
			};
			km.bind(w, s);
		}
		for (String s : KeyMap.range("0-9")) {
			Widget w = () -> {
				line.append(s);
				return true;
			};
			km.bind(w, s);
		}
		Widget clearlinew = () -> {
			view.resetOverlay();
			line.delete(0, line.length());
			return true;
		};
		Widget inserspacw = () -> {
			line.append(" ");
			return true;
		};
		Widget inserdotw = () -> {
			line.append(".");
			return true;
		};
		Widget inserdashw = () -> {
			line.append("-");
			return true;
		};
		Widget inserunderscorew = () -> {
			line.append("_");
			return true;
		};
		Widget backspacew = () -> {
			if (line.length() == 0) return true;
			line.deleteCharAt(line.length() - 1);
			return true;
		};
		Widget finallinew = () -> {
			if (line.length() == 0) return true;
			this.input = line.toString();
			line.delete(0, line.length());
			view.resetOverlay();
			return true;
		};
		km.bind(inserdotw, ".");
		km.bind(inserdashw, "-");
		km.bind(inserunderscorew, "_");
		km.bind(inserspacw, " ");
		km.bind(backspacew, KeyMap.del());
		km.bind(backspacew, KeyMap.key(terminal, Capability.key_backspace));
		km.bind(clearlinew, KeyMap.esc());
		km.bind(finallinew, "\r");
		return km;
	}

	/**
	 * Reads the first binding made available by the terminal, or waits until one is available.
	 * @return {@link Widget} Widget representing the action tied to the binding.
	 */
	public Widget readBinding() {
		return reader.readBinding(this.keymap);
	}

	/**
	 * Any methods regarding the input string are not synchronized to avoid the overhead of atomic changes,
	 * as There's only one producer (this object) and only one {@link it.polimi.ingsw.view.tui.strategy.TUIStrategy consumer}.
	 * 
	 * @return Whether the input string is available or not.
	 */
	public boolean isAvailable() {
		return this.input != null;
	}

	/**
	 * Returns the input string set by the user.
	 * 
	 * Any methods regarding the input string are not synchronized to avoid the overhead of atomic changes,
	 * as There's only one producer (this object) and only one {@link it.polimi.ingsw.view.tui.strategy.TUIStrategy consumer}.
	 * 
	 * @return The input string.
	 */
	public String takeInput() {
		String res = this.input;
		this.input = null;
		return res;
	}

	/**
	 * Peeks the input string without consuming it.
	 * 
	 * Any methods regarding the input string are not synchronized to avoid the overhead of atomic changes,
	 * as There's only one producer (this object) and only one {@link it.polimi.ingsw.view.tui.strategy.TUIStrategy consumer}.
	 * 
	 * @return The input string.
	 */
	public String peekInput() {
		return this.line.toString();
	}

	/**
	 * Entry point for any external usage of the terminal, Prints to screen starting from the specified cell.
	 * 
	 * @param string String to be printed.
	 * @param row Row of the starting cell.
	 * @param scol Column of the starting cell.
	 */
	public void print(String string, int row, int scol) {
		if(!legal) return;
		synchronized (this.termlock) {
			this.terminal.puts(Capability.cursor_address, row, scol);
			this.terminal.writer().print(string);
			this.terminal.writer().flush();
		}
	}

	/**
	 * Prints a string with an offset from the bottom line.
	 * 
	 * @param string String to be printed.
	 * @param offset Desired number of rows below of the string.
	 */
	public void printBottom(String string, int offset){
		int row = this.size.getRows() - 1 - offset;
		this.print(string, row, 0);
	}

	/**
	 * Prints a collection of strings starting from the specified cell, and moving downwards.
	 * 
	 * @param lines Strings to be printed.
	 * @param srow Row of the starting cell.
	 * @param scol Column of the starting cell.
	 */
	public void print(Collection<String> lines, int srow, int scol) {
		for (String line : lines) {
			this.print(line, srow + this.getRowsOffset(), scol + this.getColsOffset());
			srow++;
		}
	}

	/**
	 * Prints a collection of strings starting centering it automatically.
	 * 
	 * @param lines Strings to be printed.
	 */
	public void printCentered(Collection<String> lines) {
		int firstrow = (this.size.getRows() - lines.size()) / 2;
		for (String line : lines) {
			this.print(line, firstrow, (this.size.getColumns() - line.length()) / 2);
			firstrow++;
		}
	}

	/**
	 * Prints a collection of strings starting centering it automatically based on the first line.
	 * 
	 * @param lines Strings to be printed.
	 */
	public void printCenteredCorner(List<String> lines) {
		int firstrow = (this.size.getRows() - lines.size()) / 2;
		this.print(lines, firstrow, (this.size.getColumns() - lines.get(0).length()) / 2);
	}

	/**
	 * Prints a {@link org.jline.utils.InfoCmp.Capability} on the screen.
	 * 
	 * @param capability {@link org.jline.utils.InfoCmp.Capability} to be printed.
	 * @param params Parameters regarding the capability.
	 */
	public void puts(Capability capability, Object... params) {
		if(!legal) return;
		synchronized (this.termlock) {
			terminal.puts(capability, params);
			terminal.flush();
		}
	}

	/**
	 * Method run at shutdown to return the terminal to the state it was before the TUI launched.
	 * 
	 * @param a {@link Attributes} Attributes representing the previous state of the Terminal.
	 */
	private void cleanUp(Attributes a) {
		puts(Capability.clear_screen);
		puts(Capability.cursor_visible);
		terminal.setAttributes(a);
		try {
			terminal.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Prints warning screen related to insufficient size parameters.
	 * 
	 * @param s {@link Size} Current size of the terminal.
	 */
	private void showSmallScreen(Size s) {
		ArrayList<AttributedString> res = new ArrayList<>();
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("WARNING!!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Terminal size is too small!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Must be 128x32 minimum!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Current one is " + s.getColumns() + "x" + s.getRows() + "!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Press any key when resized.").toAttributedString());
		int firstrow = (s.getRows() - res.size()) / 2;
		for (String line : res.stream().map(as->as.toAnsi()).toList()) {
			this.terminal.puts(Capability.cursor_address, firstrow, (s.getColumns() - line.length()) / 2);
			this.terminal.writer().print(line);
			firstrow++;
		}
	}

	public int getCols(){
		return this.size.getColumns();
	}

	public int getRows(){
		return this.size.getRows();
	}

	public int getColsOffset(){
		int off = (this.size.getColumns() - 128)/2;
		return off;
	}

	public int getRowsOffset(){
		int off = (this.size.getRows() - 32)/2;
		return off;
	}

}
