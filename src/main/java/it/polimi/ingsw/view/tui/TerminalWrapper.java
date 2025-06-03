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

public class TerminalWrapper {

	private final Terminal terminal;
	private final KeyMap<Widget> keymap;
	private final BindingReader reader;
	private final Status status;
	private final Attributes previous;

	private final Object termlock;

	private Size size;
	private final StringBuffer line;
	private String input;
	private boolean legal;

	public TerminalWrapper(TUIView view) throws IOException {
		this.terminal = TerminalBuilder.builder()
				.system(true)
				.encoding(Charset.forName("UTF-8"))
				.ffm(true)
				.build();

		this.termlock = new Object();
		previous = this.terminal.enterRawMode();
		this.terminal.puts(Capability.enter_ca_mode);
		this.terminal.puts(Capability.clear_screen);
		this.terminal.puts(Capability.cursor_invisible);
		Cleaner c = Cleaner.create();
		c.register(this, () -> cleanUp(previous));
		this.status = Status.getStatus(terminal);
		if (status == null) {
			System.out.println("This terminal type doesn't support the status line, terminating.");
			System.exit(-1);
		}
		this.line = new StringBuffer();
		this.keymap = setupBindings(view);
		this.reader = new BindingReader(terminal.reader());
		setupHooks(view);

		legal = true;
		this.size = terminal.getSize();
		if (size.getRows() < 32 || size.getColumns() < 128) {
			this.size = terminal.getSize();
			showSmallScreen(size);
			legal = false;
		}
	}

	private void setupHooks(TUIView view) {
		this.size = terminal.getSize();
		if (size.getRows() < 32 || size.getColumns() < 128) showSmallScreen(size);

		terminal.handle(Terminal.Signal.WINCH, signal -> {
			this.size = terminal.getSize();
			terminal.setSize(size);
			if (size.getRows() < 32 || size.getColumns() < 128) {
				showSmallScreen(size);
				legal = false;
				return;
			}
			legal = true;
		});

		//Makes the JVM close gracefully.
		terminal.handle(Terminal.Signal.INT, signal -> {
			this.cleanUp(previous);
			System.exit(0);
		});
		terminal.handle(Terminal.Signal.QUIT, signal -> {
			this.cleanUp(previous);
			System.exit(0);
		});
	}

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

	public Widget readBinding() {
		return reader.readBinding(this.keymap);
	}

	public boolean isAvailable() {
		return this.input != null;
	}

	public String takeInput() {
		String res = this.input;
		this.input = null;
		return res;
	}

	public String peekInput() {
		return this.line.toString();
	}

	public void print(String string, int row, int scol) {
		if (!legal) return;
		synchronized (this.termlock) {
			this.terminal.puts(Capability.cursor_address, row, scol);
			this.terminal.writer().print(string);
			this.terminal.writer().flush();
		}
	}

	public void print(Collection<String> lines, int srow, int scol) {
		for (String line : lines) {
			this.print(line, srow, scol);
			srow++;
		}
	}

	public void printCentered(Collection<String> lines) {
		int firstrow = (this.size.getRows() - lines.size()) / 2;
		for (String line : lines) {
			this.print(line, firstrow, (this.size.getColumns() - line.length()) / 2);
			firstrow++;
		}
	}

	public void printCenteredCorner(List<String> lines) {
		int firstrow = (this.size.getRows() - lines.size()) / 2;
		this.print(lines, firstrow, (this.size.getColumns() - lines.get(0).length()) / 2);
	}

	public void puts(Capability capability, Object... params) {
		if (!legal) return;
		synchronized (this.termlock) {
			terminal.puts(capability, params);
			terminal.flush();
		}
	}

	public void setStatus(List<AttributedString> lines) {
		status.update(lines);
		terminal.flush();
		status.redraw();
		terminal.flush();
	}

	private void cleanUp(Attributes a) {
		puts(Capability.clear_screen);
		puts(Capability.cursor_visible);
		terminal.setAttributes(a);
		try {
			terminal.close();
		} catch (IOException e) {
		}
	}

	private void showSmallScreen(Size s) {
		ArrayList<AttributedString> res = new ArrayList<>();
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("WARNING!!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Terminal size is too small!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Must be 128x32 minimum!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Current one is " + s.getColumns() + "x" + s.getRows() + "!").toAttributedString());
		res.add(new AttributedStringBuilder().style(AttributedStyle.BOLD.foreground(AttributedStyle.RED)).append("Press any key when resized.").toAttributedString());
		puts(Capability.clear_screen);
		int firstrow = (s.getRows() - res.size()) / 2;
		for (String line : res.stream().map(as->as.toAnsi()).toList()) {
			this.print(line, firstrow, (s.getColumns() - line.length()) / 2);
			firstrow++;
		}
	}

}
