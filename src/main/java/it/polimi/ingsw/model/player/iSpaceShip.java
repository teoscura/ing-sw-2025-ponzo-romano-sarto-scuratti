package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;

public interface iSpaceShip {
	
	public int takeCredits(int amount);
	public int giveCredits(int amount);
	public void updateCrew(int new_num, AlienType type);

	public VerifyResult[][] verify();
	public void verifyAndClean();
	public void addComponent(iBaseComponent component, ShipCoords coords);
	public void removeComponent(ShipCoords coords);
	public void updateShip();
	public void resetPower();
	public void turnOn(ShipCoords coords_target, ShipCoords battery_location);
	
	public int getCannonPower();
	public int getCredits();
	public int[] getCrew();
	public PlayerColor getColor();
	public iBaseComponent getComponent(ShipCoords coords);
	public int getEnginePower();
	public int getEnergyPower();
	public boolean[] getShieldedDirections();
	public int getHeight();
	public int getWidth();

	public ShipCoords up(ShipCoords coords);
	public ShipCoords right(ShipCoords coords);
	public ShipCoords down(ShipCoords coords);
	public ShipCoords left(ShipCoords coords);
}