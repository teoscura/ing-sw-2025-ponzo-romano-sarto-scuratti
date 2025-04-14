package it.polimi.ingsw.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;

public interface iSpaceShip {

	public void updateCrew(int new_num, AlienType type);
	public VerifyResult[][] verify();
	public boolean verifyAndClean();
	public void addComponent(iBaseComponent component, ShipCoords coords);
	public void removeComponent(ShipCoords coords);
	public void updateShip();
	public void resetPower();
	public void turnOn(ShipCoords coords_target, ShipCoords battery_location);
	public GameModeType getType();
	public int getCannonPower();
	public int[] getCrew();
	public int[] getContains();
	public iBaseComponent getComponent(ShipCoords coords);
	public int getEnginePower();
	public int getEnergyPower();
	public boolean[] getShieldedDirections();
	public int getHeight();
	public int getWidth();
    public iBaseComponent getEmpty();
	public int getTotalCrew();
	public boolean getBrokeCenter();
	public void setBrokeCenter();
	public void addStorageCoords(ShipCoords coords);
	public void delStorageCoords(ShipCoords coords);
	public void addCabinCoords(ShipCoords coords);
	public void delCabinCoords(ShipCoords coords);
	public void addBatteryCoords(ShipCoords coords);
	public void delBatteryCoords(ShipCoords coords);
	public void addPowerableCoords(ShipCoords coords);
	public void delPowerableCoords(ShipCoords coords);
	public void setCenter(ShipCoords new_center) throws ForbiddenCallException;
	public ShipCoords getCenter();
    public ArrayList<ShipCoords> findConnectedCabins();
    public int countExposedConnectors();
    public boolean handleMeteorite(Projectile p);
    public boolean handleShot(Projectile p);
	public boolean isCabin(ShipCoords coords);
    public ClientSpaceShip getClientSpaceShip();
}