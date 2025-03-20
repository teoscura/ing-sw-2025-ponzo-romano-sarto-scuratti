package it.polimi.ingsw.model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.model.player.exceptions.NegativeCreditsException;
import it.polimi.ingsw.model.player.exceptions.NegativeCrewException;
import it.polimi.ingsw.model.rulesets.GameModeType;


public class SpaceShip implements iSpaceShip{
	private final ArrayList<ShipCoords> storage_coords;
	private final ArrayList<ShipCoords> cabin_coords;
	private final ArrayList<ShipCoords> battery_coords;
	private final ArrayList<ShipCoords> powerable_coords;
	//Player fields
	private final PlayerColor color;
	private int credits;
	private int[] crew;
	private boolean retired = false;
	//SpaceShip fields
	private final GameModeType type;
	private final iBaseComponent[][] components;
	private final iBaseComponent empty;
	private int[] containers;
	private boolean[] shielded_directions;
	private int cannon_power = 0;
	private int engine_power = 0;
	private int battery_power = 0;


	public SpaceShip(GameModeType type, 
					 PlayerColor color){
		this.type = type;
		this.color = color;
		this.components = new iBaseComponent[type.getHeight()][type.getWidth()];
		this.shielded_directions = new boolean[4];
		this.containers = new int[4];
		this.crew = new int[3];
		this.empty = new EmptyComponent();
		for(iBaseComponent[] t : this.components){
			Arrays.fill(t, this.empty); // E' la stessa reference, ma poi sara' intoccabile.
		}
		this.storage_coords = new ArrayList<ShipCoords>();
		this.cabin_coords = new ArrayList<ShipCoords>();
		this.battery_coords = new ArrayList<ShipCoords>();
		this.powerable_coords = new ArrayList<ShipCoords>();
		Arrays.fill(shielded_directions, false);
		Arrays.fill(containers, 0);
		Arrays.fill(crew, 0);
	}

	@Override 
	public GameModeType getType(){
		return this.type;
	}

	@Override
	public int getCredits(){
		return this.credits;
	}

	@Override
	public int takeCredits(int amount){
		if(amount<=0) throw new IllegalArgumentException("Cannot take negative credits.");
		if((this.credits-amount)<0) throw new NegativeCreditsException("Took more credits than available.");
		this.credits -= amount;
		return this.credits; 
	}

	@Override
	public int giveCredits(int amount){
		if(amount<=0) throw new IllegalArgumentException("Cannot earn negative credits.");
		this.credits+=amount;
		return this.credits;
	}

	@Override
	public void updateCrew(int new_num, AlienType type){
		if(type.getArraypos()==-1) throw new IllegalArgumentException();
		if(new_num<0) throw new NegativeCrewException("Cannot set a negative crew.");
		this.crew[type.getArraypos()] = new_num;
	}

	@Override
	public int[] getCrew(){
		return this.crew;
	}

	@Override
	public PlayerColor getColor(){
		return this.color;
	}

	@Override
	public VerifyResult[][] verify() {
		VerifyResult[][] res = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
		Queue<iBaseComponent> queue = new ArrayDeque<iBaseComponent>();
		Arrays.fill(res, VerifyResult.UNCHECKED);
		queue.add(this.getComponent(this.type.getCenterCabin()));
		iBaseComponent tmp = null;
		while(!queue.isEmpty()){
			tmp = queue.poll();
			if(!tmp.verify(this)) res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.BROKEN;
			else res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.GOOD;
			if(tmp.getConnector(ComponentRotation.U000)
			  .connected(this.getComponent(tmp.getCoords().up())
			    .getConnector(ComponentRotation.U180))){
				ShipCoords up = this.getComponent(tmp.getCoords().up()).getCoords();
				if(res[up.y][up.x]==VerifyResult.UNCHECKED) queue.add(this.getComponent(up));
			}
			if(tmp.getConnector(ComponentRotation.U090)
			  .connected(this.getComponent(tmp.getCoords().right())
			    .getConnector(ComponentRotation.U270))){
				ShipCoords right = this.getComponent(tmp.getCoords().right()).getCoords();
				if(res[right.y][right.x]==VerifyResult.UNCHECKED) queue.add(this.getComponent(right));
			}
			if(tmp.getConnector(ComponentRotation.U180)
			  .connected(this.getComponent(tmp.getCoords().down())
			    .getConnector(ComponentRotation.U000))){
				ShipCoords left = this.getComponent(tmp.getCoords().down()).getCoords();
				if(res[left.y][left.x]==VerifyResult.UNCHECKED) queue.add(this.getComponent(left));
			}
			if(tmp.getConnector(ComponentRotation.U270)
			  .connected(this.getComponent(tmp.getCoords().left())
			    .getConnector(ComponentRotation.U090))){
				ShipCoords down = this.getComponent(tmp.getCoords().left()).getCoords();
				if(res[down.y][down.x]==VerifyResult.UNCHECKED) queue.add(this.getComponent(down));
			}
		}
		for(VerifyResult[] t: res){
			for(VerifyResult r : t){
				if(r!=VerifyResult.UNCHECKED) continue;
				r=VerifyResult.NOT_LINKED; 
			}
		}
		return res;
	}

	@Override
	public void verifyAndClean() {
		VerifyResult[][] ver = this.verify();
		for(int i=0;i<this.type.getHeight();i++){
			for(int j=0;i<this.type.getWidth();j++){
				if(ver[i][j]!=VerifyResult.NOT_LINKED) continue;
				this.removeComponent(new ShipCoords(this.type,i,j));
			}
		}
	}

	@Override
	public void addComponent(iBaseComponent component, ShipCoords coords) {
		if(coords==null) throw new NullPointerException();
		if(coords.x<0 || coords.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(coords.y<0 || coords.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(component==null) throw new NullPointerException();
		for(int i : type.getShape()){
			if(i== (coords.y*type.getWidth()+coords.x)) throw new IllegalComponentAdd();
		}
		this.components[coords.y][coords.x] = component;
	}

	@Override
	public void removeComponent(ShipCoords coords) {
		if(coords==null) throw new NullPointerException();
		if(coords.x<0 || coords.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(coords.y<0 || coords.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(this.components[coords.y][coords.x]==this.empty) return;
		this.components[coords.y][coords.x] = this.empty;
	}

	@Override
	public void updateShip(){
		SpaceShipUpdateVisitor v = new SpaceShipUpdateVisitor(this);
		for(iBaseComponent[] col : this.components){
			for(iBaseComponent cell : col){
				cell.check(v);
			}
		}
		this.cannon_power = v.getCannonPower();
		this.battery_power = v.getBatteryPower();
		this.engine_power = v.getEnginePower();
		this.crew = v.getCrewMembers();
		this.shielded_directions = v.getDirections();
		this.containers = v.getStorageContainers();
	}

	@Override
	public void resetPower(){
		EnergyVisitor v = new EnergyVisitor(false);
		for(iBaseComponent[] col : this.components){
			for(iBaseComponent cell : col){
				cell.check(v);
			}
		}
	}

	@Override
	public void turnOn(ShipCoords coords_target, ShipCoords battery_location) {
		if(coords_target==null) throw new NullPointerException();
		if(battery_location==null) throw new NullPointerException();
		if(!this.powerable_coords.contains(coords_target)) throw new IllegalTargetException("Target is not powerable");
		if(!this.battery_coords.contains(battery_location)) throw new IllegalTargetException("Battery component wasn't present at location");
		BatteryComponent c = (BatteryComponent)getComponent(battery_location);
		EnergyVisitor v = new EnergyVisitor(true);
		if(c.getContains()==0) throw new IllegalTargetException("No batteries found at location");
		c.takeOne();
		c.check(v);
	}

	@Override
	public iBaseComponent getComponent(ShipCoords coords) {
		if(coords==null) throw new NullPointerException();
		if(coords.x<0 || coords.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(coords.y<0 || coords.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access.");
		return this.components[coords.y][coords.x];
	}

	@Override
	public int getCannonPower() {
		this.updateShip();
		return this.cannon_power;
	}

	@Override
	public int getEnginePower() {
		this.updateShip();
		return this.engine_power;
	}

	@Override
	public int getEnergyPower() {
		this.updateShip();
		return this.battery_power;
	}

	@Override
	public boolean[] getShieldedDirections(){
		this.updateShip();
		return this.shielded_directions;
	}

	@Override
	public int getHeight(){
		return this.type.getHeight();
	}

	@Override
	public int getWidth(){
		return this.type.getWidth();
	}

	@Override
	public iBaseComponent getEmpty() {
		return this.empty;
	}

	public void addStorageCoords(ShipCoords coords){
		if(this.storage_coords.contains(coords)) throw new NotUniqueException("Coords are already present in storage coords");
		this.storage_coords.add(coords);
	}

	public void delStorageCoords(ShipCoords coords){
		if(!this.storage_coords.contains(coords)) throw new NotPresentException("Coords arent present in storage coords");
		this.storage_coords.remove(coords);
	}

	public void addCabinCoords(ShipCoords coords){
		if(this.cabin_coords.contains(coords)) throw new NotUniqueException("Coords are already present in cabin coords");
		this.cabin_coords.add(coords);
	}

	public void delCabinCoords(ShipCoords coords){
		if(!this.cabin_coords.contains(coords)) throw new NotPresentException("Coords arent present in cabin coords");
		this.cabin_coords.remove(coords);
	}

	public void addBatteryCoords(ShipCoords coords){
		if(this.battery_coords.contains(coords)) throw new NotUniqueException("Coords are already present in battery coords");
		this.battery_coords.add(coords);
	}	

	public void delBatteryCoords(ShipCoords coords){
		if(!this.battery_coords.contains(coords)) throw new NotPresentException("Coords arent present in battery coords");
		this.battery_coords.remove(coords);
	}

	public void addPowerableCoords(ShipCoords coords){
		if(this.powerable_coords.contains(coords)) throw new NotUniqueException("Coords are already present in powerable coords");
		this.powerable_coords.add(coords);
	}

	public void delPowerableCoords(ShipCoords coords){
		if(!this.powerable_coords.contains(coords)) throw new NotPresentException("Coords arent present in powerable coords");
		this.powerable_coords.remove(coords);
	}

	

}

