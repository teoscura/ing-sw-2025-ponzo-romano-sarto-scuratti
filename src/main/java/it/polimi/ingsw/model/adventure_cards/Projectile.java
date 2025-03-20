//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDimension;
import it.polimi.ingsw.model.adventure_cards.enums.ProjectileDirection;
import it.polimi.ingsw.model.player.ShipType;

public class Projectile {

    private ProjectileDirection direction;
    private ProjectileDimension dimension;
    private ShipType type;
    private int offset = -1; 

    public Projectile(ShipType type, ProjectileDirection direction, ProjectileDimension dimension){
        this.type = type;
        this.direction = direction;
        this.dimension = dimension;
    }

    public Projectile(ShipType type, ProjectileDirection direction, ProjectileDimension dimension, int offset){
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

    public ShipType getType(){
        return this.type;
    }

    public int getOffset(){
        return this.offset;
    }
} 




