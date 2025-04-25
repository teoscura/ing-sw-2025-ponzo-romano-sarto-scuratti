package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import it.polimi.ingsw.model.cards.utils.*;

public class LevelOneCardFactory implements iCardFactory {

	private final HashMap<Integer, iCard> cards;

	@Override
	public iCard getCard(int id) {
		if (!this.cards.containsKey(id)) throw new IllegalArgumentException("Non valid card id.");
		return this.cards.get(id);
	}

	public LevelOneCardFactory() {
		this.cards = new HashMap<Integer, iCard>() {{
			put(1, new SlaversCard(1, 1, 6, 3, 5));
			put(2, new SmugglersCard(2, 1,
					new Planet(new int[]{1, 1, 1, 0}),
					2, 4)
			);
			put(3, new PiratesCard(3, 1,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL)}),
					5, 4)
			);
			put(4, new StardustCard(4));
			put(5, new OpenSpaceCard(5));
			put(6, new OpenSpaceCard(6));
			put(7, new OpenSpaceCard(7));
			put(8, new OpenSpaceCard(8));
			put(9, new MeteorSwarmCard(9,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL)}))
			);
			put(10, new MeteorSwarmCard(10,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL)}))
			);
			put(11, new MeteorSwarmCard(11,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG)}))
			);
			put(12, new PlanetCard(12, 3,
					new Planet[]{
							new Planet(new int[]{3, 1, 0, 1}),
							new Planet(new int[]{1, 0, 1, 1}),
							new Planet(new int[]{3, 0, 0, 1}),
							new Planet(new int[]{0, 1, 0, 1})})
			);
			put(13, new PlanetCard(13, 2,
					new Planet[]{
							new Planet(new int[]{0, 0, 0, 2}),
							new Planet(new int[]{2, 0, 0, 1}),
							new Planet(new int[]{0, 0, 1, 0})})
			);
			put(14, new PlanetCard(14, 3,
					new Planet[]{
							new Planet(new int[]{2, 1, 1, 0}),
							new Planet(new int[]{0, 0, 2, 0})})
			);
			put(15, new PlanetCard(15, 1,
					new Planet[]{
							new Planet(new int[]{0, 2, 0, 0}),
							new Planet(new int[]{0, 0, 1, 0}),
							new Planet(new int[]{3, 0, 0, 0})})
			);
			put(16, new CombatZoneCard(16,
					new ArrayList<>(Arrays.asList(new CombatZoneSection(
									CombatZoneCriteria.LEAST_CREW,
									CombatZonePenalty.DAYS, 3),
							new CombatZoneSection(
									CombatZoneCriteria.LEAST_ENGINE,
									CombatZonePenalty.CREW, 2),
							new CombatZoneSection(
									CombatZoneCriteria.LEAST_CANNON,
									CombatZonePenalty.SHOTS))),
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U000, ProjectileDimension.BIG)}))
			);
			put(17, new AbandonedShipCard(17, 1, 2, 3));
			put(18, new AbandonedShipCard(18, 1, 3, 4));
			put(19, new AbandonedStationCard(19, 1,
					new Planet(new int[]{0, 1, 1, 0}),
					5)
			);
			put(20, new AbandonedStationCard(20, 1,
					new Planet(new int[]{0, 0, 0, 2}),
					6)
			);
		}};
	}

}
