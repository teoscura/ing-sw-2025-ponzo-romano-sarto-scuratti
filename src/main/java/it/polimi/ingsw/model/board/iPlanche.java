package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

import java.io.Serializable;

/**
 * The planche class handles the movement of the player on the board.
 */
public interface iPlanche extends Serializable {

	/**
	 * Returns the current position on the planche of a player.
	 *
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player being searched.
	 * @return The position of the player being searched, or -1 if the player is not present.
	 */
	int getPlayerPosition(Player p);

	/**
	 * Given a cell number, finds if there are any players at the corrisponding cell on the board.
	 *
	 * @param position Number of the cell to query.
	 * @return {@link it.polimi.ingsw.model.player.Player} The player on the cell, or null if nobody occupies it.
	 */
	Player getPlayerAt(int position);

	/**
	 * Either moves the player forward or backwards by a number of steps equal to rel_change, skipping the players in between.
	 *
	 * @param state {@link it.polimi.ingsw.model.state.VoyageState} The state currently being played.
	 * @param p {@link it.polimi.ingsw.model.player.Player} The player being moved.
	 * @param rel_change The number of steps the player has to move.
	 * 
	 * @throws IllegalArgumentException if the player being moved is not on the Planche.
	 */
	void movePlayer(VoyageState state, Player p, int rel_change);

	boolean checkLapped(Player p);

	void loseGame(Player p);

}
