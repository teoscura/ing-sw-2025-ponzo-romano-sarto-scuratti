//Done.
package it.polimi.ingsw.model.adventure_cards;

import java.util.ArrayList;

import it.polimi.ingsw.controller.server.state.GameState;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.client.ClientMessage;
import it.polimi.ingsw.message.client.GameLostMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.CardResponseType;
import it.polimi.ingsw.model.adventure_cards.utils.PlayerResponse;
import it.polimi.ingsw.model.adventure_cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.iSpaceShip;

public class EpidemicCard extends Card{
    
    public EpidemicCard(int id){
        super(id,0);
    }

    @Override
    public GameState getState() {
        return new NoResponseState();
    }

    @Override
    public void validateResponse(ServerMessage response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateResponse'");
    }
    
    private void apply(ModelInstance model, iSpaceShip ship, PlayerResponse response) throws PlayerNotFoundException{
        if(ship==null||response==null) throw new NullPointerException();
        this.after_response = CardResponseType.NONE;
        ArrayList<ShipCoords> ill_cabins = ship.findConnectedCabins();
        CrewRemoveVisitor v = new CrewRemoveVisitor(ship);
        for(ShipCoords s : ill_cabins){
            ship.getComponent(s).check(v);
        }
        ship.updateShip();
        if(ship.getTotalCrew()==0) {
            model.loseGame(ship.getColor());
            return new GameLostMessage();
        }
        return null;
    }

}
