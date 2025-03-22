package it.polimi.ingsw.model.adventure_cards.utils;

import it.polimi.ingsw.model.components.enums.ShipmentType;

public class SmugglerCardRewardResponse implements iCardResponse {
    
    private final int[] contains;
    private final int days;

    public SmugglerCardRewardResponse(Planet planet){
        this.days = planet.getDays();
        this.contains = new int[4];
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()==0) continue;
            this.contains[t.getValue()-1] = planet.getQuantity(t);
        }
    }

    @Override
    public int getDays() {
        return this.days;
    }

    @Override
    public int[] getMerch() {
        return this.contains;
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
