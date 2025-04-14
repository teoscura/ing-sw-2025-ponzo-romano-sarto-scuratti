package it.polimi.ingsw.model.client.player;

import it.polimi.ingsw.model.player.PlayerColor;

public class ClientEndgamePlayer {
    
    private final String username;
    private final PlayerColor color;
    private final int planche_slot;
    private final int credits;
    private final int[] shipments;
    private final int score; 

    public ClientEndgamePlayer(String username, PlayerColor color, int planche_slot, int credits, int[] shipments, int score){
        if(username == null || shipments==null || color == PlayerColor.NONE) throw new NullPointerException();
        if(shipments.length!=4) throw new IllegalArgumentException(); 
        this.username = username;
        this.color = color;
        this.planche_slot = planche_slot;
        this.credits = credits;
        this.shipments = shipments;
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public int getPlanche_slot() {
        return this.planche_slot;
    }

    public int getCredits() {
        return this.credits;
    }

    public int[] getShipments() {
        return this.shipments;
    }

    public int getScore() {
        return this.score;
    }

}
