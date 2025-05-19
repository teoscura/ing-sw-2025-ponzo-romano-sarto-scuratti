package it.polimi.ingsw.view.tui;

public class CommandPreprocessor {
    
    private final TUIView view;

	public CommandPreprocessor(TUIView view){
		this.view = view;
	}

    public void process(String s){
        String[] parts = s.split(" ", 16);
        switch(s){
            case "ship": if(parts.length!=2) forward(s);
                view.changeShip(s);
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
