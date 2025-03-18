package it.polimi.ingsw.model.adventure_cards;

import java.io.Serializable;

import it.polimi.ingsw.model.player.ShipCoords;

public class PlayerResponse implements iPlayerResponse, Serializable{
    
    private ShipCoords[] coords;
    private int[] merch_choices;
    private int id;
    private boolean accept;

    public PlayerResponse(ShipCoords[] coords, int[] merch_choices, int id, boolean accept){
        this.coords = coords;
        this.merch_choices = merch_choices;
        this.id = id;
        this.accept = accept;
    }

    public ShipCoords[] getCoordArray(){
        return this.coords;
    }

    public int[] getMerchChoices(){
        return this.merch_choices;
    }

    public int getId(){
        return this.id;
    }

    public boolean getAccept(){
        return this.accept;
    }
}
