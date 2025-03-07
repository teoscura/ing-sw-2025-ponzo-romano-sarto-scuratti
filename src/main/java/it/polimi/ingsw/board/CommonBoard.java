package it.polimi.ingsw.board;

public class CommonBoard {

	private Queue<StarshipComponent> covered_components;
	private ArrayList<StarshipComponent> uncovered_components;


	// methods
	public StarshipComponent pullComponent() {

	}

	public void discardComponent(StarshipComponent a) {
	}

	public StarshipComponent pullDiscarded() {
		return null; //TODO
	}

}
