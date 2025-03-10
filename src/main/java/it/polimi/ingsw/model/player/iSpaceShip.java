package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.components.iBaseComponent;

public interface iSpaceShip {
	
	public void verify();
	public void verifyAndClean();
	public void addComponent(iBaseComponent component, int position);
	public void removeComponent(int position);
	public void updateShip();
	public void resetPower();
	public void turnOn(int position);

	public iBaseComponent getComponent(int position);
	public int getCannonPower();
	public int getEnginePower();
	public int getEnergyPower();
	public boolean[] getShieldedDirections();

    public int up(int position);
    public int down(int position);
    public int left(int position);
    public int right(int position);

}