package it.polimi.ingsw.view.gui.tiles.piece;

import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ShipmentType;

/**
 * Utility class used to retrieve the paths for the images representing each used piece.
 */
public class PieceImagePathProvider {

	static public String destroy() {
		return "galaxy_trucker_imgs/piece/destroy.png";
	}

	static public String blob() {
		return "galaxy_trucker_imgs/piece/pointing.png";
	}

	static public String crew(AlienType type) {
		switch (type) {
			case BROWN:
				return "galaxy_trucker_imgs/piece/brown.png";
			case PURPLE:
				return "galaxy_trucker_imgs/piece/purple.png";
			default:
				return "galaxy_trucker_imgs/piece/human.png";
		}
	}

	static public String cargo(ShipmentType type) {
		switch (type) {
			case BLUE:
				return "galaxy_trucker_imgs/piece/blue_cargo.png";
			case GREEN:
				return "galaxy_trucker_imgs/piece/green_cargo.png";
			case RED:
				return "galaxy_trucker_imgs/piece/red_cargo.png";
			case YELLOW:
				return "galaxy_trucker_imgs/piece/yellow_cargo.png";
			default:
				return "galaxy_trucker_imgs/piece/red_cargo.png";
		}
	}

	static public String battery() {
		return "galaxy_trucker_imgs/piece/battery.png";
	}
}
