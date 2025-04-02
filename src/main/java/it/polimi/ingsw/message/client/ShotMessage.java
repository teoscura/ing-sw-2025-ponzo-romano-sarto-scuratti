package it.polimi.ingsw.message.client;

import it.polimi.ingsw.model.adventure_cards.utils.Projectile;

public class ShotMessage extends ClientMessage {

    private final Projectile shot;

    public ShotMessage(Projectile shot){
        if(shot==null) throw new NullPointerException();
        if(shot.getOffset()==-1) throw new IllegalArgumentException("Projectile must be initialized");
        this.shot = shot;
    }
    
}
