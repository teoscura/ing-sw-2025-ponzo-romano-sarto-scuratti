package it.polimi.ingsw.message.client;

import it.polimi.ingsw.model.adventure_cards.utils.Projectile;

public class MeteorMessage extends ClientMessage {
    
    private final Projectile meteorite;

    public MeteorMessage(Projectile meteorite){
        if(meteorite==null) throw new NullPointerException();
        if(meteorite.getOffset()==-1) throw new IllegalArgumentException("Projectile must be initialized");
        this.meteorite = meteorite;
    }
}
