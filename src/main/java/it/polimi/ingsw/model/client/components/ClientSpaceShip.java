package it.polimi.ingsw.model.client.components;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.VerifyResult;

public class ClientSpaceShip {
    
    private final GameModeType type;
    private final ClientComponent[][] ship;
    private final boolean[] shielded;
    private final int cannon_power;
    private final int engine_power;
    private final int energy_power;
    private final int[] crew;

    public ClientSpaceShip(GameModeType type,
                           ClientComponent[][] ship,
                           boolean[] shielded,
                           int cannon_power,
                           int engine_power, 
                           int energy_power,
                           int[] crew){
        if(ship==null||shielded==null||crew==null) throw new NullPointerException();
        if(crew.length!=3||shielded.length!=4||cannon_power<0||engine_power<0||energy_power<0) throw new IllegalArgumentException();
        for(int c : crew){
            if(c<0) throw new IllegalArgumentException();
        }
        this.type = type;
        this.ship = ship;
        this.shielded = shielded;
        this.cannon_power = cannon_power;
        this.engine_power = engine_power;
        this.energy_power = energy_power;
        this.crew = crew;
    }

    public ClientSpaceShip getVerifyShip(VerifyResult[][] results){
        if(results == null) throw new NullPointerException();
        if(results.length != this.type.getHeight()) throw new IllegalArgumentException();
        if(results[0].length != this.type.getWidth()) throw new IllegalArgumentException();
        ClientComponent[][] tmp = new ClientComponent[this.type.getHeight()][this.type.getWidth()];
        for(int i = 0; i < this.type.getHeight(); i++){
            for(int j=0;j < this.type.getWidth(); j++){
                if(results[i][j]==VerifyResult.BROKEN) tmp[i][j] = new ClientBrokenVerifyComponentDecorator(this.ship[i][j]);
                else tmp[i][j] = this.ship[i][j];
            }
        }
        return new ClientSpaceShip(type, tmp, shielded, cannon_power, engine_power, energy_power, crew);
    }

    public GameModeType getType() {
        return this.type;
    }
    
    public ClientComponent getComponent(ShipCoords coords){
        if(coords.getType()!=this.type) throw new IllegalArgumentException();
        return this.ship[coords.y][coords.x];
    }

    public boolean[] getShielded(){
        return this.shielded;
    }

    public int getCannonPower() {
        return this.cannon_power;
    }

    public int getEnginePower() {
        return this.engine_power;
    }

    public int getEnergyPower() {
        return this.energy_power;
    }

    public int[] getCrew() {
        return this.crew;
    }
}
