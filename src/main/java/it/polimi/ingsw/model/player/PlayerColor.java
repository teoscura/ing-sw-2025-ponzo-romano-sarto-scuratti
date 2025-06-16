package it.polimi.ingsw.model.player;

/**
 * Enumeration representing all supported player colors.
 */
public enum PlayerColor {

	RED(0, 52),
	BLUE(1, 33),
	GREEN(2, 34),
	YELLOW(3, 61),
	NONE(-1, 1);

	private final int order;
	private final int center_cabin_id;

	PlayerColor(int order, int center_cabin_id) {
		this.order = order;
		this.center_cabin_id = center_cabin_id;
	}

	static public PlayerColor getColor(int i) {
		switch (i) {
			case 0:
				return PlayerColor.RED;
			case 1:
				return PlayerColor.BLUE;
			case 2:
				return PlayerColor.GREEN;
			case 3:
				return PlayerColor.YELLOW;
			default:
				throw new RuntimeException();
		}
	}

	public int getID(){
		return center_cabin_id;
	}

	public int getOrder() {
		return this.order;
	}
}

