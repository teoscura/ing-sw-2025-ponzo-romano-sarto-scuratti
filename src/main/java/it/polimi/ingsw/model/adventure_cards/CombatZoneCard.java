package it.polimi.ingsw.model.adventure_cards;

import java.util.List;

import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.CombatZoneAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.*;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneCard extends Card{

    private final List<CombatZoneSection> sections;
    private final ProjectileArray shots;

    public CombatZoneCard(int id, List<CombatZoneSection> sections, ProjectileArray shots){
        super(id,0);
        if(shots==null||sections==null) throw new NullPointerException();
        this.sections = sections;
        this.shots = shots;
    } 

    @Override
    public CardState getState(VoyageState state){
        return new CombatZoneAnnounceState(state, this.getId(), sections, shots);
    }

}
