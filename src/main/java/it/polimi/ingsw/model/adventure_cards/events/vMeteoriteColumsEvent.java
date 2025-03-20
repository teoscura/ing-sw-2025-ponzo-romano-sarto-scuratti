package it.polimi.ingsw.model.adventure_cards.events;

import it.polimi.ingsw.model.adventure_cards.Projectile;

//XXX stub.
public class vMeteoriteColumsEvent implements iCEvent {
    
    private Projectile[] projectiles;

    public vMeteoriteColumsEvent(Projectile[] projectiles){
        this.projectiles = projectiles;
    }

    public Projectile[] getProjectiles(){
        return this.projectiles;
    }
}
