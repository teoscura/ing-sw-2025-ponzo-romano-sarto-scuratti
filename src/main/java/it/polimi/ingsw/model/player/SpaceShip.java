package it.polimi.ingsw.model.player;

import java.util.Arrays;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;



public class SpaceShip implements iSpaceShip{
	static int[] board_shape = {0, 1, 3, 5, 6, 7, 13, 31};
	private iBaseComponent[] components;
	private int cannon_power = 0;
	private int engine_power = 0;
	private int batteries = 0;
	private boolean[] shielded_directions;
	
	public SpaceShip(){
		this.components = new iBaseComponent[35];
		this.shielded_directions = new boolean[4];
		EmptyComponent tmp = new EmptyComponent();
		Arrays.fill(components, tmp); // E' la stessa reference, ma poi sara' intoccabile.
		Arrays.fill(shielded_directions, false);
	}

	//FIXME forse ritornare array di posizioni che non van bene?
	@Override
	public void verify() {
		//TODO BFS on components.
	}

	@Override
	public void verifyAndClean() {
		//TODO BFS on components, unreached are removed.
	}

	@Override
	public void addComponent(iBaseComponent component, int position) {
		if(position<0 || position>=35) throw new OutOfBoundsException();
		if(component==null) throw new NullPointerException();
		for(int i : board_shape){
			if(position==i) throw new IllegalComponentAdd();
		}
		this.components[position] = component;
	}

	@Override
	public void removeComponent(int position) {
		//TODO
	}

	@Override
	public void updateShip(){
		// TODO
	}

	@Override
	public void resetPower(){
		//TODO
	}

	@Override
	public void turnOn(int position) {
		//TODO
	}

	@Override
	public iBaseComponent getComponent(int position) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getComponent'");
	}

	@Override
	public int getCannonPower() { //TODO
		return this.cannon_power;
	}

	@Override
	public int getEnginePower() { //TODO
		return this.engine_power;
	}

	@Override
	public int getEnergyPower() { //TODO
		return this.batteries;
	}

	public boolean[] getShieldedDirections(){
		//TODO
		return null;
	}

	@Override
	public int up(int position){
		//TODO
		return 0;
	}

	@Override
    public int down(int position){
		//TODO
		return 0;
	}

	@Override
    public int left(int position){
		//TODO
		return 0;
	}

	@Override
    public int right(int position){
		//TODO
		return 0;
	}

}
