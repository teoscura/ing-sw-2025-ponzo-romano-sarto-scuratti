//Done.
package it.polimi.ingsw.model.adventure_cards.responses;

public interface iCardResponse {
    public int getDays();
    public int[] getMerch();
    public int getStaffChange();
    public int getCredits();
    public int getRequiredMerch();
    public default boolean hasShot(){
        return false;
    }
}
