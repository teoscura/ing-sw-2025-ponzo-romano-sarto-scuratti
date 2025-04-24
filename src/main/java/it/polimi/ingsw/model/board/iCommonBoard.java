package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.List;

import it.polimi.ingsw.model.components.iBaseComponent;

public interface iCommonBoard extends Serializable {
	int getCoveredSize();

	iBaseComponent pullComponent();

	void discardComponent(iBaseComponent c);

	iBaseComponent pullDiscarded(int id);

	List<Integer> getDiscarded();
}
