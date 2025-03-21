package it.polimi.ingsw.model.adventure_cards.utils;

public class BrokenCenterCabinResponse implements iCardResponse {

    private final int staff_change;

    public BrokenCenterCabinResponse(){
        this.staff_change = 0;
    }

    public BrokenCenterCabinResponse(int staff_change){
        this.staff_change = staff_change;
    }

    @Override
    public int getDays() {
        return 0;
    }

    @Override
    public int[] getMerch() {
        return null;
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
