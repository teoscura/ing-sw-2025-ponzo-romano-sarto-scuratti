package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.components.iBaseComponent;

public interface iCommonBoard {
	public iBaseComponent pullComponent();

	public void discardComponent(iBaseComponent c);

	public iBaseComponent pullDiscarded(int i);

}
