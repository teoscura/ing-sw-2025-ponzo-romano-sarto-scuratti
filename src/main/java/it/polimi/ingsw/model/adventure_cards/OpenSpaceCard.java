package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.OpenSpaceState;
import it.polimi.ingsw.model.player.Player;

public class OpenSpaceCard extends Card {
    
    protected OpenSpaceCard(int id) {
        super(id, 0);
    }

    @Override
    public CardState getState(ModelInstance model) {
        return new OpenSpaceState(model, this);
    }

    public void apply(ModelInstance model, Player p) throws PlayerNotFoundException {
        if(model==null||p==null) throw new NullPointerException();
        if(p.getSpaceShip().getEnginePower()>0){
            model.getPlanche().movePlayer(p.getColor(), p.getSpaceShip().getEnginePower());
        }
        model.loseGame(p.getColor());
    }

}
