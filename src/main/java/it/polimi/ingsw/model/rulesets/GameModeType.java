package it.polimi.ingsw.model.rulesets;

import it.polimi.ingsw.model.player.ShipCoords;

public enum GameModeType{
	TEST (1, false,5, 7, 18, 4, 10, 5, 9),
	LVL2 (2, true, 5, 7, 24, 4, 10, 5, 9);

	private boolean lifesupports;
	private int level;
	private int height;
	private int width;
	private int length;
	private int min_x;
	private int max_x;
	private int min_y;
	private int max_y;	

	GameModeType(int level, boolean lifesupports, int height, int width, int length, int min_x, int max_x, int min_y, int max_y){
		this.level = level;
		this.lifesupports = lifesupports;
		this.height = height;
		this.width = width;
		this.length = length;
		this.min_x = min_x;
		this.max_x = max_x;
		this.min_y = min_y;
		this.max_y = max_y;
	}

	public int getLevel(){
		return this.level;
	}

	public boolean getLifeSupport(){
		return this.lifesupports;
	}

	public int getHeight(){
		return this.height;
	}

	public int getWidth(){
		return this.width;
	}

	public int getLength(){
		return this.length;
	}

	public int getMinX(){
		return this.min_x;
	}
	
	public int getMaxX(){
		return this.max_x;
	}	

	public int getMinY(){
		return this.min_y;
	}

	public int getMaxY(){
		return this.max_y;
	}

	private int[] getShape(){
		if(this.level==1){
			return new int[]{0, 1, 2, 4, 5, 6, 7, 8 , 12, 13, 14, 20, 21, 27, 28, 31, 34};
		}
		else{
			return new int[]{0, 1, 3, 5, 6, 7, 13, 31};
		}
	}

    public ShipCoords getCenterCabin(){
        if(this.level==1 || this.level==2){
            return new ShipCoords(this,3,2);
        }
        else{
            return new ShipCoords(this,3,2);
        }
    }

	public boolean isForbidden(ShipCoords coords){
		int tmp = coords.y*this.width+coords.x;
		for(int i : this.getShape()){
			if(i==tmp) return true;
		}
		return false;
	}

    public int getLenght() {
        return this.length;
    }
}