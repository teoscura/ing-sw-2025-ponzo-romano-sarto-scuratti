package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.components.iBaseComponent;

public interface iSpaceShip {
	
	public void verify();

	public void addComponent(iBaseComponent component, int position);

	public void removeComponent(int position);

	public iBaseComponent getComponent(int position);

	public boolean turnOn(int position);

    public void resetPower();

	public int getPower();

	public int getEnginePower();

	public int getSpeed();

	public void updateShip();

    public int up(int position);

    public int down(int position);

    public int left(int position);

    public int right(int position);

}