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
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.cards.visitors.LargeMeteorVisitor;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;


public class SpaceShip{

	private final Player player;

	private ArrayList<ArrayList<ShipCoords>> blobs;
	private final ArrayList<ShipCoords> storage_coords;
	private final ArrayList<ShipCoords> cabin_coords;
	private final ArrayList<ShipCoords> battery_coords;
	private final ArrayList<ShipCoords> powerable_coords;
	private final GameModeType type;
	private final BaseComponent[][] components;
	private final BaseComponent empty;
	private int[] crew;
	private ShipCoords center;
	private int[] containers;
	private boolean[] shielded_directions;
	private double cannon_power = 0;
	private int engine_power = 0;

	public SpaceShip(GameModeType type,
					 Player player) {
		this.player = player;
		this.type = type;
		this.components = new BaseComponent[type.getHeight()][type.getWidth()];
		this.shielded_directions = new boolean[4];
		this.containers = new int[5];
		this.crew = new int[3];
		this.empty = new EmptyComponent();
		this.empty.onCreation(this, new ShipCoords(type, 0,0));
		this.storage_coords = new ArrayList<ShipCoords>();
		this.cabin_coords = new ArrayList<ShipCoords>();
		this.battery_coords = new ArrayList<ShipCoords>();
		this.powerable_coords = new ArrayList<ShipCoords>();
		this.center = type.getCenterCabin();
		for (BaseComponent[] t : this.components) {
			Arrays.fill(t, this.empty); // E' la stessa reference;
		}
		BaseComponent scabin = new StartingCabinComponent(1, new ConnectorType[]{
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL},
				ComponentRotation.U000,
				player.getColor(),
				center);
		scabin.onCreation(this, center);
		this.components[center.y][center.x] = scabin;
		this.updateShip();
		Arrays.fill(shielded_directions, false);
		Arrays.fill(containers, 0);
		Arrays.fill(crew, 0);
		this.updateShip();
	}

	public GameModeType getType() {
		return this.type;
	}

	public int[] getCrew() {
		return this.crew;
	}

	public VerifyResult[][] bulkVerify() {
		VerifyResult[][] res = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
		for(int y = 0; y < this.type.getHeight(); y++){
			for(int x = 0; x < this.type.getWidth(); x++){
				res[y][x] = this.components[y][x] == this.empty ? VerifyResult.UNCHECKED : VerifyResult.NOT_LNKED;
			}
		}
		for(int y = 0; y < this.type.getHeight(); y++){
			for(int x = 0; x < this.type.getWidth(); x++){
				if(res[y][x]==VerifyResult.UNCHECKED) continue;
				BaseComponent tmp = this.components[y][x];
				if (!tmp.verify(this)) res[y][x] = VerifyResult.BRKN_COMP;
				else res[y][x] = VerifyResult.GOOD_COMP;
			}
		}
		return res;
	}

	public boolean bulkVerifyResult(){
		var t = bulkVerify();
		for(var row : t){
			for (var r : row){
				if(r==VerifyResult.BRKN_COMP) return false;
			}
		}
		return true;
	}

    public int getBlobsSize(){
        this.updateShipBlobs();
        return this.blobs.size();
    }
	
	public void printBlobs(){
		this.updateShipBlobs();
		for(var blob : this.blobs){
			for(var c : blob){
				System.out.print(c+" ");
			}
			System.out.println("");
		}
	}

    public void updateShipBlobs(){
        ArrayList<ArrayList<ShipCoords>> res = new ArrayList<>();
        VerifyResult[][] map = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
        for(int y = 0; y<type.getHeight(); y++){
            for(int x = 0; x<type.getWidth(); x++){
                map[y][x] = this.components[y][x] == this.empty ? VerifyResult.UNCHECKED : VerifyResult.NOT_LNKED;
            }
        }
        for(int y = 0; y<type.getHeight(); y++){
            for(int x = 0; x<type.getWidth(); x++){
                if(map[y][x]!=VerifyResult.NOT_LNKED) continue;
                res.add(this.verifyBlob(map, new ShipCoords(this.type, x, y)));
            }
        }
        this.blobs = res;
    }

	private ArrayList<ShipCoords> verifyBlob(VerifyResult[][] map, ShipCoords starting_point) {
        ArrayList<ShipCoords> res = new ArrayList<>();
		Queue<ShipCoords> queue = new ArrayDeque<>();
		queue.add(starting_point);
		ShipCoords tmp = null;
		while (!queue.isEmpty()) {
			tmp = queue.poll();
            res.add(tmp);
			if (!this.getComponent(tmp).verify(this)) map[tmp.y][tmp.x] = VerifyResult.BRKN_COMP;
			else map[tmp.y][tmp.x] = VerifyResult.GOOD_COMP;
			for (BaseComponent c : this.getComponent(tmp).getConnectedComponents(this)) {
				if (c == this.empty) continue;
				ShipCoords xy = c.getCoords();
				if (map[xy.y][xy.x] == VerifyResult.NOT_LNKED && !queue.contains(xy)) queue.add(c.getCoords());
			}
		}
        return res;
	}

    public void selectShipBlob(ShipCoords blob_coord) throws ForbiddenCallException{
		if(this.blobs.size()<=1) throw new ForbiddenCallException();
        for(ArrayList<ShipCoords> blob : this.blobs){
            if(!blob.contains(blob_coord)) continue;
			ArrayList<ArrayList<ShipCoords>> previous = this.blobs;
            previous.remove(blob);
            previous.stream().forEach(b->b.stream().forEach(c->this.removeComponent(c)));
            this.blobs = new ArrayList<>(){{add(blob);}};
			this.center = blob_coord;
			updateShip();
            return;
        }
        throw new IllegalTargetException("Blob coordinate was invalid!");
    }


	public void addComponent(BaseComponent component, ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		if (coords.x < 0 || coords.x >= this.type.getWidth())
			throw new OutOfBoundsException("Illegal getComponent access.");
		if (coords.y < 0 || coords.y >= this.type.getHeight())
			throw new OutOfBoundsException("Illegal getComponent access.");
		if (component == null) throw new NullPointerException();
		if (this.getComponent(coords) != this.getEmpty())
			throw new IllegalComponentAdd("Component is already present at these coords.");
		if (this.type.isForbidden(coords)) throw new IllegalComponentAdd();
		boolean next = false;
		for (ShipCoords s : coords.getNextTo()) {
			if (this.getComponent(s) != this.getEmpty()) {
				next = true;
				break;
			}
		}
		if (!next) throw new IllegalTargetException("Component is not adjacent to others.");
		component.onCreation(this, coords);
		this.components[coords.y][coords.x] = component;
		this.updateShip();
	}

	public void removeComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		System.out.println("Removed component on coords: "+coords+" for player '"+this.player.getUsername()+"'.");
		BaseComponent tmp = this.getComponent(coords);
		if (this.components[coords.y][coords.x] == this.empty) throw new IllegalTargetException();
		this.components[coords.y][coords.x] = this.empty;
		this.player.addScore(-1);
		tmp.onDelete(this);
		this.updateShip();
	}

	public void updateShip() {
		SpaceShipUpdateVisitor v = new SpaceShipUpdateVisitor();
		for (BaseComponent[] col : this.components) {
			for (BaseComponent cell : col) {
				cell.check(v);
			}
		}
		this.crew = v.getCrewMembers();
		this.shielded_directions = v.getDirections();
		this.containers = v.getContainers();
		this.cannon_power = v.getCannonPower();
		this.engine_power = v.getEnginePower();
		if (v.getCannonPower() > 0) {
			this.cannon_power += 2 * this.crew[2];
		}
		if (v.getEnginePower() > 0) {
			this.engine_power += 2 * this.crew[1];
		}
	}

	public void resetPower() {
		EnergyVisitor v = new EnergyVisitor(false);
		for (BaseComponent[] col : this.components) {
			for (BaseComponent cell : col) {
				cell.check(v);
			}
		}
		this.updateShip();
	}

	public void turnOn(ShipCoords coords_target, ShipCoords battery_location) {
		if (coords_target == null) throw new NullPointerException();
		if (battery_location == null) throw new NullPointerException();
		if (!this.powerable_coords.contains(coords_target)) throw new IllegalTargetException("Target is not powerable");
		if (!this.battery_coords.contains(battery_location)) throw new IllegalTargetException("Battery component wasn't present at location");
		BatteryComponent c = (BatteryComponent) getComponent(battery_location);
		EnergyVisitor v = new EnergyVisitor(true);
		if (c.getContains() == 0) throw new IllegalTargetException("No batteries found at location");
		c.takeOne();
		this.getComponent(coords_target).check(v);
		this.updateShip();
	}

	public BaseComponent getComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		if (coords.x < 0 || coords.x >= this.type.getWidth())
			throw new OutOfBoundsException("Illegal getComponent access.");
		if (coords.y < 0 || coords.y >= this.type.getHeight())
			throw new OutOfBoundsException("Illegal getComponent access.");
		return this.components[coords.y][coords.x];
	}

	public double getCannonPower() {
		return this.cannon_power;
	}

	public int getEnginePower() {
		return this.engine_power;
	}
	
	public int getEnergyPower() {
		return this.containers[0];
	}

	public boolean[] getShieldedDirections() {
		return this.shielded_directions;
	}

	public int getHeight() {
		return this.type.getHeight();
	}

	public int getWidth() {
		return this.type.getWidth();
	}

	public BaseComponent getEmpty() {
		return this.empty;
	}

	public void addStorageCoords(ShipCoords coords) {
		if (this.storage_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in storage coords");
		this.storage_coords.add(coords);
	}
	
	public void delStorageCoords(ShipCoords coords) {
		if (!this.storage_coords.contains(coords))
			throw new NotPresentException("Coords arent present in storage coords");
		this.storage_coords.remove(coords);
	}

	public void addCabinCoords(ShipCoords coords) {
		if (this.cabin_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in cabin coords");
		this.cabin_coords.add(coords);
	}

	public void delCabinCoords(ShipCoords coords) {
		if (!this.cabin_coords.contains(coords)) throw new NotPresentException("Coords arent present in cabin coords");
		this.cabin_coords.remove(coords);
	}
	
	public void addBatteryCoords(ShipCoords coords) {
		if (this.battery_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in battery coords");
		this.battery_coords.add(coords);
	}

	public void delBatteryCoords(ShipCoords coords) {
		if (!this.battery_coords.contains(coords))
			throw new NotPresentException("Coords arent present in battery coords");
		this.battery_coords.remove(coords);
	}
	
	public void addPowerableCoords(ShipCoords coords) {
		if (this.powerable_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in powerable coords");
		this.powerable_coords.add(coords);
	}
	
	public void delPowerableCoords(ShipCoords coords) {
		if (!this.powerable_coords.contains(coords))
			throw new NotPresentException("Coords arent present in powerable coords");
		this.powerable_coords.remove(coords);
	}
	
	public int getTotalCrew() {
		int sum = 0;
		for (int i : this.getCrew()) {
			sum += i;
		}
		return sum;
	}
	
	public ArrayList<ShipCoords> findConnectedCabins() {
		ArrayList<ShipCoords> res = new ArrayList<>();
		for (ShipCoords coords : this.cabin_coords) {
			for (BaseComponent c : this.getComponent(coords).getConnectedComponents(this)) {
				if (c.getCoords() == coords || !this.cabin_coords.contains(c.getCoords())) continue;
				res.add(coords);
				break;
			}
		}
		return res;
	}

	
	public int countExposedConnectors() {
		int sum = 0;
		for (BaseComponent[] col : this.components) {
			for (BaseComponent c : col) {
				if (c != this.empty) {
					if (this.getComponent(c.getCoords().up()) == this.empty) {
						if (c.getConnector(ComponentRotation.U000).getValue() != 0) sum++;
					}
					if (this.getComponent(c.getCoords().right()) == this.empty) {
						if (c.getConnector(ComponentRotation.U090).getValue() != 0) sum++;
					}
					if (this.getComponent(c.getCoords().down()) == this.empty) {
						if (c.getConnector(ComponentRotation.U180).getValue() != 0) sum++;
					}
					if (this.getComponent(c.getCoords().left()) == this.empty) {
						if (c.getConnector(ComponentRotation.U270).getValue() != 0) sum++;
					}
				}
			}
		}
		return sum;
	}

	
	public void handleMeteorite(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if (index < 0) return;
		//If it is in a possible row, handle the logic.
		if (p.getDimension() == ProjectileDimension.BIG) {
			//Handle adjacent rows.
			boolean found_cannon = findCannon(p.getDirection(), index);
			if (found_cannon) return;
			ShipCoords tmp = this.getFirst(p.getDirection(), index);
			if (tmp.equals(this.empty.getCoords())) return;
			//if (tmp==this.getCenterCabin()) return true -> gestita response nuova nave nuovo centro
			this.removeComponent(tmp);
			return;
		}
		boolean shielded = this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if (shielded) return;
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if (tmp.equals(this.empty.getCoords())) return;
		if (this.getComponent(tmp).getConnector(ComponentRotation.getRotation(p.getDirection().getOpposite())) == ConnectorType.EMPTY)
			return;
		this.removeComponent(tmp);
	}

	
	public void handleShot(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if (index < 0) return;
		//If it is in a possible row, handle the logic.
		boolean shielded = p.getDimension() != ProjectileDimension.BIG && this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if (shielded) return;
		//Traverse row/column and destroy first block found.
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if (tmp.equals(this.empty.getCoords())) return;
		this.removeComponent(tmp);
	}

	private int normalizeRoll(ProjectileDirection direction, int roll) {
		if (direction.getShift() % 2 == 0) {
			if (roll < this.type.getMinX() || roll > this.type.getMaxX()) return -1;
			return roll - (this.type.getMinX());
		}
		if (roll < this.type.getMinY() || roll > this.type.getMaxY()) return -1;
		return roll - (this.type.getMinY());
	}

	private ShipCoords getFirst(ProjectileDirection d, int index) {
		if (index < 0 || index >= (d.getShift() % 2 == 0 ? this.getWidth() : this.getHeight()))
			throw new OutOfBoundsException("Offset goes out of bounds");
		BaseComponent[] line = d.getShift() % 2 == 0 ? this.constructCol(index).clone() : this.components[index].clone();
		if (d.getShift() == 0 || d.getShift() == 3) Collections.reverse(Arrays.asList(line));
		for (BaseComponent c : line) {
			if (c == this.empty || this.type.isForbidden(c.getCoords())) continue;
			return c.getCoords();
		}
		return this.empty.getCoords();
	}


	private BaseComponent[] constructCol(int index) {
		//No validation needed, it's only used in getFirst.
		BaseComponent[] res = new BaseComponent[this.type.getHeight()];
		for (int i = 0; i < this.type.getHeight(); i++) {
			res[i] = this.components[i][index];
		}
		return res;
	}

	private boolean findCannon(ProjectileDirection d, int index) {
		LargeMeteorVisitor v = new LargeMeteorVisitor(d);
		if (d == ProjectileDirection.U180) {
			for (BaseComponent t : constructCol(index)) {
				t.check(v);
			}
			return v.getFoundCannon();
		}
		int[] indexes = new int[]{0, 0, 0};
		for (int i = -1; i <= 1; i++) {
			indexes[i + 1] = index + i;
		}
		for (int i : indexes) {
			if (i < 0) continue;
			BaseComponent[] line = d.getShift() % 2 == 0 ? constructCol(i) : this.components[i];
			for (BaseComponent t : line) {
				t.check(v);
			}
		}
		return v.getFoundCannon();
	}
	
	public int[] getContains() {
		return this.containers;
	}

	// public ShipCoords getCenter(){
	// 	return this.center;
	// }

	public boolean isCabin(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		return this.cabin_coords.contains(coords);
	}
	
	public ClientSpaceShip getClientSpaceShip() {
		ClientComponent[][] res = new ClientComponent[this.type.getHeight()][this.type.getWidth()];
		for (int x = 0; x < this.type.getWidth(); x++) {
			for (int y = 0; y < this.type.getHeight(); y++) {
				res[y][x] = this.components[y][x].getClientComponent();
			}
		}
		return new ClientSpaceShip(type, res, shielded_directions, cannon_power, engine_power, containers, crew);
	}

}

