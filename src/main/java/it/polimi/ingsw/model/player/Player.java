package it.polimi.ingsw.model.player;



public class Player {
	private int credits;
	private int[] crew = new int[3];
	private int shields;
	private int distance;
	private PlayerColor color;
}

enum PlayerColor {
	RED, 
	BLUE, 
	GREEN, 
	YELLOW;
}
