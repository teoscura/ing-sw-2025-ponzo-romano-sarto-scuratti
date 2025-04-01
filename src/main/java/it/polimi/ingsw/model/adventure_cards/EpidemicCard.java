//Done.
package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.EpidemicState;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;

public class EpidemicCard extends Card{
    
    public EpidemicCard(int id){
        super(id,0);
    }

    @Override
    public CardState getState(ModelInstance model){
        return new EpidemicState(model, this);
    }


    public void apply(ModelInstance model, Player p) throws PlayerNotFoundException{
        if(model==null||p==null) throw new NullPointerException();
        ArrayList<ShipCoords> ill_cabins = p.getSpaceShip().findConnectedCabins();
        CrewRemoveVisitor v = new CrewRemoveVisitor(p.getSpaceShip());
        for(ShipCoords s : ill_cabins){
            p.getSpaceShip().getComponent(s).check(v);
        }
        p.getSpaceShip().updateShip();
        if(p.getSpaceShip().getTotalCrew()==0) {
            model.loseGame(p.getSpaceShip().getColor());
        }
    }

}
