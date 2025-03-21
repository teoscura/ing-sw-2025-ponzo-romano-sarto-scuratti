//Done.
package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.model.adventure_cards.utils.DaysCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iCardResponse;
import it.polimi.ingsw.model.adventure_cards.utils.iPlayerResponse;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EpidemicCard extends Card{
    
    public EpidemicCard(int id){
        super(id,0);
    }

    @Override
    public iCardResponse apply(iSpaceShip ship, iPlayerResponse response){
        ArrayList<ShipCoords> ill_cabins = ship.findConnectedCabins();
        CrewRemoveVisitor v = new CrewRemoveVisitor();
        for(ShipCoords s : ill_cabins){
            ship.getComponent(s).check(v);
        }
        return new DaysCardResponse(0);
    }

}
