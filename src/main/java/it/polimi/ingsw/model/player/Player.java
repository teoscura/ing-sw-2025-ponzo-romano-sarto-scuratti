package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.exceptions.AlreadyPoweredException;
import it.polimi.ingsw.controller.server.ClientDescriptor;

public class Player {
    private final PlayerColor color;
    private boolean retired = false;
    private boolean disconnected = false;
    private int credits;
    private int score; 
    private iSpaceShip ship;
    private ClientDescriptor descriptor;

    public Player(GameModeType gamemode, PlayerColor color){
        this.color = color;
        ship = new SpaceShip(gamemode, this);
    }

    public PlayerColor getColor(){
        return this.color;
    }

    public void retire() {
        if(this.retired) throw new AlreadyPoweredException("Player has alredy retired.");
        this.retired = true;
    }

    public boolean getRetired() {
        return this.retired;
    }

    public void reconnect(){
        if(!this.disconnected) throw new AlreadyPoweredException("Player is alread y connected.");
        this.disconnected = false;
    }

    public void disconnect() {
        if(this.disconnected) throw new AlreadyPoweredException("Player has already disconnected.");
        this.disconnected = true;
    }

    public boolean getDisconnected() {
        return this.disconnected;
    }

	public int giveCredits(int amount){
		if(amount<=0) throw new IllegalArgumentException("Cannot earn negative credits.");
		this.credits+=amount;
		return this.credits;
	}

    public int getCredits(){
        return this.credits;
    }

    public void addScore(int rel_change){
        this.score += rel_change;
    }

    public int getScore(){
        return this.score;
    }

    public void finalScore() {
        for(ShipmentType t : ShipmentType.values()){
            if(t.getValue()<=0) continue;
            this.score += this.getSpaceShip().getContains()[t.getValue()-1] * t.getValue();
        }
    }

    public void reconnect(ClientDescriptor new_descriptor){
        this.bindDescriptor(new_descriptor);
        this.disconnected = false;
    }

    public iSpaceShip getSpaceShip(){
        return this.ship;
    }

    public void bindDescriptor(ClientDescriptor descriptor){
        this.descriptor = descriptor;
    }

    public ClientDescriptor getDescriptor(){
        return this.descriptor;
    }
}