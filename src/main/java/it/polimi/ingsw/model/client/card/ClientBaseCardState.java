package it.polimi.ingsw.model.client.card;

import it.polimi.ingsw.view.ClientView;

public class ClientBaseCardState implements ClientCardState {

    private final int id;

    public ClientBaseCardState(int id){
        if(id<1||id>120||(id>20&&id<100)) throw new IllegalArgumentException();
        this.id = id;
    }

    public int getID(){
        return this.id;
    }

    @Override
    public void showCardState(ClientView view){
        view.show(this);
    }
}
