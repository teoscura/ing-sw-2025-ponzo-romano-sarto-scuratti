package it.polimi.ingsw.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;

public interface iSpaceShip {

	void updateCrew(int new_num, AlienType type);

	VerifyResult[][] verify();

	boolean verifyAndClean();

	void addComponent(iBaseComponent component, ShipCoords coords);

	void removeComponent(ShipCoords coords);

	void updateShip();

	void resetPower();

	void turnOn(ShipCoords coords_target, ShipCoords battery_location);

	GameModeType getType();

	int getCannonPower();

	int[] getCrew();

	int[] getContains();

	iBaseComponent getComponent(ShipCoords coords);

	int getEnginePower();

	int getEnergyPower();

	boolean[] getShieldedDirections();

	int getHeight();

	int getWidth();

	iBaseComponent getEmpty();

	int getTotalCrew();

	boolean getBrokeCenter();

	void setBrokeCenter();

	void addStorageCoords(ShipCoords coords);

	void delStorageCoords(ShipCoords coords);

	void addCabinCoords(ShipCoords coords);

	void delCabinCoords(ShipCoords coords);

	void addBatteryCoords(ShipCoords coords);

	void delBatteryCoords(ShipCoords coords);

	void addPowerableCoords(ShipCoords coords);

	void delPowerableCoords(ShipCoords coords);

	void setCenter(ShipCoords new_center) throws ForbiddenCallException;

	ShipCoords getCenter();

	ArrayList<ShipCoords> findConnectedCabins();

	int countExposedConnectors();

	boolean handleMeteorite(Projectile p);

	boolean handleShot(Projectile p);

	boolean isCabin(ShipCoords coords);

	ClientSpaceShip getClientSpaceShip();
}