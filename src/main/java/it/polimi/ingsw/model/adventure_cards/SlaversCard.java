package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.NegativeArgumentException;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.model.adventure_cards.state.CardState;
import it.polimi.ingsw.model.adventure_cards.state.SlaversAnnounceState;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class SlaversCard extends Card{

    private final int min_power;
    private final int crew_penalty;
    private final int credits;

    public SlaversCard(int id, int days, int min_power, int crew_penalty, int credits){
        super(id, days);
        if(credits<=0||crew_penalty<=0) throw new NegativeArgumentException();
        this.min_power = min_power;
        this.crew_penalty = crew_penalty;
        this.credits = credits;
    }

    @Override
    public CardState getState(VoyageState state){
        return new SlaversAnnounceState(state, this, state.getOrder(CardOrder.NORMAL));
    }

    public int getCredits(){
        return credits;
    }

    public int getCrewLost(){
        return this.crew_penalty;
    }

	public boolean apply(VoyageState state, Player p){
        if(state==null||p==null) throw new NullPointerException();
        if(p.getSpaceShip().getCannonPower()>this.min_power){
            this.exhaust();
            return true;
        }
        else if(p.getSpaceShip().getCannonPower()==this.min_power){
            return true;
        }
        if(p.getSpaceShip().getTotalCrew()<=this.crew_penalty){
            try {
                state.loseGame(p.getColor());
            } catch (PlayerNotFoundException e) {
                // Unreachable.
                e.printStackTrace();
            } return false;
        }
        return false;
	}

}

