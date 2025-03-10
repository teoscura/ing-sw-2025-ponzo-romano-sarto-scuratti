package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.components.iBaseComponent;

public class SpaceShip implements iSpaceShip{
	private iBaseComponent[] component = new iBaseComponent[35];
	private int total_power;
	private int total_firing_power;
	private int total_engine_power;
	private int batteries;

	public SpaceShip(){
		//TODO
	}

	@Override
	public void verify() {
		//TODO BFS on components, unreached are removed.
	}

	@Override
	public void addComponent(iBaseComponent component, int position) {
		//TODO
	}

	@Override
	public void removeComponent(int position) {
		//TODO
	}

	@Override
	public iBaseComponent getComponent(int position) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getComponent'");
	}

	@Override
	public boolean turnOn(int position) {
		return false;
	}

	@Override
	public int getPower() { //TODO
		return this.total_firing_power;
	}

	@Override
	public int getEnginePower() { //TODO
		return this.total_engine_power;
	}

	@Override
	public int getSpeed() { //TODO
		return 0;
	}

	@Override
	public void resetPower(){
		//TODO
	}
	
	@Override
	public void updateShip(){
		// TODO
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
