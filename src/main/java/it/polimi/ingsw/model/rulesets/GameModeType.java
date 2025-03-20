package it.polimi.ingsw.model.rulesets;

import it.polimi.ingsw.model.player.ShipCoords;

public enum GameModeType{
	TEST (1, 5, 7, 18),
	LVL2 (2, 5, 7, 24);

	private int level;
	private int height;
	private int width;
	private int length;

	GameModeType(int level, int height, int width, int length){
		this.level = level;
		this.height = height;
		this.width = width;
		this.length = length;
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

	public int getLength(){
		return this.length;
	}

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

    int getLenght() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLenght'");
    }
}