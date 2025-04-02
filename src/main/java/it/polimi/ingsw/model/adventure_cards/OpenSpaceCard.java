package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.OpenSpaceState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class OpenSpaceCard extends Card {
    
    protected OpenSpaceCard(int id) {
        super(id, 0);
    }

    @Override
    public CardState getState(VoyageState state) {
        return new OpenSpaceState(state, this);
    }

    public void apply(VoyageState state, Player p) throws PlayerNotFoundException {
        if(state==null||p==null) throw new NullPointerException();
        if(p.getSpaceShip().getEnginePower()>0){
            state.getPlanche().movePlayer(p.getColor(), p.getSpaceShip().getEnginePower());
        }
        state.loseGame(p.getColor());
    }

}
