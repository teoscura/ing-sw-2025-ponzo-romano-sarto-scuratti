package it.polimi.ingsw.model.client.card;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientAwaitConfirmCardStateDecorator implements ClientCardState {
    
    private final ClientCardState base;
    
    private final ArrayList<PlayerColor> awaiting;

    public ClientAwaitConfirmCardStateDecorator(ClientCardState base, ArrayList<PlayerColor> awaiting){
        if (base == null || awaiting == null) throw new NullPointerException();
        this.base = base;
        this.awaiting = awaiting;
    }
    
    public ArrayList<PlayerColor> getAwaiting(){
        return this.awaiting;
    }

    @Override
    public void showCardState(ClientView view){
        base.showCardState(view);
        view.show(this);
    }
}