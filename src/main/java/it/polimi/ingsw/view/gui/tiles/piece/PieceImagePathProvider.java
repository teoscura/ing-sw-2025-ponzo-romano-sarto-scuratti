package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ShipmentType;

public class PieceImagePathProvider {
    
    static public String crew(AlienType type){
        switch(type){
            case BROWN:
                return "galaxy_trucker_imgs/piece/human.jpg";
            case HUMAN:
                return "galaxy_trucker_imgs/piece/brown.jpg";
            case PURPLE:
                return "galaxy_trucker_imgs/piece/purple.jpg";
            default:
                return "galaxy_trucker_imgs/empty.jpg";
        }
    }

    static public String cargo(ShipmentType type){
        switch(type){
            case BLUE:
                return "galaxy_trucker_imgs/piece/cargo.jpg";
            case GREEN:
                return "galaxy_trucker_imgs/piece/green.jpg";
            case RED:
                return "galaxy_trucker_imgs/piece/red.jpg";
            case YELLOW:
                return "galaxy_trucker_imgs/piece/yellow.jpg";
            default:
                return "galaxy_trucker_imgs/piece/empty.jpg";
        }
    }

    static public String battery(){
        return "galaxy_trucker_imgs/piece/battery.jpg";
    }  
}
