package it.polimi.ingsw.model.adventure_cards.utils;

public class CombatZoneLine {
    private final CombatZoneCriteria criteria;
    private final iCardResponse response;

    public CombatZoneLine(CombatZoneCriteria criteria, iCardResponse response){
        if(criteria.getId()<0) throw new IllegalArgumentException("Cannot have 'NONE' as a criteria.");
        if(response==null) throw new NullPointerException();
        this.criteria = criteria;
        this.response = response;
    }   

    public CombatZoneCriteria getCriteria(){
        return this.criteria;
    }

    public iCardResponse getResponse(){
        return this.response;
    }
}
