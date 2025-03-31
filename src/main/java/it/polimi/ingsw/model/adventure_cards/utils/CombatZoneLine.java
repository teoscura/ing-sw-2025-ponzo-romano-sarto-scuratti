package it.polimi.ingsw.model.adventure_cards.utils;

public enum CombatZoneLine {
    LEAST_CANNON (0),
    LEAST_ENGINE (1),
    LEAST_CREW (2);

    private int number;

    CombatZoneLine(int number){
        this.number = number;
    }

    public int getNumber(){
        return this.number;
    }
}
