//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.events.iCEvent;
import it.polimi.ingsw.model.adventure_cards.exceptions.CardAlreadyExhaustedException;
import it.polimi.ingsw.model.player.iSpaceShip;

public abstract class Card implements iCard {
    
    private int id;
    private boolean exhausted;

    protected Card(int id){
        this.id = id;
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public boolean getExhausted(){
        return this.exhausted;
    }

    protected void exhaust(){
        if(this.exhausted) throw new CardAlreadyExhaustedException("This card's effect was already exhausted.");
    }

    @Override
    public abstract int apply(iSpaceShip state, iPlayerResponse response);

    public abstract iCEvent setup(iSpaceShip state);

    //TODO: add general methods
}
