//Done.
package it.polimi.ingsw.model.board;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;

public class CommonBoard implements iCommonBoard {

	private Queue<iBaseComponent> covered_components;
	private HashMap<Integer, iBaseComponent> uncovered_components;

	public CommonBoard(){
		ComponentFactory factory = new ComponentFactory();
		ArrayList<iBaseComponent> tmp = new ArrayList<>();
		main: for(int i = 1;i<158;i++){
			for(int t : factory.getForbiddenID()){
				if(t==i) continue main;
			}
			tmp.add(factory.getComponent(i));
		}
		Collections.shuffle(tmp);
		this.covered_components = new ArrayDeque<iBaseComponent>(tmp);
		this.uncovered_components = new HashMap<>();
	}

	@Override
	public iBaseComponent pullComponent() {
		if(this.covered_components.isEmpty()) return null;
		return this.covered_components.poll();
	}

	@Override
	public void discardComponent(iBaseComponent c) {
		if(this.uncovered_components.containsKey(c.getID())) throw new IllegalArgumentException("Tried to insert a duplicate component.");
		this.uncovered_components.put(c.getID(),c);
	}

	@Override
	public iBaseComponent pullDiscarded(int id) {
		if(!this.uncovered_components.containsKey(id)) throw new ContainerEmptyException();
		iBaseComponent tmp = this.uncovered_components.get(id);
		this.uncovered_components.remove(id);
		return tmp;
	}

	@Override
	public int getCoveredSize() {
		return this.covered_components.size();
	}

	@Override
	public List<Integer> getDiscarded() {
		return new ArrayList<Integer>(this.uncovered_components.keySet());
	}

}
