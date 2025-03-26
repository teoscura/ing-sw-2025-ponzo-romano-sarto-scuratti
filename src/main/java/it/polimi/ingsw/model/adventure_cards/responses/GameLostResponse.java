package it.polimi.ingsw.model.adventure_cards.responses;

public class GameLostResponse implements iCardResponse {

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
