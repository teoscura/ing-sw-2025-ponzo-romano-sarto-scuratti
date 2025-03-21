package it.polimi.ingsw.model.adventure_cards.utils;

public class CombatZoneProjectilesResponse implements iCardResponse{

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
        return 0;
    }

    @Override
    public boolean hasShot(){
        return true;
    }
    
}
