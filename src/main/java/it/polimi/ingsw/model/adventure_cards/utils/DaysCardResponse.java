package it.polimi.ingsw.model.adventure_cards.utils;

public class DaysCardResponse implements iCardResponse{
    
    private int days;

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
}
