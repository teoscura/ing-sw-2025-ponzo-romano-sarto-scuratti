//Done.
package it.polimi.ingsw.model.adventure_cards.responses;

import it.polimi.ingsw.exceptions.NegativeArgumentException;

public class PirateCardReward implements iCardResponse {

    private final int credits;
    private final int days;

    public PirateCardReward(int credits, int days){
        if(credits<=0) throw new NegativeArgumentException("Credits cannot be negative.");
        this.credits = credits;
        this.days = days;
    }

    @Override
    public int getDays() {
        return this.days;
    }

    @Override
    public int[] getMerch() {
        return new int[]{0,0,0,0};
    }

    @Override
    public int getStaffChange() {
        return 0;
    }

    @Override
    public int getCredits() {
        return this.credits;
    }

    @Override
    public int getRequiredMerch() {
        return 0;
    }
    
}
