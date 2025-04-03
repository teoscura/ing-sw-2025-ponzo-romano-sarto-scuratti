package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.player.PlayerColor;

public class ClientPlayer {
    private final String username;
    private final PlayerColor color;
    private final int planche_slot;
    private final int credits;
    private final int crew;
    private final int[] power;
    private final boolean disconnected;
    private final boolean retired_lost;

    public ClientPlayer(String username, PlayerColor color, int planche_slot, int credits,
                        int crew, int[] power, boolean disconnected, boolean retired_lost){
        if(username==null||power==null) throw new NullPointerException();
        if(power.length!=3) throw new IllegalArgumentException();
        for(int i: power){
            if(i<0) throw new IllegalArgumentException();
        }
        if(planche_slot<0||crew<0||credits<0) throw new IllegalArgumentException();
        this.username = username;
        this.color = color;
        this.planche_slot = planche_slot;
        this.credits = credits;
        this.crew = crew;
        this.power = power;
        this.disconnected = disconnected;
        this.retired_lost = retired_lost;
    }

    public String getUsername(){
        return this.username;
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public int getPlancheSlot(){
        return this.planche_slot;
    }

    public int getCredits(){
        return this.credits;
    }

    public int getCrew(){
        return this.crew;
    }

    public int[] getPower(){
        return this.power;
    }

    public boolean getDisconnected(){
        return this.disconnected;
    }

    public boolean getRetired(){
        return this.retired_lost;
    }
}
