//Done.
package it.polimi.ingsw.model.adventure_cards.responses;

import it.polimi.ingsw.exceptions.NegativeArgumentException;

public class SmugglerCardPenaltyResponse implements iCardResponse {

    private final int required_merch;

    public SmugglerCardPenaltyResponse(int required_merch){
        if(required_merch<=0) throw new NegativeArgumentException();
        this.required_merch = required_merch;
    }

    @Override
    public int getDays() {
        return 0;
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
        return 0;
    }

    @Override
    public int getRequiredMerch() {
        return this.required_merch;
    }

}
