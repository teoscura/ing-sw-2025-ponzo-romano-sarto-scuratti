package it.polimi.ingsw.model.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDimension;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDirection;
import it.polimi.ingsw.model.adventure_cards.utils.Projectile;
import it.polimi.ingsw.model.adventure_cards.visitors.LargeMeteorVisitor;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.model.player.exceptions.NegativeCreditsException;
import it.polimi.ingsw.model.player.exceptions.NegativeCrewException;


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
	private ShipCoords center;
	private int[] containers;
	private boolean[] shielded_directions;
	private int cannon_power = 0;
	private int engine_power = 0;
	private int battery_power = 0;

	//TODO: add setNewCenter method, handle fragments (ask the cugola before)

	public SpaceShip(GameModeType type, 
					 PlayerColor color){
		this.type = type;
		this.color = color;
		this.components = new iBaseComponent[type.getHeight()][type.getWidth()];
		this.shielded_directions = new boolean[4];
		this.containers = new int[4];
		this.crew = new int[3];
		this.empty = new EmptyComponent();
		this.storage_coords = new ArrayList<ShipCoords>();
		this.cabin_coords = new ArrayList<ShipCoords>();
		this.battery_coords = new ArrayList<ShipCoords>();
		this.powerable_coords = new ArrayList<ShipCoords>();
		this.center = type.getCenterCabin();
		for(iBaseComponent[] t : this.components){
			Arrays.fill(t, this.empty); // E' la stessa reference;
		}
		this.addComponent(new StartingCabinComponent(1, new ConnectorType[]{
														 ConnectorType.UNIVERSAL,
														 ConnectorType.UNIVERSAL,
														 ConnectorType.UNIVERSAL,
														 ConnectorType.UNIVERSAL}, 
											ComponentRotation.U000,
											color,
											center),
											center);
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
		VerifyResult[][] res = new VerifyResult[this.type.getHeight()][];
		Queue<iBaseComponent> queue = new ArrayDeque<iBaseComponent>();
		for (int i = 0; i < res.length; i++) {
			res[i] = new VerifyResult[this.type.getWidth()];
			Arrays.fill(res[i], VerifyResult.UNCHECKED);
		}
		queue.add(this.getComponent(this.type.getCenterCabin()));
		iBaseComponent tmp = null;
		while(!queue.isEmpty()){
			tmp = queue.poll();
			if(!tmp.verify(this)) res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.BROKEN;
			else res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.GOOD;
			for(iBaseComponent c : tmp.getConnectedComponents(this)){
				if(c==this.empty) continue;
				ShipCoords xy = c.getCoords();
				if(res[xy.y][xy.x]==VerifyResult.UNCHECKED) queue.add(c);
			}
		}
		for(int t = 0; t < this.getHeight(); t++){
			for(int r = 0; r<this.getWidth(); r++){
				if(res[t][r]==VerifyResult.UNCHECKED) res[t][r]=VerifyResult.NOT_LINKED;
			}
		}
		return res;
	}

	@Override
	public void verifyAndClean() {
		VerifyResult[][] ver = this.verify();
		for(int i=0;i<this.type.getHeight();i++){
			for(int j=0;j<this.type.getWidth();j++){
				if(ver[i][j]!=VerifyResult.NOT_LINKED) continue;
				this.removeComponent(new ShipCoords(this.type,j,i));
			}
		}
	}

	@Override
	public void addComponent(iBaseComponent component, ShipCoords coords) {
		if(coords==null) throw new NullPointerException();
		if(coords.x<0 || coords.x >= this.type.getWidth()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(coords.y<0 || coords.y >= this.type.getHeight()) throw new OutOfBoundsException("Illegal getComponent access.");
		if(component==null) throw new NullPointerException();
		if(this.getComponent(coords) != this.getEmpty()) throw new IllegalComponentAdd("Component is already present at these coords.");
		if(this.type.isForbidden(coords)) throw new IllegalComponentAdd();
		//if(this.getComponent(coords.up())==this.getEmpty() && this.getComponent(coords.down())==this.getEmpty() && this.getComponent(coords.left())==this.getEmpty() && this.getComponent(coords.right())==this.getEmpty())
			//throw new IllegalTargetException("Component is not adjacent to others."); temporary, dirty implemetation
		component.onCreation(this);
		this.components[coords.y][coords.x] = component;
	}

	@Override
	public void removeComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		iBaseComponent tmp = this.getComponent(coords);
		if (this.components[coords.y][coords.x] == this.empty) return;
		this.components[coords.y][coords.x] = this.empty;
		tmp.onDelete(this);
		//verify and clean needs to be called outside when component is removed
	}

	@Override
	public void updateShip(){
		SpaceShipUpdateVisitor v = new SpaceShipUpdateVisitor();
		for(iBaseComponent[] col : this.components){
			for(iBaseComponent cell : col){
				cell.check(v);
			}
		}
		this.crew = v.getCrewMembers();
		this.battery_power = v.getBatteryPower();
		this.shielded_directions = v.getDirections();
		this.containers = v.getStorageContainers();
		this.cannon_power = v.getCannonPower();
		this.engine_power = v.getEnginePower();
		if(v.getCannonPower()>0){
			this.cannon_power += 2*this.crew[2];
		}
		if(v.getEnginePower()>0){
			this.engine_power += 2*this.crew[1];
		}
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

	@Override
	public void addStorageCoords(ShipCoords coords){
		if(this.storage_coords.contains(coords)) throw new NotUniqueException("Coords are already present in storage coords");
		this.storage_coords.add(coords);
	}

	@Override
	public void delStorageCoords(ShipCoords coords){
		if(!this.storage_coords.contains(coords)) throw new NotPresentException("Coords arent present in storage coords");
		this.storage_coords.remove(coords);
	}

	@Override
	public void addCabinCoords(ShipCoords coords){
		if(this.cabin_coords.contains(coords)) throw new NotUniqueException("Coords are already present in cabin coords");
		this.cabin_coords.add(coords);
	}

	@Override
	public void delCabinCoords(ShipCoords coords){
		if(!this.cabin_coords.contains(coords)) throw new NotPresentException("Coords arent present in cabin coords");
		this.cabin_coords.remove(coords);
	}

	@Override
	public void addBatteryCoords(ShipCoords coords){
		if(this.battery_coords.contains(coords)) throw new NotUniqueException("Coords are already present in battery coords");
		this.battery_coords.add(coords);
	}	

	@Override
	public void delBatteryCoords(ShipCoords coords){
		if(!this.battery_coords.contains(coords)) throw new NotPresentException("Coords arent present in battery coords");
		this.battery_coords.remove(coords);
	}

	@Override
	public void addPowerableCoords(ShipCoords coords){
		if(this.powerable_coords.contains(coords)) throw new NotUniqueException("Coords are already present in powerable coords");
		this.powerable_coords.add(coords);
	}

	@Override
	public void delPowerableCoords(ShipCoords coords){
		if(!this.powerable_coords.contains(coords)) throw new NotPresentException("Coords arent present in powerable coords");
		this.powerable_coords.remove(coords);
	}

	@Override
	public int getTotalCrew() {
		int sum = 0;
		for(int i : this.getCrew()){
			sum+=i;
		}
		return sum;
	}

	@Override
	public void setCenterCabin(ShipCoords new_center) {
		if(this.type.isForbidden(new_center) || this.getComponent(new_center)==this.empty) throw new IllegalTargetException("New center is either forbidden or illegal.");
		this.center = new_center;
	}

	@Override
	public ShipCoords getCenterCabin() {
		return this.center;
	}

	@Override
	public ArrayList<ShipCoords> findConnectedCabins() {
		ArrayList<ShipCoords> res = new ArrayList<>();
		for(ShipCoords coords : this.cabin_coords){
			for(iBaseComponent c : this.getComponent(coords).getConnectedComponents(this)){
				if(c.getCoords()==coords || !this.cabin_coords.contains(c.getCoords())) continue;
				res.add(coords);
				break;
			}	
		}
		return res;
	}

	@Override
	public int countExposedConnectors() {
		int sum = 0;
		for(iBaseComponent[] col : this.components){
			for(iBaseComponent c : col){
				if(c != this.empty){
					if(this.getComponent(c.getCoords().up())==this.empty){
						if(c.getConnector(ComponentRotation.U000).getValue()!=0) sum++;
					}
					if(this.getComponent(c.getCoords().right())==this.empty){
						if(c.getConnector(ComponentRotation.U090).getValue()!=0) sum++;
					}
					if(this.getComponent(c.getCoords().down())==this.empty){
						if(c.getConnector(ComponentRotation.U180).getValue()!=0) sum++;
					}
					if(this.getComponent(c.getCoords().left())==this.empty){
						if(c.getConnector(ComponentRotation.U270).getValue()!=0) sum++;
					}
				}
			}
		}
		return sum;
	}	

	@Override
	public boolean handleMeteorite(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if(index<0) return false;
		//If it is in a possible row, handle the logic.
		if(p.getDimension()==ProjectileDimension.BIG){
			//Handle adjacent rows.
			boolean found_cannon = findCannon(p.getDirection(), index);
			if(found_cannon) return false;
			ShipCoords tmp = this.getFirst(p.getDirection(), index);
			if(tmp.equals(this.empty.getCoords())) return false;
			//if (tmp==this.getCenterCabin()) return true -> gestita response nuova nave nuovo centro
			this.removeComponent(tmp);
			return tmp.equals(this.getCenterCabin()) ? true : false;
		}
		boolean shielded = p.getDimension()!=ProjectileDimension.BIG && this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if(shielded) return false;
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if(tmp.equals(this.empty.getCoords())) return false;
		if(this.getComponent(tmp).getConnectors()[p.getDirection().getOpposite().getShift()]==ConnectorType.EMPTY) return false;
		this.removeComponent(tmp);
		return tmp.equals(this.getCenterCabin()) ? true : false;
	}

	@Override
	public boolean handleShot(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if(index<0) return false;
		//If it is in a possible row, handle the logic.
		boolean shielded = p.getDimension()!=ProjectileDimension.BIG && this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if(shielded) return false;
		//Traverse row/column and destroy first block found.
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if(tmp.equals(this.empty.getCoords())) return false;
		this.removeComponent(tmp);
		return tmp.equals(this.getCenterCabin()) ? true : false;	
	}	

	private int normalizeRoll(ProjectileDirection direction, int roll){
		if(direction.getShift()%2==0){
			if(roll<this.type.getMinX() || roll>this.type.getMaxX()) return -1;
			return roll-(this.type.getMinX());
		}
		if(roll<this.type.getMinY() || roll>this.type.getMaxY()) return -1;
		return roll-(this.type.getMinY());
	}
		
	private ShipCoords getFirst(ProjectileDirection d, int index){
		if(index<0||index>=(d.getShift()%2==0?this.getWidth():this.getHeight())) throw new OutOfBoundsException("Offset goes out of bounds");
		iBaseComponent[] line = d.getShift()%2==0 ? this.constructCol(index) : this.components[index];
		if(d.getShift() == 0 || d.getShift() == 3) Collections.reverse(Arrays.asList(line));
		for(iBaseComponent c : line){
			if(c==this.empty || this.type.isForbidden(c.getCoords())) continue;
			return c.getCoords();
		}
		return this.empty.getCoords();
	}

	private iBaseComponent[] constructCol(int index){
		//No validation needed, it's only used in getFirst.
		iBaseComponent[] res = new iBaseComponent[this.type.getHeight()];
		for(int i = 0; i<this.type.getHeight(); i++){
			res[i] = this.components[i][index];
		}
		return res;
	}

	private boolean findCannon(ProjectileDirection d, int index){
		LargeMeteorVisitor v = new LargeMeteorVisitor(d);
		if(d==ProjectileDirection.U180){
			for(iBaseComponent t : constructCol(index)){
				t.check(v);
			}
			return v.getFoundCannon();
		}
		int[] indexes = new int[]{0,0,0};
		for(int i = -1; i<=1; i++){
			indexes[i+1]=index+i;
		}
		for(int i : indexes){
			if(i<0) continue;
			iBaseComponent[] line = d.getShift()%2==0? constructCol(i) : this.components[i];
			for(iBaseComponent t : line){
				t.check(v);
			}
		}
		return v.getFoundCannon();
	}

	@Override
	public void retire() {
		if(retired) throw new AlreadyPoweredException("Ship is alredy retired.");
		this.retired = true;
	}

	@Override
	public boolean getRetired() {
		return this.retired;
	}

}

