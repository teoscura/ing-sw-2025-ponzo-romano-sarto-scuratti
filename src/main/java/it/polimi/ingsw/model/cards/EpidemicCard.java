//Done.
package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.state.EpidemicState;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

import java.util.ArrayList;

public class EpidemicCard extends Card {

	public EpidemicCard(int id) {
		super(id, 0);
	}

	@Override
	public CardState getState(VoyageState state) {
		return new EpidemicState(state, this);
	}


	public void apply(VoyageState state, Player p) throws PlayerNotFoundException {
		if (state == null || p == null) throw new NullPointerException();
		ArrayList<ShipCoords> ill_cabins = p.getSpaceShip().findConnectedCabins();
		CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
		for (ShipCoords s : ill_cabins) {
			try {
				p.getSpaceShip().getComponent(s).check(v);
			} catch (IllegalTargetException e) {
				//Do nothing, if its empty its fine.
			}
		}
		p.getSpaceShip().updateShip();
	}

}
