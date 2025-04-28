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
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;


public class SpaceShip implements iSpaceShip {

	private final Player player;

	private final ArrayList<ShipCoords> storage_coords;
	private final ArrayList<ShipCoords> cabin_coords;
	private final ArrayList<ShipCoords> battery_coords;
	private final ArrayList<ShipCoords> powerable_coords;
	private final GameModeType type;
	private final iBaseComponent[][] components;
	private final iBaseComponent empty;
	private int[] crew;
	private ShipCoords center;
	private int[] containers;
	private boolean[] shielded_directions;
	private boolean broke_center;
	private double cannon_power = 0;
	private int engine_power = 0;

	public SpaceShip(GameModeType type,
					 Player player) {
		this.player = player;
		this.type = type;
		this.components = new iBaseComponent[type.getHeight()][type.getWidth()];
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
		for (iBaseComponent[] t : this.components) {
			Arrays.fill(t, this.empty); // E' la stessa reference;
		}
		this.addComponent(new StartingCabinComponent(1, new ConnectorType[]{
						ConnectorType.UNIVERSAL,
						ConnectorType.UNIVERSAL,
						ConnectorType.UNIVERSAL,
						ConnectorType.UNIVERSAL},
						ComponentRotation.U000,
						player.getColor(),
						center),
				center);
		Arrays.fill(shielded_directions, false);
		Arrays.fill(containers, 0);
		Arrays.fill(crew, 0);
		this.updateShip();
	}

	@Override
	public GameModeType getType() {
		return this.type;
	}

	@Override
	public int[] getCrew() {
		return this.crew;
	}

	@Override
	public VerifyResult[][] verify() {
		VerifyResult[][] res = new VerifyResult[this.type.getHeight()][];
		Queue<iBaseComponent> queue = new ArrayDeque<iBaseComponent>();
		for (int i = 0; i < res.length; i++) {
			res[i] = new VerifyResult[this.type.getWidth()];
			Arrays.fill(res[i], VerifyResult.UNCHECKED);
		}
		queue.add(this.getComponent(this.getCenter()));
		iBaseComponent tmp = null;
		while (!queue.isEmpty()) {
			tmp = queue.poll();
			if (!tmp.verify(this)) res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.BRKN_COMP;
			else res[tmp.getCoords().y][tmp.getCoords().x] = VerifyResult.GOOD_COMP;
			for (iBaseComponent c : tmp.getConnectedComponents(this)) {
				if (c == this.empty) continue;
				ShipCoords xy = c.getCoords();
				if (res[xy.y][xy.x] == VerifyResult.UNCHECKED) queue.add(c);
			}
		}
		for (int t = 0; t < this.getHeight(); t++) {
			for (int r = 0; r < this.getWidth(); r++) {
				if (res[t][r] == VerifyResult.UNCHECKED && this.components[t][r]!=this.empty) res[t][r] = VerifyResult.NOT_LNKED;
			}
		}
		return res;
	}

	@Override
	public boolean verifyAndClean() {
		VerifyResult[][] ver = this.verify();
		boolean had_to_clean = false;
		for (int i = 0; i < this.type.getHeight(); i++) {
			for (int j = 0; j < this.type.getWidth(); j++) {
				if (ver[i][j] != VerifyResult.NOT_LNKED) continue;
				had_to_clean = true;
				System.out.println("Cleaning up!");
				this.removeComponent(new ShipCoords(this.type, j, i));
			}
		}
		return had_to_clean;
	}

	@Override
	public void addComponent(iBaseComponent component, ShipCoords coords) {
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
			if (s != this.getEmpty()) {
				next = true;
				break;
			}
		}
		if (!next) throw new IllegalTargetException("Component is not adjacent to others.");
		component.onCreation(this, coords);
		this.components[coords.y][coords.x] = component;
		this.updateShip();
	}

	@Override
	public void removeComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		if (coords == this.getCenter()) this.setBrokeCenter();
		iBaseComponent tmp = this.getComponent(coords);
		if (this.components[coords.y][coords.x] == this.empty) throw new IllegalTargetException();
		this.components[coords.y][coords.x] = this.empty;
		this.player.addScore(-1);
		tmp.onDelete(this);
		System.out.println("Removed component on coords: "+coords+" for player '"+this.player.getUsername()+"'.");
		this.updateShip();
	}

	@Override
	public void updateShip() {
		SpaceShipUpdateVisitor v = new SpaceShipUpdateVisitor();
		for (iBaseComponent[] col : this.components) {
			for (iBaseComponent cell : col) {
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

	@Override
	public void resetPower() {
		EnergyVisitor v = new EnergyVisitor(false);
		for (iBaseComponent[] col : this.components) {
			for (iBaseComponent cell : col) {
				cell.check(v);
			}
		}
		this.updateShip();
	}

	@Override
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

	@Override
	public iBaseComponent getComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		if (coords.x < 0 || coords.x >= this.type.getWidth())
			throw new OutOfBoundsException("Illegal getComponent access.");
		if (coords.y < 0 || coords.y >= this.type.getHeight())
			throw new OutOfBoundsException("Illegal getComponent access.");
		return this.components[coords.y][coords.x];
	}

	@Override
	public double getCannonPower() {
		return this.cannon_power;
	}

	@Override
	public int getEnginePower() {
		return this.engine_power;
	}

	@Override
	public int getEnergyPower() {
		return this.containers[0];
	}

	@Override
	public boolean[] getShieldedDirections() {
		return this.shielded_directions;
	}

	@Override
	public int getHeight() {
		return this.type.getHeight();
	}

	@Override
	public int getWidth() {
		return this.type.getWidth();
	}

	@Override
	public iBaseComponent getEmpty() {
		return this.empty;
	}

	@Override
	public void addStorageCoords(ShipCoords coords) {
		if (this.storage_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in storage coords");
		this.storage_coords.add(coords);
	}

	@Override
	public void delStorageCoords(ShipCoords coords) {
		if (!this.storage_coords.contains(coords))
			throw new NotPresentException("Coords arent present in storage coords");
		this.storage_coords.remove(coords);
	}

	@Override
	public void addCabinCoords(ShipCoords coords) {
		if (this.cabin_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in cabin coords");
		this.cabin_coords.add(coords);
	}

	@Override
	public void delCabinCoords(ShipCoords coords) {
		if (!this.cabin_coords.contains(coords)) throw new NotPresentException("Coords arent present in cabin coords");
		this.cabin_coords.remove(coords);
	}

	@Override
	public void addBatteryCoords(ShipCoords coords) {
		if (this.battery_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in battery coords");
		this.battery_coords.add(coords);
	}

	@Override
	public void delBatteryCoords(ShipCoords coords) {
		if (!this.battery_coords.contains(coords))
			throw new NotPresentException("Coords arent present in battery coords");
		this.battery_coords.remove(coords);
	}

	@Override
	public void addPowerableCoords(ShipCoords coords) {
		if (this.powerable_coords.contains(coords))
			throw new NotUniqueException("Coords are already present in powerable coords");
		this.powerable_coords.add(coords);
	}

	@Override
	public void delPowerableCoords(ShipCoords coords) {
		if (!this.powerable_coords.contains(coords))
			throw new NotPresentException("Coords arent present in powerable coords");
		this.powerable_coords.remove(coords);
	}

	@Override
	public int getTotalCrew() {
		int sum = 0;
		for (int i : this.getCrew()) {
			sum += i;
		}
		return sum;
	}

	@Override
	public void setCenter(ShipCoords new_center) throws ForbiddenCallException {
		if (this.type.isForbidden(new_center) || this.getComponent(new_center) == this.empty)
			throw new IllegalTargetException("New center is either forbidden or illegal.");
		if (!broke_center) throw new ForbiddenCallException("Cabin isn't broken");
		this.center = new_center;
		this.broke_center = false;
		this.verifyAndClean();
	}

	@Override
	public ShipCoords getCenter() {
		return this.center;
	}

	@Override
	public ArrayList<ShipCoords> findConnectedCabins() {
		ArrayList<ShipCoords> res = new ArrayList<>();
		for (ShipCoords coords : this.cabin_coords) {
			for (iBaseComponent c : this.getComponent(coords).getConnectedComponents(this)) {
				if (c.getCoords() == coords || !this.cabin_coords.contains(c.getCoords())) continue;
				res.add(coords);
				break;
			}
		}
		return res;
	}

	@Override
	public int countExposedConnectors() {
		int sum = 0;
		for (iBaseComponent[] col : this.components) {
			for (iBaseComponent c : col) {
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

	@Override
	public boolean handleMeteorite(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if (index < 0) return false;
		//If it is in a possible row, handle the logic.
		if (p.getDimension() == ProjectileDimension.BIG) {
			//Handle adjacent rows.
			boolean found_cannon = findCannon(p.getDirection(), index);
			if (found_cannon) return false;
			ShipCoords tmp = this.getFirst(p.getDirection(), index);
			if (tmp.equals(this.empty.getCoords())) return false;
			//if (tmp==this.getCenterCabin()) return true -> gestita response nuova nave nuovo centro
			this.removeComponent(tmp);
			return this.broke_center;
		}
		boolean shielded = this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if (shielded) return false;
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if (tmp.equals(this.empty.getCoords())) return false;
		if (this.getComponent(tmp).getConnectors()[p.getDirection().getOpposite().getShift()] == ConnectorType.EMPTY)
			return false;
		this.removeComponent(tmp);
		return this.broke_center;
	}

	@Override
	public boolean handleShot(Projectile p) {
		//Normalize Roll and see if it grazes or is in a possible row.
		int index = normalizeRoll(p.getDirection(), p.getOffset());
		if (index < 0) return false;
		//If it is in a possible row, handle the logic.
		boolean shielded = p.getDimension() != ProjectileDimension.BIG && this.shielded_directions[p.getDirection().getOpposite().getShift()];
		if (shielded) return false;
		//Traverse row/column and destroy first block found.
		ShipCoords tmp = this.getFirst(p.getDirection(), index);
		if (tmp.equals(this.empty.getCoords())) return false;
		this.removeComponent(tmp);
		return this.broke_center;
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
		//XXX fix.
		if (index < 0 || index >= (d.getShift() % 2 == 0 ? this.getWidth() : this.getHeight()))
			throw new OutOfBoundsException("Offset goes out of bounds");
		iBaseComponent[] line = d.getShift() % 2 == 0 ? this.constructCol(index) : this.components[index];
		if (d.getShift() == 0 || d.getShift() == 3) Collections.reverse(Arrays.asList(line.clone()));
		for (iBaseComponent c : line) {
			if (c == this.empty || this.type.isForbidden(c.getCoords())) continue;
			return c.getCoords();
		}
		return this.empty.getCoords();
	}

	private iBaseComponent[] constructCol(int index) {
		//No validation needed, it's only used in getFirst.
		iBaseComponent[] res = new iBaseComponent[this.type.getHeight()];
		for (int i = 0; i < this.type.getHeight(); i++) {
			res[i] = this.components[i][index];
		}
		return res;
	}

	private boolean findCannon(ProjectileDirection d, int index) {
		LargeMeteorVisitor v = new LargeMeteorVisitor(d);
		if (d == ProjectileDirection.U180) {
			for (iBaseComponent t : constructCol(index)) {
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
			iBaseComponent[] line = d.getShift() % 2 == 0 ? constructCol(i) : this.components[i];
			for (iBaseComponent t : line) {
				t.check(v);
			}
		}
		return v.getFoundCannon();
	}

	@Override
	public boolean getBrokeCenter() {
		return this.broke_center;
	}

	@Override
	public void setBrokeCenter() {
		this.broke_center = true;
	}

	@Override
	public int[] getContains() {
		return this.containers;
	}

	@Override
	public boolean isCabin(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		return this.cabin_coords.contains(coords);
	}

	@Override
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

