//Done.
package it.polimi.ingsw.model.adventure_cards.responses;

public class DaysCardResponse implements iCardResponse{
    
    private final int days;

    public DaysCardResponse(int days){
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
        return 0;
    }

    @Override
    public int getRequiredMerch() {
        return 0;
    }
}
