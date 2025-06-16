package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class PieceImagePathProvider {
    
    //TODO

    static public String crew(AlienType type){
        switch(type){
            case BROWN:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            case HUMAN:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            case PURPLE:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            default:
                return "galaxy_trucker_imgs/piece/ph.jpg";
        }
    }

    static public String cargo(ShipmentType type){
        switch(type){
            case BLUE:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            case GREEN:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            case RED:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            case YELLOW:
                return "galaxy_trucker_imgs/piece/ph.jpg";
            default:
                return "galaxy_trucker_imgs/piece/ph.jpg";
        }
    }

    static public String battery(){
        return "galaxy_trucker_imgs/piece/ph.jpg";
    }  
}
