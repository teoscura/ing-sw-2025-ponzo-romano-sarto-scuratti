package it.polimi.ingsw.model.adventure_cards;

public class Projectile {

    private ProjectileDirection direction;
    private ProjectileDimension dimension; 

    public Projectile(ProjectileDirection direction, ProjectileDimension dimension){
        this.direction = direction;
        this.dimension = dimension;
    }

    public ProjectileDirection getDirection(){
        return this.direction;
    }

    public ProjectileDimension getDimension(){
        return this.dimension;
    }
} 

enum ProjectileDimension{
    
    BIG(false),
    SMALL(true);

    private boolean blockable;

    ProjectileDimension(boolean blockable){
        this.blockable = blockable;
    }

    public boolean getBlockable(){
        return this.blockable;
    }
}

//Rotazione relativa in gradi, parte da su e va in senso orario
enum ProjectileDirection {
    
    U000 (0), 
    U090 (1),
    U180 (2), 
    U270 (3);

    private int shift;

    ProjectileDirection(int shift){
        this.shift = shift;
    }

    public int getShift(){
        return this.shift;
    }

    public ProjectileDirection getOpposite(){
        switch(this.shift){
            case 0: return ProjectileDirection.U180;
            case 1: return ProjectileDirection.U270;
            case 2: return ProjectileDirection.U000;
            case 3: return ProjectileDirection.U090;
            default: return ProjectileDirection.U000;
        }
    }
}