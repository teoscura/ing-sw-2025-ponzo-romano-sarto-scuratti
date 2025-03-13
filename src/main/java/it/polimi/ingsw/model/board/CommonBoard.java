package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import it.polimi.ingsw.model.components.iBaseComponent;

public class CommonBoard implements iCommonBoard {

	private Queue<iBaseComponent> covered_components;
	private ArrayList<iBaseComponent> uncovered_components;

	public CommonBoard(){
		//TODO: factory load from JSON.
		this.covered_components = new ArrayDeque<iBaseComponent>();
		this.uncovered_components = new ArrayList<iBaseComponent>();
	}

	// methods
	@Override
	public iBaseComponent pullComponent() {
			return null; //TODO

	}

	@Override
	public void discardComponent(iBaseComponent a) {
		return; //TODO
	}

	@Override
	public iBaseComponent pullDiscarded() {
		return null; //TODO
	}

}
