package it.polimi.ingsw.model.adventure_cards;

import java.util.HashMap;

import it.polimi.ingsw.model.adventure_cards.enums.*;
import it.polimi.ingsw.model.adventure_cards.responses.*;
import it.polimi.ingsw.model.adventure_cards.utils.*;

public class LevelTwoCardFactory implements iCardFactory{
    
    private HashMap<Integer, iCard> cards;

    @Override
    public iCard getCard(int id) {
        if(this.cards.containsKey(id)) throw new IllegalArgumentException("Non valid card id.");
        return this.cards.get(id);
    }

    public LevelTwoCardFactory(){
        this.cards = new HashMap<Integer, iCard>(){{
            put(1, new SlaversCard(1, 2, 7, 4, 8));
            put(2, new SmugglersCard(2, 1, 
                new Planet(new int[]{0,0,2,1}), 
                3, 8));
            put(3, new PiratesCard(3, 2, 
                new ProjectileArray( 
                    new Projectile[]{ 
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG), 
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL), 
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG)}), 
                6, 7)
            );
            put(4, new StardustCard(4));
            put(5, new EpidemicCard(5));
            put(6, new OpenSpaceCard(6));
            put(7, new OpenSpaceCard(7));
            put(8, new OpenSpaceCard(8));
            put(9, new MeteorSwarmCard(9, 
                new ProjectileArray(
                    new Projectile[]{
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U090, ProjectileDimension.BIG),
                    new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL)}))
            );
            put(10, new MeteorSwarmCard(10, 
                new ProjectileArray(
                    new Projectile[]{
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.BIG),
                    new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U000, ProjectileDimension.SMALL)}))
            );
            put(11, new MeteorSwarmCard(10, 
                new ProjectileArray(
                    new Projectile[]{
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U270, ProjectileDimension.BIG),
                    new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL)}))
            );
            put(12, new PlanetCard(12, 4, 
                new Planet[]{
                new Planet(new int[]{0,0,1,3}),
                new Planet(new int[]{0,2,0,2}),
                new Planet(new int[]{4,0,0,1})})
            );
            put(13, new PlanetCard(13, 3, 
                new Planet[]{
                new Planet(new int[]{0,0,0,2}),
                new Planet(new int[]{0,4,0,0})})
            );
            put(14, new PlanetCard(14, 2, 
                new Planet[]{
                new Planet(new int[]{0,0,1,1}),
                new Planet(new int[]{1,1,1,0}),
                new Planet(new int[]{0,2,0,0}),
                new Planet(new int[]{0,0,1,0})})
            
            );
            put(15, new PlanetCard(15, 3, 
                new Planet[]{
                new Planet(new int[]{0,4,0,0}),
                new Planet(new int[]{0,0,2,0}),
                new Planet(new int[]{4,0,0,0})})
            );
            put(16, new CombatZoneCard(16, //FIXME in case of rework to combat zone( sicuramente ).
                new iCardResponse[]{
                    new CombatZoneProjectilesResponse(),
                    new SmugglerCardPenaltyResponse(3),
                    new DaysCardResponse(-4)},
                new ProjectileArray(
                    new Projectile[]{
                    new Projectile(ProjectileDirection.U180, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U090, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U270, ProjectileDimension.SMALL),
                    new Projectile(ProjectileDirection.U000, ProjectileDimension.BIG)}))
            );
            put(17, new AbandonedShipCard(17, 1, 4, 6));
            put(18, new AbandonedShipCard(18, 2, 5, 6));
            put(19, new AbandonedStationCard(19, 1, 
                new Planet(new int[]{0,0,1,1}),
                7)
            );
            put(20, new AbandonedStationCard(20, 1, 
                new Planet(new int[]{0,1,2,0}),
                8)
            );
        }};
    }
}
