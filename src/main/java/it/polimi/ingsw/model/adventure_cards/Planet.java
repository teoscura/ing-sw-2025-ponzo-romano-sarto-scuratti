package it.polimi.ingsw.model.adventure_cards;

public class Planet {
    private int red_material, blue_material, green_material, yellow_material;
    private boolean visited;

    public int getBlueMaterial(){
        return blue_material;
    }
    public int getGreenMaterial(){
        return green_material;
    }
    public int getYellowMaterial(){
        return yellow_material;
    }
    public int getRedMaterial(){
        return red_material;
    }
    public boolean isVisited(){
        return visited;
    } 
}
