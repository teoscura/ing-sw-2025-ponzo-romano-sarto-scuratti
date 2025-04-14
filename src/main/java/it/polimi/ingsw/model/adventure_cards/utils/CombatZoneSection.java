package it.polimi.ingsw.model.adventure_cards.utils;

public class CombatZoneSection {
    private final CombatZoneCriteria criteria;
    private final CombatZonePenalty penalty;
    private final int amount;

    public CombatZoneSection(CombatZoneCriteria criteria, CombatZonePenalty penalty){
        this.criteria = criteria;
        this.penalty = penalty;
        this.amount = -1;
    }
    
    public CombatZoneSection(CombatZoneCriteria criteria, CombatZonePenalty penalty, int amount){
        this.criteria = criteria;
        this.penalty = penalty;
        this.amount = amount;
    }

    public CombatZoneCriteria getCriteria(){
        return this.criteria;
    }

    public CombatZonePenalty getPenalty(){
        return this.penalty;
    }

    public int getAmount(){
        return this.amount;
    }
}
