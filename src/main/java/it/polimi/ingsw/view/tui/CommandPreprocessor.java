package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.player.PlayerColor;

public class CommandPreprocessor {
    
    private final TUIView view;

	public CommandPreprocessor(TUIView view){
		this.view = view;
	}

    public void process(String s){
        String[] parts = s.split(" ", 16);
        switch(s){
            case "ship": if(parts.length!=2) forward(s);
                view.setColor(parseColor(parts[1]));
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
        new CommandBuilder(view).build(s);
    }

    private PlayerColor parseColor(String s){
        switch(s){
            case "red":    return PlayerColor.RED;
            case "blue":   return PlayerColor.BLUE;
            case "green":  return PlayerColor.GREEN;
            case "yellow": return PlayerColor.YELLOW;
            default: return PlayerColor.NONE;
        }
    }

}
