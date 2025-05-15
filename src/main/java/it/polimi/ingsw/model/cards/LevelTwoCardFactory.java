package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LevelTwoCardFactory implements iCardFactory {

	private final HashMap<Integer, iCard> cards;

	public LevelTwoCardFactory() {
		this.cards = new HashMap<Integer, iCard>() {{
			put(101, new SlaversCard(101, 2, 7F, 4, 8));
			put(102, new SmugglersCard(102, 1,
					new Planet(new int[]{0, 0, 2, 1}),
					3, 8F));
			put(103, new PiratesCard(103, 2,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG)}),
					6F, 7)
			);
			put(104, new StardustCard(104));
			put(105, new EpidemicCard(105));
			put(106, new OpenSpaceCard(106));
			put(107, new OpenSpaceCard(107));
			put(108, new OpenSpaceCard(108));
			put(109, new MeteorSwarmCard(109,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL)}))
			);
			put(110, new MeteorSwarmCard(110,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL)}))
			);
			put(111, new MeteorSwarmCard(111,
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.BIG),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL)}))
			);
			put(112, new PlanetCard(112, 4,
					new Planet[]{
							new Planet(new int[]{0, 0, 1, 3}),
							new Planet(new int[]{0, 2, 0, 2}),
							new Planet(new int[]{4, 0, 0, 1})})
			);
			put(113, new PlanetCard(113, 3,
					new Planet[]{
							new Planet(new int[]{0, 0, 0, 2}),
							new Planet(new int[]{0, 4, 0, 0})})
			);
			put(114, new PlanetCard(114, 2,
					new Planet[]{
							new Planet(new int[]{0, 0, 1, 1}),
							new Planet(new int[]{1, 1, 1, 0}),
							new Planet(new int[]{0, 2, 0, 0}),
							new Planet(new int[]{0, 0, 1, 0})})
			);
			put(115, new PlanetCard(115, 3,
					new Planet[]{
							new Planet(new int[]{0, 4, 0, 0}),
							new Planet(new int[]{0, 0, 2, 0}),
							new Planet(new int[]{4, 0, 0, 0})})
			);
			put(116, new CombatZoneCard(116,
					new ArrayList<>(Arrays.asList(new CombatZoneSection(
									CombatZoneCriteria.LEAST_CANNON,
									CombatZonePenalty.DAYS, 4),
							new CombatZoneSection(
									CombatZoneCriteria.LEAST_ENGINE,
									CombatZonePenalty.CARGO, 3),
							new CombatZoneSection(
									CombatZoneCriteria.LEAST_CREW,
									CombatZonePenalty.SHOTS))),
					new ProjectileArray(
							new Projectile[]{
									new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL),
									new Projectile(ProjectileDirection.U000, ProjectileDimension.BIG)}))
			);
			put(117, new AbandonedShipCard(117, 1, 4, 6));
			put(118, new AbandonedShipCard(118, 2, 5, 6));
			put(119, new AbandonedStationCard(119, 1,
					new Planet(new int[]{0, 0, 1, 1}),
					7)
			);
			put(120, new AbandonedStationCard(120, 1,
					new Planet(new int[]{0, 1, 2, 0}),
					8)
			);
		}};
	}

	@Override
	public iCard getCard(int id) {
		if (!this.cards.containsKey(id)) throw new IllegalArgumentException("Non valid card id.");
		return this.cards.get(id);
	}
}
