package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.SpaceShip;

class PlancheTest {

	private Planche planche;
	private SpaceShip shipRed;
	private SpaceShip shipBlue;

	// @BeforeEach
	// void setUp() {
	//     planche = new Planche(GameModeType.LVL2, PlayerCount.TWO);
	//     shipRed = new SpaceShip(GameModeType.LVL2, PlayerColor.RED);
	//     shipBlue = new SpaceShip(GameModeType.LVL2, PlayerColor.BLUE);
	// }

	// @Test
	// void getPlayerPosition() {
	//     assertEquals(0, planche.getPlayerPosition(shipRed));
	//     assertEquals(0, planche.getPlayerPosition(shipBlue));
	// }

	// @Test
	// void getPlayersAt() {
	//     planche.movePlayer(shipBlue, 1);
	//     assertEquals(PlayerColor.RED, planche.getPlayersAt(0));
	//     assertEquals(PlayerColor.BLUE, planche.getPlayersAt(1));
	// }

	// @Test
	// void movePlayer() {
	//     planche.movePlayer(shipRed, 3);
	//     planche.movePlayer(shipBlue, 2);
	//     planche.movePlayer(shipBlue, 1);
	//     assertEquals(4, planche.getPlayerPosition(shipBlue));
	//     assertEquals(3, planche.getPlayerPosition(shipRed));

	//     planche.movePlayer(shipBlue, -2);
	//     planche.movePlayer(shipRed, -1);

	//     assertEquals(2, planche.getPlayerPosition(shipRed));
	//     assertEquals(1, planche.getPlayerPosition(shipBlue));
	// }

	// @Test
	// void getOrder() {
	//     planche.movePlayer(shipRed, 3);
	//     planche.movePlayer(shipBlue, 1);

	//     List<PlayerColor> order = planche.getOrder();
	//     assertEquals(List.of(PlayerColor.BLUE, PlayerColor.RED), order);
	// }
}