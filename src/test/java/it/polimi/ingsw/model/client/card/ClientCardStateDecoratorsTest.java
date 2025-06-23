package it.polimi.ingsw.model.client.card;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.model.cards.utils.CombatZoneCriteria;
import it.polimi.ingsw.model.cards.utils.CombatZonePenalty;
import it.polimi.ingsw.model.cards.utils.CombatZoneSection;
import it.polimi.ingsw.model.cards.utils.Planet;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientCardStateDecoratorsTest {
    @Test
    public void decorators(){
        ClientBaseCardState base = new ClientBaseCardState("base", 1);
        ClientAwaitConfirmCardStateDecorator d1 = new ClientAwaitConfirmCardStateDecorator(base, new ArrayList<>(){{add(PlayerColor.RED);}});
        ClientCargoPenaltyCardStateDecorator d2 = new ClientCargoPenaltyCardStateDecorator(d1, PlayerColor.RED, new int[]{0,1,1,1,1});
        ClientCargoRewardCardStateDecorator d3 = new ClientCargoRewardCardStateDecorator(d2, PlayerColor.RED, 2, new int[]{1,1,1,1});
        var sect = new CombatZoneSection(CombatZoneCriteria.LEAST_CANNON, CombatZonePenalty.SHOTS);
        ClientCombatZoneIndexCardStateDecorator d4 = new ClientCombatZoneIndexCardStateDecorator(d3, sect, 2);
        ClientCreditsRewardCardStateDecorator d5 = new ClientCreditsRewardCardStateDecorator(d4, PlayerColor.RED, 2, 1);
        ClientCrewPenaltyCardStateDecorator d6 = new ClientCrewPenaltyCardStateDecorator(d5, PlayerColor.RED, 1);
        ClientEnemyCardStateDecorator d7 = new ClientEnemyCardStateDecorator(d6, 1, CombatZonePenalty.CARGO, 2);
        ArrayList<Planet> l = new ArrayList<>(){{add(new Planet(0, new int[]{1,1,1,1}));}};
        ClientLandingCardStateDecorator d8 = new ClientLandingCardStateDecorator(d7, PlayerColor.RED, 1, 2, 3, l);
        var p = new Projectile(ProjectileDirection.U000, ProjectileDimension.BIG, 2);
        ClientMeteoriteCardStateDecorator d9 = new ClientMeteoriteCardStateDecorator(d8, p);
        ClientNewCenterCardStateDecorator d10 = new ClientNewCenterCardStateDecorator(d9, new ArrayList<>(){{add(PlayerColor.RED);}});
        ClientProjectileCardStateDecorator d11 = new ClientProjectileCardStateDecorator(d10, p);
        
        assertEquals(d11.getProjectile(), p);
        assertEquals(d10.getAwaiting(), new ArrayList<>(){{add(PlayerColor.RED);}});
        assertEquals(d9.getProjectile(), p);
        assertEquals(d8.getCredits(), 3);
        assertEquals(d8.getCrewNeeded(), 2);
        assertEquals(d8.getDaysTaken(), 1);
        assertEquals(d8.getTurn(), PlayerColor.RED);
        assertEquals(d8.getAvailable(), l);
        assertEquals(d7.getPower(), 1); 
        assertEquals(d7.getPenalty(), CombatZonePenalty.CARGO);
        assertEquals(d7.getAmount(), 2);
        assertEquals(d6.getCrewLost(), 1);
        assertEquals(d6.getTurn(), PlayerColor.RED);
        assertEquals(d5.getCredits(), 2);
        assertEquals(d5.getDaysTaken(), 1);
        assertEquals(d5.getTurn(), PlayerColor.RED);
        assertEquals(d4.getIndex(), 2);
        assertEquals(d4.getSection(), sect);
        assertEquals(d3.getTurn(), PlayerColor.RED);
        assertEquals(d3.getDaysTaken(), 2);
        assertArrayEquals(d3.getShipments(), new int[]{1,1,1,1});
        assertArrayEquals(d2.getShipments(), new int[]{0,1,1,1,1});
        assertEquals(d2.getTurn(), PlayerColor.RED);
        assertEquals(d1.getAwaiting(), new ArrayList<>(){{add(PlayerColor.RED);}});
        assertEquals(base.getID(), 1);
        assertEquals(base.getState(), "base");

        DummyCardStateVisitor d = new DummyCardStateVisitor();
        d11.showCardState(d);
        assertEquals(12, d.visited());
    }



}