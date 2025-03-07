package it.polimi.ingsw.board;

public interface iCommonBoard {
	public StarshipComponent pullComponent();

	public void discardComponent(Starship Component a);

	public StarshipComponent pullDiscarded();

}
