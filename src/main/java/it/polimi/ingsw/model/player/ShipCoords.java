package it.polimi.ingsw.model.player;

public class ShipCoords {
    private ShipType type = ShipType.LVL2;
    public int x;
    public int y;

    public ShipCoords(ShipType type, int x, int y){
        if(x<0 || y<0 || x>=type.getWidth() || y>=type.getWidth());
        this.type = type;
        this.x = x; 
        this.y = y;
    }

    public ShipCoords up(){
        if(this.y==0) return new ShipCoords(this.type, 0, 0);
        return new ShipCoords(this.type, this.x, this.y-1);
    }

    public ShipCoords down(){
        if(this.y==this.type.getHeight()-1) return new ShipCoords(this.type, 0, 0);
        return new ShipCoords(this.type, this.x, this.y+1);
    }

    public ShipCoords right(){
        if(this.x==this.type.getWidth()-1) return new ShipCoords(this.type, 0, 0);
        return new ShipCoords(this.type, this.x+1, this.y);
    }

    public ShipCoords left(){
        if(this.x==0) return new ShipCoords(this.type, 0, 0);
        return new ShipCoords(this.type, this.x-1, this.y);
    }
}
