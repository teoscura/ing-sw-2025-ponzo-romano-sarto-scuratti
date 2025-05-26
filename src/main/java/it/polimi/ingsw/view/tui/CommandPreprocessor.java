package it.polimi.ingsw.view.tui;

public class CommandPreprocessor {

	private final TUIView view;

	public CommandPreprocessor(TUIView view) {
		this.view = view;
	}

	//XXX add disconnect command that handles disconnection gracefully.
	public void process(String s) {
		switch (s) {
			case "red", "blue", "green", "yellow":
				view.changeShip(s);
				break;
			case "help":
				view.showHelpScreen();
				break;
			case "stateinfo":
				view.showStateInfo();
				break;
			default:
				forward(s);
				break;
		}
	}

	private void forward(String s) {
		view.setInput(new CommandBuilder(view).build(s));
	}

}
