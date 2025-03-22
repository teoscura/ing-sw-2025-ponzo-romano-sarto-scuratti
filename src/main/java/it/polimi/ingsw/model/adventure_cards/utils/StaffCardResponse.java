package it.polimi.ingsw.model.adventure_cards.utils;

public class StaffCardResponse implements iCardResponse {

    private final int staff_change;

    public StaffCardResponse(int staff_change){
        this.staff_change = staff_change;
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
        return this.staff_change;
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
