package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.controller.server.ClientDescriptor;

public class Player {
    private final PlayerColor color;
    private boolean retired = false;
    private int credits;
    private SpaceShip ship;
    private ClientDescriptor descriptor;

    public Player(GameModeType gamemode, PlayerColor color){
        this.color = color;
        ship = new SpaceShip(gamemode, color);
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public int getCredits(){
        return this.credits;
    }

    public void retire() {
        if(retired) throw new AlreadyPoweredException("Player has alredy retired.");
        this.retired = true;
    }

    public boolean getRetired() {
        return this.retired;
    }

    public SpaceShip getSpaceShip(){
        return this.ship;
    }

    public void bindDescriptor(ClientDescriptor descriptor) throws Exception {
        this.descriptor = descriptor;
    }

    public ClientDescriptor getDescriptor(){
        return this.descriptor;
    }
}