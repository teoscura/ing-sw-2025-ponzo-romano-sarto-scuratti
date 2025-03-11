package it.polimi.ingsw.model;

public interface iCardEffect {

	public BiConsumer<iStarShip, Player.Player> apply(iSpaceShip ship);


}
