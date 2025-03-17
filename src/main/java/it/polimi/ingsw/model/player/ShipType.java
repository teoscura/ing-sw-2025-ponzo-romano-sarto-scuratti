package it.polimi.ingsw.model.player;

public enum ShipType{
	LVL1 (1, 5, 7),
	LVL2 (2, 5, 7);

	private int level;
	private int height;
	private int width;

	ShipType(int level, int height, int width){
		this.level = level;
		this.height = height;
		this.width = width;
	}

	public int getLevel(){
		return this.level;
	}

	public int getHeight(){
		return this.height;
	}

	public int getWidth(){
		return this.width;
	}

	//TODO change to use ShipCoords
	public int[] getShape(){
		if(this.level==1){
			return new int[]{0, 1, 2, 4, 5, 6, 7, 8 , 12, 13, 14, 20, 21, 27, 28, 31, 34};
		}
		else{
			return new int[]{0, 1, 3, 5, 6, 7, 13, 31};
		}
	}

    public ShipCoords getCenterCabin(){
        if(this.level==1 || this.level==2){
            return new ShipCoords(this,4,3);
        }
        else{
            return new ShipCoords(this,4,3);
        }
    }
}