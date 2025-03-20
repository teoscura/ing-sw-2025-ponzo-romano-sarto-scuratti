package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
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
		if(this.covered_components.isEmpty()) return null;
		return this.covered_components.poll();
	}

	@Override
	public void discardComponent(iBaseComponent a) {
		//TODO check duplicates
		this.uncovered_components.add(a);
	}

	@Override
	public iBaseComponent pullDiscarded(int i) {
		if(this.uncovered_components.isEmpty()) return null;
		if(i<0) throw new OutOfBoundsException();
		if(i>=this.uncovered_components.size()) throw new OutOfBoundsException();
		iBaseComponent tmp = this.uncovered_components.get(i);
		this.uncovered_components.remove(i);
		return tmp;
	}

}
