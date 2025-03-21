package it.polimi.ingsw.model.adventure_cards.utils;

public enum CombatZoneCriteria {
    NONE(-1),
    POWER(0),
    ENGINE(1),
    CREW(2);

    private final int id;

    CombatZoneCriteria(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
