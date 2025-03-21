package it.polimi.ingsw.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.rulesets.GameModeType;

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
	public GameModeType getType();
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
    public iBaseComponent getEmpty();
	public int getTotalCrew();
	public void addStorageCoords(ShipCoords coords);
	public void delStorageCoords(ShipCoords coords);
	public void addCabinCoords(ShipCoords coords);
	public void delCabinCoords(ShipCoords coords);
	public void addBatteryCoords(ShipCoords coords);
	public void delBatteryCoords(ShipCoords coords);
	public void addPowerableCoords(ShipCoords coords);
	public void delPowerableCoords(ShipCoords coords);
    public ArrayList<ShipCoords> findConnectedCabins();
    public int countExposedConnectors();
    public boolean handleMeteorite(Projectile p);
    public boolean handleShot(Projectile p);
}