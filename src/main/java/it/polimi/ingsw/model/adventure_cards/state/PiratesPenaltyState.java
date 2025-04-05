package it.polimi.ingsw.model.adventure_cards.state;

import java.util.List;

import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.adventure_cards.PiratesCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.state.VoyageState;

public class PiratesPenaltyState extends CardState {

    private final PiratesCard card;
    private final List<Player> list;
    private final ProjectileArray shots;
    private boolean responded = false;

    protected PiratesPenaltyState(VoyageState state, PiratesCard card, List<Player> list, ProjectileArray shots) {
        super(state);
        if(list.size()>this.state.getCount().getNumber()||list.size()<1||list==null) throw new IllegalArgumentException("Constructed insatisfyable state");
        if(card==null||shots==null) throw new NullPointerException();
        this.card = card;
        this.list = list;
        this.shots = shots;
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!responded) return;
        this.list.getFirst().getSpaceShip().handleShot(this.shots.getProjectiles().getFirst());
        this.transition();
    }

    @Override
    protected CardState getNext() {
        this.shots.getProjectiles().removeFirst();
        if(!this.list.getFirst().getSpaceShip().getBrokeCenter()) this.list.getFirst().getSpaceShip().verifyAndClean();
        else{
            return new PiratesNewCabinState(state, card, list, shots);
        }
        if(!this.shots.getProjectiles().isEmpty()) return new PiratesPenaltyState(state, card, list, shots);
        this.list.removeFirst();
        if(!this.list.isEmpty()) return new PiratesAnnounceState(state, card, list);
        return null;
    }

}
