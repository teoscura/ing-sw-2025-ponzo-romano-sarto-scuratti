package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class PieceImagePathProvider {
    
    //TODO

    static public String crew(AlienType type){
        switch(type){
            case BROWN:
                return "galaxy_trucker_imgs/piece/ph.png";
            case HUMAN:
                return "galaxy_trucker_imgs/piece/ph.png";
            case PURPLE:
                return "galaxy_trucker_imgs/piece/ph.png";
            default:
                return "galaxy_trucker_imgs/piece/ph.png";
        }
    }

    static public String cargo(ShipmentType type){
        switch(type){
            case BLUE:
                return "galaxy_trucker_imgs/piece/ph.png";
            case GREEN:
                return "galaxy_trucker_imgs/piece/ph.png";
            case RED:
                return "galaxy_trucker_imgs/piece/ph.png";
            case YELLOW:
                return "galaxy_trucker_imgs/piece/ph.png";
            default:
                return "galaxy_trucker_imgs/piece/ph.png";
        }
    }

    static public String battery(){
        return "galaxy_trucker_imgs/piece/ph.png";
    }  
}
