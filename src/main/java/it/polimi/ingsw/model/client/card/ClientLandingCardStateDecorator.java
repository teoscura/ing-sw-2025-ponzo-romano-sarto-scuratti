package it.polimi.ingsw.model.client.card;

import java.util.List;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientLandingCardStateDecorator implements ClientCardState {
    
    private final ClientCardState base;

    private final PlayerColor turn;
    private final int days_taken;
    private final int crew_needed;
    private final List<Boolean> available;
    
    public ClientLandingCardStateDecorator(ClientCardState base, PlayerColor turn, int days_taken, int crew_needed, List<Boolean> available){
        if(base==null || available == null) throw new NullPointerException();
        if(days_taken<=0 || crew_needed < 0) throw new IllegalArgumentException();
        this.base = base;
        this.turn = turn;
        this.days_taken = days_taken;
        this.crew_needed = crew_needed;
        this.available = available;
    }

    public PlayerColor getTurn(){
        return this.turn;
    }

    public int getDaysTaken(){
        return this.days_taken;
    }

    public List<Boolean> getAvailable(){
        return this.available;
    }

    @Override
    public void showCardState(ClientView view){
        base.showCardState(view);
        view.show(this);
    }

}

