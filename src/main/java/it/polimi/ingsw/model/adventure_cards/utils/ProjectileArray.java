//Done.
package it.polimi.ingsw.model.adventure_cards.utils;

import java.util.concurrent.ThreadLocalRandom;

public class ProjectileArray {
    
    private final Projectile[] projectiles;

    public ProjectileArray(Projectile[] projectiles){
        if(projectiles==null || projectiles.length==0) throw new NullPointerException("Meteorites Array is empty or null");
        int max = 12;
        for(int i = 0; i<projectiles.length; i++){
            int value = ThreadLocalRandom.current().nextInt(1 , max+1);
            projectiles[i] = new Projectile(projectiles[i].getDirection(), 
                                            projectiles[i].getDimension(), 
                                            value);
        }
        this.projectiles = projectiles;
    }

    public Projectile[] getProjectiles(){
        return this.projectiles.clone();
    }
}
