//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.adventure_cards.exceptions.CardAlreadyExhaustedException;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.player.iSpaceShip;
    
public abstract class Card implements iCard {
    
    private int id;
    protected int days;
    private boolean exhausted;

    protected Card(int id, int days){
        if(days<0) throw new IllegalArgumentException("Negative arguments not allowed.");
        this.id = id;
        this.days = days;
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public boolean multiPhase(){
        return false;
    }

    @Override
    public void nextPhase(){
        return;
    }

    @Override
    public boolean hasMultipleRequirements(){
        return false;
    }

    @Override
    public boolean getExhausted(){
        return this.exhausted;
    }

    protected void exhaust(){
        if(this.exhausted) throw new CardAlreadyExhaustedException("This card's effect was already exhausted.");
    }

    @Override
    public abstract iCardResponse apply(iSpaceShip ship, iPlayerResponse response);
}
