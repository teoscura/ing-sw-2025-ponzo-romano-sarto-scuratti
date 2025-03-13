package it.polimi.ingsw.model.player;

import java.util.ArrayDeque;
import java.util.Arrays;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.model.player.exceptions.NegativeCreditsException;
import it.polimi.ingsw.model.player.exceptions.NegativeCrewException;


public class SpaceShip implements iSpaceShip{

	//Player fields
	PlayerColor color;
	private int credits;
	private int[] crew;
	//SpaceShip fields
	private ShipType type;
	private iBaseComponent[][] components;
	private iBaseComponent empty;
	private int[] containers;
	private boolean[] shielded_directions;
	private int cannon_power = 0;
	private int engine_power = 0;
	private int battery_power = 0;


	public SpaceShip(ShipType type, 
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
		Arrays.fill(shielded_directions, false);
		Arrays.fill(containers, 0);
		Arrays.fill(crew, 0);
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

	//FIXME forse ritornare array di posizioni che non van bene?
	@Override
	public VerifyResult[] verify() {
		//TODO BFS on components.
		VerifyResult[] res = new VerifyResult[this.type.getHeight()*this.type.getWidth()];
		ArrayDeque<iBaseComponent> queue = new ArrayDeque<iBaseComponent>();
		
		return res;
	}

	private void DFSRecursive(VerifyResult[] a, ArrayDeque<iBaseComponent> q, iBaseComponent c){
		//TODO
	}

	@Override
	public void verifyAndClean() {
		VerifyResult[] ver = this.verify();
		for(VerifyResult v : ver){

		}
		//TODO BFS on components, unreached are removed.
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

	//HACK this is terrible, ask if instanceof is allowed to check for singular classes.
	@Override
	public void turnOn(ShipCoords coords_target, ShipCoords battery_location) {
		if(coords_target==null) throw new NullPointerException();
		if(battery_location==null) throw new NullPointerException();
		if(coords_target.x<0 || coords_target.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access: Target");
		if(coords_target.y<0 || coords_target.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access: Target");
		if(battery_location.x<0 || battery_location.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access: Battery Provider");
		if(battery_location.y<0 || battery_location.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access: Battery Provider");
		//FIXME
		EnergyVisitor v = new EnergyVisitor(false);
		iBaseComponent target = getComponent(coords_target);
		iBaseComponent battery = getComponent(battery_location);  
		target.check(v);
		if(!v.getPowerable()) throw new IllegalTargetException("Target is not powerable");
		battery.check(v);
		if(!v.getFoundBatteryComponent()) throw new IllegalTargetException("Battery component wasn't present at location");
		if(!v.hasBattery()) throw new IllegalTargetException("No batteries found at location");
		v.toggle();
		battery.check(v);
		target.check(v);
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

	public ShipCoords up(ShipCoords coords){
		if(coords.x<0 || coords.y<0) throw new OutOfBoundsException("Illegal coords: negative");
		if(coords.x>=this.getWidth() || coords.y>=this.getHeight()) throw new OutOfBoundsException("Illegal coords: too big");
		if(coords.y==0) return new ShipCoords(0,0);
		return new ShipCoords(coords.x,coords.y+1);
	}

	public ShipCoords right(ShipCoords coords){
		if(coords.x<0 || coords.y<0) throw new OutOfBoundsException("Illegal coords: negative");
		if(coords.x>=this.getWidth() || coords.y>=this.getHeight()) throw new OutOfBoundsException("Illegal coords: too big");
		if(coords.x==this.getWidth()-1) return new ShipCoords(0,0);
		return new ShipCoords(coords.x+1,coords.y);
	}

	public ShipCoords down(ShipCoords coords){
		if(coords.x<0 || coords.y<0) throw new OutOfBoundsException("Illegal coords: negative");
		if(coords.x>=this.getWidth() || coords.y>=this.getHeight()) throw new OutOfBoundsException("Illegal coords: too big");
		if(coords.y==this.getHeight()-1) return new ShipCoords(0,0);
		return new ShipCoords(coords.x,coords.y-1);
	}

	public ShipCoords left(ShipCoords coords){
		if(coords.x<0 || coords.y<0) throw new OutOfBoundsException("Illegal coords: negative");
		if(coords.x>=this.getWidth() || coords.y>=this.getHeight()) throw new OutOfBoundsException("Illegal coords: too big");
		if(coords.x==0) return new ShipCoords(0,0);
		return new ShipCoords(coords.x-1,coords.y);
	}

}

