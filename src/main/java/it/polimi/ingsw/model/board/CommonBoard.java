package it.polimi.ingsw.model.board;

import java.util.ArrayList;
import java.util.Queue;

public class CommonBoard implements iCommonBoard {

	private Queue<iBaseComponent> covered_components;
	private ArrayList<iBaseComponent> uncovered_components;


	// methods
	@Override
	public iBaseComponent pullComponent() {

	}

	@Override
	public void discardComponent(iBaseComponent a) {
	}

	@Override
	public iBaseComponent pullDiscarded() {
		return null; //TODO
	}

}
