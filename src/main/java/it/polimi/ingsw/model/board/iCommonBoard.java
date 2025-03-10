package it.polimi.ingsw.model.board;

public interface iCommonBoard {
	public iBaseComponent pullComponent();

	public void discardComponent(iBaseComponent a);

	public iBaseComponent pullDiscarded();

}
