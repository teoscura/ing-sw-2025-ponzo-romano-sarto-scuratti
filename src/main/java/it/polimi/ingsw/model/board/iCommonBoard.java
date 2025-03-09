package it.polimi.ingsw.model.board;

public interface iCommonBoard {
	public StarshipComponent pullComponent();

	public void discardComponent(StarshipComponent a);

	public StarshipComponent pullDiscarded();

}
