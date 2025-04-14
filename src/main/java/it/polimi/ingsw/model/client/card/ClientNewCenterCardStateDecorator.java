package it.polimi.ingsw.model.client.card;

import java.util.List;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;

public class ClientNewCenterCardStateDecorator implements ClientCardState {
    
    private final ClientCardState base;

    private final List<PlayerColor> awaiting;

    public ClientNewCenterCardStateDecorator(ClientCardState base, List<PlayerColor> awaiting) {
        if (base == null || awaiting == null) throw new NullPointerException();
        this.base = base;
        this.awaiting = awaiting;
    }

    public List<PlayerColor> getAwaiting(){
        return awaiting;
    }

    @Override
    public void showCardState(ClientView view){
        base.showCardState(view);
        view.show(this);
    }
}