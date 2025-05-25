package it.polimi.ingsw.model.player;

public enum PlayerColor {

	RED(0),
	BLUE(1),
	GREEN(2),
	YELLOW(3),
	NONE(-1);

	private final int order;

	PlayerColor(int order) {
		this.order = order;
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

	public int getOrder() {
		return this.order;
	}
}

