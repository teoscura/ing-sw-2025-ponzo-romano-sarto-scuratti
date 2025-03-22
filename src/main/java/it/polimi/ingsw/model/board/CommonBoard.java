package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.components.iBaseComponent;

public class CommonBoard implements iCommonBoard {

	private Queue<iBaseComponent> covered_components;
	private ArrayList<iBaseComponent> uncovered_components;

	public CommonBoard(Set<iBaseComponent> starting_pile){
		this.covered_components = new ArrayDeque<iBaseComponent>(starting_pile);
		this.uncovered_components = new ArrayList<iBaseComponent>(0);
	}

	@Override
	public iBaseComponent pullComponent() {
		if(this.covered_components.isEmpty()) return null;
		return this.covered_components.poll();
	}

	@Override
	public void discardComponent(iBaseComponent c) {
		if(this.uncovered_components.contains(c)) throw new IllegalArgumentException("Tried to insert a duplicate component.");
		this.uncovered_components.add(c);
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
