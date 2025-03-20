//Done.
package it.polimi.ingsw.model.adventure_cards.utils;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDimension;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDirection;
import it.polimi.ingsw.model.rulesets.GameModeType;

public class Projectile {

    private ProjectileDirection direction;
    private ProjectileDimension dimension;
    private GameModeType type;
    private int offset = -1; 

    public Projectile(GameModeType type, ProjectileDirection direction, ProjectileDimension dimension){
        this.type = type;
        this.direction = direction;
        this.dimension = dimension;
    }

    public Projectile(GameModeType type, ProjectileDirection direction, ProjectileDimension dimension, int offset){
        if(offset<0 || 
          (offset>=type.getHeight()&&(direction.getShift()-1)%2==0) || 
          (offset>=type.getWidth()&&(direction.getShift())%2==0))
            throw new OutOfBoundsException("Offset goes over ship bounds.");
        this.type = type;
        this.offset = offset;
        this.direction = direction;
        this.dimension = dimension;
    }

    public ProjectileDirection getDirection(){
        return this.direction;
    }

    public ProjectileDimension getDimension(){
        return this.dimension;
    }

    public GameModeType getType(){
        return this.type;
    }

    public int getOffset(){
        return this.offset;
    }
} 




