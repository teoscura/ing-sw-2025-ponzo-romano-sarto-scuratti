package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class PieceImagePathProvider {
    
    //TODO

    static public String crew(AlienType type){
        switch(type){
            case BROWN:
                return "galaxy_trucker_imgs/piece/brown.png";
            case PURPLE:
                return "galaxy_trucker_imgs/piece/purple.png";
            default:
                return "galaxy_trucker_imgs/piece/human.png";
        }
    }

    static public String cargo(ShipmentType type){
        switch(type){
            case BLUE:
                return "galaxy_trucker_imgs/piece/blue.png";
            case GREEN:
                return "galaxy_trucker_imgs/piece/green.png";
            case RED:
                return "galaxy_trucker_imgs/piece/red.png";
            case YELLOW:
                return "galaxy_trucker_imgs/piece/yellow.png";
            default:
                return "galaxy_trucker_imgs/piece/red.png";
        }
    }

    static public String battery(){
        return "galaxy_trucker_imgs/piece/battery.png";
    }  
}
