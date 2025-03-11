package it.polimi.ingsw.model;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class CardEffect implements iCardEffect {
	private BiPredicate<iStarShip, Player.Player> test;
	private BiConsumer<iStarShip, Player.Player> on_satisfy;
	private BiConsumer<iStarShip, Player.Player> on_unsatisfy;


	public BiConsumer<iStarShip, Player.Player> apply(iSpaceShip ship) {
		return null; //TODO
	}
}
