package it.polimi.ingsw.model.player;

public class Player {
	private iSpaceShip spaceship = null;
	private int credits = 0;
	private int[] crew = new int[3];
	private PlayerColor color;

	public Player(){
		this.spaceship = new SpaceShip();
	}
	
}

//TODO add functions for this.


