//Done.
package it.polimi.ingsw.model.cards.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ProjectileArray implements Serializable {

	private final ArrayList<Projectile> projectiles;

	public ProjectileArray(Projectile[] projectiles) {
		if (projectiles == null || projectiles.length == 0)
			throw new NullPointerException("Meteorites Array is empty or null");
		int max = 12;
		for (int i = 0; i < projectiles.length; i++) {
			int value = ThreadLocalRandom.current().nextInt(1, max + 1);
			projectiles[i] = new Projectile(projectiles[i].getDirection(),
					projectiles[i].getDimension(),
					value);
		}
		this.projectiles = new ArrayList<>(Arrays.asList(projectiles));

	}

	public ArrayList<Projectile> getProjectiles() {
		return this.projectiles;
	}

	public ProjectileArray copy(){
		Projectile[] copy = new Projectile[this.projectiles.size()];
		int i = 0;
		for(Projectile p : projectiles){
			copy[i] = new Projectile(p.getDirection(), p.getDimension(), p.getOffset());
		}
		return new ProjectileArray(copy);
	}
}
