package it.polimi.ingsw.board;

public class CommonBoard implements iCommonBoard {

	private Queue<StarshipComponent> covered_components;
	private ArrayList<StarshipComponent> uncovered_components;


	// methods
	@Override
	public StarshipComponent pullComponent() {

	}

	@Override
	public void discardComponent(StarshipComponent a) {
	}

	@Override
	public StarshipComponent pullDiscarded() {
		return null; //TODO
	}

}
