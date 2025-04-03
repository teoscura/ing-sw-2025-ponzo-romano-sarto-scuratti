package it.polimi.ingsw.model.client;

import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.player.PlayerColor;

public class ClientSpaceShip {
    
    private final GameModeType type;
    private final ClientComponent[][] ship;
    private final PlayerColor color;
    private final int cannon_power;
    private final int engine_power;
    private final int[] crew;

    public ClientSpaceShip(GameModeType type,
                           ClientComponent[][] ship,
                           PlayerColor color,
                           int cannon_power,
                           int engine_power, 
                           int[] crew){
        if(ship==null||cannon_power<0||engine_power<0||crew==null) throw new NullPointerException();
        if(crew.length!=3) throw new IllegalArgumentException();
        for(int c : crew){
            if(c<0) throw new IllegalArgumentException();
        }
        this.type = type;
        this.ship = ship;
        this.color = color;
        this.cannon_power = cannon_power;
        this.engine_power = engine_power;
        this.crew = crew;
    }

    public GameModeType getType() {
        return this.type;
    }
    
    public ClientComponent[][] getShip() {
        return this.ship;
    }

    public PlayerColor getColor() {
        return this.color;
    }

    public int getCannon_power() {
        return this.cannon_power;
    }

    public int getEngine_power() {
        return this.engine_power;
    }

    public int[] getCrew() {
        return this.crew;
    }
}
