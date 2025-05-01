package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.ArrayList;

import it.polimi.ingsw.model.components.BaseComponent;

public interface iCommonBoard extends Serializable {
	int getCoveredSize();

	BaseComponent pullComponent();

	void discardComponent(BaseComponent c);

	BaseComponent pullDiscarded(int id);

	ArrayList<Integer> getDiscarded();
}
