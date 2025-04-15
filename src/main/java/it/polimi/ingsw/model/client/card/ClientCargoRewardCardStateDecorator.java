package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientCargoRewardCardStateDecorator implements ClientCardState {
    
    private final ClientCardState base;

    private final PlayerColor turn;
    private final int days_taken;
    private final int[] shipments;

    public ClientCargoRewardCardStateDecorator(ClientCardState base, PlayerColor turn, int days_taken, int[] shipments){
        if(base == null || shipments == null || shipments.length != 4 || turn == PlayerColor.NONE || days_taken < 0) throw new NullPointerException();
        for(int t : shipments){
            if(t<0) throw new IllegalArgumentException();
        }
        this.base = base;
        this.turn = turn;
        this.days_taken = days_taken;
        this.shipments = shipments;
    }

    public PlayerColor getTurn(){
        return this.turn;
    }

    public int getDaysTaken(){
        return this.days_taken;
    }

    public int[] getShipments(){
        return this.shipments;
    }

    @Override
    public void showCardState(ClientView view){
        base.showCardState(view);
        view.show(this);
    }

}
