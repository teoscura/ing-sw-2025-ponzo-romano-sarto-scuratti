package it.polimi.ingsw.view.tui;

public class CommandPreprocessor {
    
    private final TUIView view;

	public CommandPreprocessor(TUIView view){
		this.view = view;
	}

    public void process(String s){
        switch(s){
            case "red":
                view.changeShip("red");
                break;
            case "blue":
                view.changeShip("blue");
                break;
            case "green":
                view.changeShip("green");
                break;
            case "yellow":
                view.changeShip("yellow");
                break;
            case "help":
                view.showHelpScreen();
                break;
            default:
                forward(s);
                break;
        }
    }

    private void forward(String s){
        view.setInput(new CommandBuilder(view).build(s));
    }

}
