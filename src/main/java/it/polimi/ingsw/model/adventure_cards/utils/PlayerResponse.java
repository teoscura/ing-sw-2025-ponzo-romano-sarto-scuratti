//Done.
package it.polimi.ingsw.model.adventure_cards.utils;

import it.polimi.ingsw.model.adventure_cards.exceptions.ResponseFieldUnsetException;
import it.polimi.ingsw.model.player.ShipCoords;

public class PlayerResponse{

    private ShipCoords[] coords = null;
    private int[] merch_choices = null;
    private int id = -2;
    private boolean accept = false;

    public PlayerResponse(int id){
        if(id<=-2) throw new ResponseFieldUnsetException("Id has been set to illegal value.");
        this.id = id;
    }

    public PlayerResponse(ShipCoords[] coords, boolean accept){
        if(coords == null) throw new NullPointerException();
        if(id<=-2) throw new ResponseFieldUnsetException("Id has been set to illegal value.");
        this.coords = coords;
        this.accept = accept;
    }

    public PlayerResponse(ShipCoords[] coords, int[] merch_choices, boolean accept){
        if(coords == null || merch_choices == null) throw new NullPointerException();
        this.coords = coords;
        this.merch_choices = merch_choices;
        this.accept = accept;
    }
    
    public PlayerResponse(ShipCoords[] coords, int[] merch_choices, int id, boolean accept){
        if(coords == null || merch_choices == null) throw new NullPointerException();
        if(id<=-2) throw new ResponseFieldUnsetException("Id has been set to illegal value.");
        this.coords = coords;
        this.merch_choices = merch_choices;
        this.id = id;
        this.accept = accept;
    }

    public ShipCoords[] getCoordArray(){
        if(this.coords == null) throw new ResponseFieldUnsetException("Response doesn't have the coordinates.");
        return this.coords;
    }

    public int[] getMerchChoices(){
        if(this.merch_choices == null) throw new ResponseFieldUnsetException("Response doesn't have the merch choices.");
        return this.merch_choices;
    }

    public int getId(){
        if(id==-2) throw new ResponseFieldUnsetException("Response doesn't have an id.");
        return this.id;
    }

    public boolean getAccept(){
        return this.accept;
    }

}
