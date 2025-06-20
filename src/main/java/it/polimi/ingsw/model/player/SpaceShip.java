package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.NotPresentException;
import it.polimi.ingsw.exceptions.NotUniqueException;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.cards.visitors.LargeMeteorVisitor;
import it.polimi.ingsw.model.client.components.ClientComponent;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.EmptyComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.visitors.EnergyVisitor;
import it.polimi.ingsw.model.components.visitors.SpaceShipUpdateVisitor;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.LoggerLevel;

import java.io.Serializable;
import java.util.*;

/**
 * <h2>SpaceShip</h2>
 * <p>
 * Represents the player's ship in the game.
 * The spaceship is a grid of components, dynamically assembled by the player,
 * and responsible for handling structure validation, component effects,
 * combat resolution, and state updates.
 * </p>
 */
public class SpaceShip implements Serializable {

	private final Player player;
	private final ArrayList<ShipCoords> storage_coords;
	private final ArrayList<ShipCoords> cabin_coords;
	private final ArrayList<ShipCoords> battery_coords;
	private final ArrayList<ShipCoords> powerable_coords;
	private final GameModeType type;
	private final BaseComponent[][] components;
	private final BaseComponent empty;
	private ArrayList<ArrayList<ShipCoords>> blobs;
	private int[] crew;
	private ShipCoords center;
	private int[] containers;
	private boolean[] shielded_directions;
	private double cannon_power = 0;
	private int engine_power = 0;

	/**
	 * Constructs a {@link it.polimi.ingsw.model.player.SpaceShip} object.
	 * 
	 * @param type {@link GameModeType} Type of the ship.
	 * @param player {@link it.polimi.ingsw.model.player.Player} Player it belongs to.
	 */
	public SpaceShip(GameModeType type,
					 Player player) {
		this.player = player;
		this.type = type;
		this.components = new BaseComponent[type.getHeight()][type.getWidth()];
		this.shielded_directions = new boolean[4];
		this.containers = new int[5];
		this.crew = new int[3];
		this.empty = new EmptyComponent();
		this.empty.onCreation(this, new ShipCoords(type, 0, 0));
		this.storage_coords = new ArrayList<ShipCoords>();
		this.cabin_coords = new ArrayList<ShipCoords>();
		this.battery_coords = new ArrayList<ShipCoords>();
		this.powerable_coords = new ArrayList<ShipCoords>();
		this.center = type.getCenterCabin();

		for (BaseComponent[] t : this.components) {
			Arrays.fill(t, this.empty); // E' la stessa reference;
		}
		BaseComponent scabin = new StartingCabinComponent(player.getColor().getID(), new ConnectorType[]{
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL,
				ConnectorType.UNIVERSAL},
				ComponentRotation.U000,
				player.getColor());
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

	/**
	 * Returns the count of crew members by type (index 0 = human, 1 = brown, 2 = purple).
	 * 
	 * @return An array of crew counts.
	 */
	public int[] getCrew() {
		return this.crew;
	}

	/**
	 * Performs a bulk verification of all components in the ship.
	 * Returns a 2D matrix of {@link VerifyResult}.
	 * 
	 * @return Verification result grid.
	 */
	public VerifyResult[][] bulkVerify() {
		VerifyResult[][] res = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
		for (int y = 0; y < this.type.getHeight(); y++) {
			for (int x = 0; x < this.type.getWidth(); x++) {
				res[y][x] = this.components[y][x] == this.empty ? VerifyResult.UNCHECKED : VerifyResult.NOT_LNKED;
			}
		}
		for (int y = 0; y < this.type.getHeight(); y++) {
			for (int x = 0; x < this.type.getWidth(); x++) {
				if (res[y][x] == VerifyResult.UNCHECKED) continue;
				BaseComponent tmp = this.components[y][x];
				if (!tmp.verify(this)) res[y][x] = VerifyResult.BRKN_COMP;
				else res[y][x] = VerifyResult.GOOD_COMP;
			}
		}
		return res;
	}

	/**
	 * Returns {@code true} if no broken components are found.
	 * 
	 * @return Overall ship integrity status.
	 */
	public boolean bulkVerifyResult() {
		var t = bulkVerify();
		for (var row : t) {
			for (var r : row) {
				if (r == VerifyResult.BRKN_COMP) return false;
			}
		}
		return true;
	}

	/**
	 * Returns the number of distinct component blobs (connected groups).
	 * 
	 * @return Number of blobs.
	 */
	public int getBlobsSize() {
		this.updateShipBlobs();
		return this.blobs.size();
	}

	/**
	 * Updates the list of "blobs" composing the {@link it.polimi.ingsw.model.player.SpaceShip}.
	 * Blobs are sets of connected {@link BaseComponent components}.
	 */
	public void updateShipBlobs() {
		ArrayList<ArrayList<ShipCoords>> res = new ArrayList<>();
		VerifyResult[][] map = new VerifyResult[this.type.getHeight()][this.type.getWidth()];
		for (int y = 0; y < type.getHeight(); y++) {
			for (int x = 0; x < type.getWidth(); x++) {
				map[y][x] = this.components[y][x] == this.empty ? VerifyResult.UNCHECKED : VerifyResult.NOT_LNKED;
			}
		}
		for (int y = 0; y < type.getHeight(); y++) {
			for (int x = 0; x < type.getWidth(); x++) {
				if (map[y][x] != VerifyResult.NOT_LNKED) continue;
				res.add(this.verifyBlob(map, new ShipCoords(this.type, x, y)));
			}
		}
		this.blobs = res;
	}

	/**
	 * Utility method, uses a BFS to build a list of {@link it.polimi.ingsw.model.player.ShipCoords coordinates} pointing to all {@link BaseComponent components} part of the same blob.
	 * 
	 * @param map Matrix of {@link VerifyResult} used to check which {@link it.polimi.ingsw.model.player.ShipCoords coordinates} were already visited.
	 * @param starting_point {@link it.polimi.ingsw.model.player.ShipCoords} Starting point of the BFS.
	 * @return ArrayList of {@link it.polimi.ingsw.model.player.ShipCoords} containting all {@link it.polimi.ingsw.model.player.ShipCoords coordinates} of all blob {@link BaseComponent components}. 
	 */
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

	/**
	 * Selects a blob from {@link it.polimi.ingsw.model.player.SpaceShip} containing the specified {@link it.polimi.ingsw.model.player.ShipCoords coordinate}, discarding all other {@link BaseComponent components} contained in other blobs.
	 * 
	 * @param blob_coord {@link it.polimi.ingsw.model.player.ShipCoords} Coordinate belonging to the blob to keep.
	 * @throws ForbiddenCallException if only one blob exists.
	 * @throws IllegalTargetException if the coordinates are invalid.
	 */
	public void selectShipBlob(ShipCoords blob_coord) throws ForbiddenCallException {
		updateShipBlobs();
		if (this.blobs.size() <= 1) throw new ForbiddenCallException();
		for (ArrayList<ShipCoords> blob : this.blobs) {
			if (!blob.contains(blob_coord)) continue;
			ArrayList<ArrayList<ShipCoords>> previous = this.blobs;
			previous.remove(blob);
			previous.stream().forEach(b -> b.stream().forEach(c -> this.removeComponent(c)));
			this.blobs = new ArrayList<>() {{
				add(blob);
			}};
			this.center = blob_coord;
			updateShip();
			return;
		}
		throw new IllegalTargetException("Blob coordinate was invalid!");
	}

	/**
	 * Adds a {@link BaseComponent component} to the {@link it.polimi.ingsw.model.player.SpaceShip} at the specified {@link it.polimi.ingsw.model.player.ShipCoords coordinates}.
	 * 
	 * @param component {@link it.polimi.ingsw.model.components.BaseComponent}  The component to add.
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} The location to place it.
	 * @throws IllegalComponentAdd if the location is illegal or occupied.
	 * @throws IllegalTargetException if not adjacent to any existing component.
	 */
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

	/**
	 * Removes the {@link BaseComponent component} at the given {@link it.polimi.ingsw.model.player.ShipCoords coordinates} and updates the ship state.
	 * 
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coordinates of the {@link BaseComponent component} to remove.
	 * @throws IllegalTargetException if no {@link BaseComponent component} is present at the location.
	 */
	public void removeComponent(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		if (player.getDescriptor() != null)
			Logger.getInstance().print(LoggerLevel.MODEL, "[" + this.player.getDescriptor().getID() + "] " + "[Player: '" + this.player.getUsername() + "'] " + "Removed component on coords: " + coords + ".");
		BaseComponent tmp = this.getComponent(coords);
		if (this.components[coords.y][coords.x] == this.empty) throw new IllegalTargetException();
		this.components[coords.y][coords.x] = this.empty;
		this.player.giveCredits(-1);
		tmp.onDelete(this);
		this.updateShip();
	}

	/**
	 * Updates the ship state based on the currently installed {@link BaseComponent components}.
	 * 
	 * Calculates crew, power, containers, cannon power, engine power.
	 */
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

	/**
	 * Resets all powered {@link BaseComponent components} (e.g., cannons, engines) to their unpowered state.
	 */
	public void resetPower() {
		EnergyVisitor v = new EnergyVisitor(false);
		for (BaseComponent[] col : this.components) {
			for (BaseComponent cell : col) {
				cell.check(v);
			}
		}
		this.updateShip();
	}

	/**
	 * Powers on a target {@link BaseComponent component} using a battery located at another {@link it.polimi.ingsw.model.player.ShipCoords coordinate}
	 * @param coords_target {@link it.polimi.ingsw.model.player.ShipCoords} Coordinates to the target {@link BaseComponent component}.
	 * @param battery_location {@link it.polimi.ingsw.model.player.ShipCoords} Coordinates to the battery {@link BaseComponent component}.
	 * @throws IllegalTargetException if invalid component or battery {@link it.polimi.ingsw.model.player.ShipCoords}.
	 */
	public void turnOn(ShipCoords coords_target, ShipCoords battery_location) {
		if (coords_target == null) throw new NullPointerException();
		if (battery_location == null) throw new NullPointerException();
		if (!this.powerable_coords.contains(coords_target)) throw new IllegalTargetException("Target is not powerable");
		if (!this.battery_coords.contains(battery_location))
			throw new IllegalTargetException("Battery component wasn't present at location");
		BatteryComponent c = (BatteryComponent) getComponent(battery_location);
		EnergyVisitor v = new EnergyVisitor(true);
		if (c.getContains() == 0) throw new IllegalTargetException("No batteries found at location");
		c.takeOne();
		this.getComponent(coords_target).check(v);
		this.updateShip();
	}

	/**
	 * Returns the {@link BaseComponent component} at the specified {@link it.polimi.ingsw.model.player.ShipCoords coordinates}.
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Location to retrieve.
	 * @return {@link it.polimi.ingsw.model.components.BaseComponent}  Component Reference, corresponding to {@link it.polimi.ingsw.model.player.SpaceShip#getEmpty()} if not set or invalid.
	 * @throws OutOfBoundsException if {@link it.polimi.ingsw.model.player.ShipCoords coordinates} are outside grid.
	 */
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

	/**
	 * Returns the number of batteries present.
	 * 
	 * @return Battery count.
	 */
	public int getEnergyPower() {
		return this.containers[0];
	}

	/**
	 * Returns a boolean array indicating shield coverage in the 4 directions.
	 * 
	 * @return Array of booleans.
	 */
	public boolean[] getShieldedDirections() {
		return this.shielded_directions;
	}

	/**
	 * Returns the height of the {@link it.polimi.ingsw.model.player.SpaceShip ship}'s grid.
	 * 
	 * @return Height.
	 */
	public int getHeight() {
		return this.type.getHeight();
	}

	/**
	 * Returns the width of the {@link it.polimi.ingsw.model.player.SpaceShip ship}'s grid.
	 * 
	 * @return Width.
	 */
	public int getWidth() {
		return this.type.getWidth();
	}

	/**
	 * Returns the {@link it.polimi.ingsw.model.player.SpaceShip ship}'s reference to its {@link EmptyComponent}.
	 * @return {@link it.polimi.ingsw.model.components.BaseComponent}  reference to owned {@link EmptyComponent}.
	 */
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

	/**
	 * Returns the total number of crew members.
	 * 
	 * @return Total number of crew members.
	 */
	public int getTotalCrew() {
		int sum = 0;
		for (int i : this.getCrew()) {
			sum += i;
		}
		return sum;
	}

	/**
	 * Finds cabins that are connected to at least one other cabin.
	 * 
	 * @return ArrayList of {@link it.polimi.ingsw.model.player.ShipCoords}, each one pointing to a {@link it.polimi.ingsw.model.components.CabinComponent} that is adjacent to another {@link it.polimi.ingsw.model.components.CabinComponent}.
	 */
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

	/**
	 * Counts how many exposed connectors are not connected to other {@link BaseComponent components}.
	 * 
	 * @return The number of exposed connectors.
	 */
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

	/**
	 * Handles an incoming meteorite based on its direction and offset.
	 * Applies shield logic and potentially removes {@link it.polimi.ingsw.model.components.BaseComponent} .
	 * 
	 * @param p {@link it.polimi.ingsw.model.cards.utils.Projectile} Meteor to handle.
	 */
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


	/**
	 * Handles a laser shot based on direction and offset.
	 * Applies shield logic and removes first impacted component if not deflected or shielded.
	 * 
	 * @param p {@link it.polimi.ingsw.model.cards.utils.Projectile} The projectile to handle.
	 */
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

	/**
	 * @param index Index of the column to fetch.
	 * @return An Array of {@link it.polimi.ingsw.model.components.BaseComponent}  representing all components along a column of the {@link it.polimi.ingsw.model.player.SpaceShip}.
	 */
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

	/**
	 * Returns the current cargo quantities stored in the ship.
	 * 
	 * @return Array of integers representing the total sum of each {@link it.polimi.ingsw.model.components.enums.ShipmentType}.
	 */
	public int[] getContains() {
		return this.containers;
	}

	/**
	 * Checks if the provided coordinates belong to a cabin component.
	 * 
	 * @param coords {@link it.polimi.ingsw.model.player.ShipCoords} Coordinate to verify.
	 * @return {@code true} if it is a cabin, {@code false} otherwise.
	 */
	public boolean isCabin(ShipCoords coords) {
		if (coords == null) throw new NullPointerException();
		return this.cabin_coords.contains(coords);
	}

	/**
	 * Converts the internal ship state to a serializable {@link ClientSpaceShip}
	 * used for network transmission to clients.
	 * 
	 * @return {@link ClientSpaceShip} A complete client-side representation of the ship
	 */
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

