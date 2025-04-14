package it.polimi.ingsw.model.board;

import java.util.List;

import it.polimi.ingsw.model.components.iBaseComponent;

public interface iCommonBoard {
	public int getCoveredSize();
	public iBaseComponent pullComponent();
	public void discardComponent(iBaseComponent c);
	public iBaseComponent pullDiscarded(int id);
	public List<Integer> getDiscarded();
}
