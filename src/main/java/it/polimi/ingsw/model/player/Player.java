package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.controller.server.ClientDescriptor;

public class Player {
    private final PlayerColor color;
    private boolean retired = false;
    private boolean disconnected = false;
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

	public int giveCredits(int amount){
		if(amount<=0) throw new IllegalArgumentException("Cannot earn negative credits.");
		this.credits+=amount;
		return this.credits;
	}

    public int getCredits(){
        return this.credits;
    }

    public void reconnect(ClientDescriptor new_descriptor){
        this.bindDescriptor(new_descriptor);
        this.disconnected = false;
    }

    public void disconnect() {
        if( this.disconnected) throw new AlreadyPoweredException("Player has alredy disconnected.");
        this.disconnected = true;
    }

    public boolean getDisconnected() {
        return  this.disconnected;
    }

    public void retire() {
        if(this.retired) throw new AlreadyPoweredException("Player has alredy retired.");
        this.retired = true;
    }

    public boolean getRetired() {
        return this.retired;
    }

    public SpaceShip getSpaceShip(){
        return this.ship;
    }

    public void bindDescriptor(ClientDescriptor descriptor){
        this.descriptor = descriptor;
    }

    public ClientDescriptor getDescriptor(){
        return this.descriptor;
    }
}