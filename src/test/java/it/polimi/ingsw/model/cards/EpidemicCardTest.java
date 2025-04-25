package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.*;
import it.polimi.ingsw.model.components.enums.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicCardTest {
	private VoyageState state;
	private Planche planche;
	private EpidemicCard card;
	private CardState cstate;
	private ModelInstance model;

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player_scarso;
	ClientDescriptor psdesc;

	@BeforeEach
	void setUp() throws UnknownHostException, IOException {

		StorageComponent triple_storage = new StorageComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, StorageType.TRIPLENORMAL, new ShipCoords(GameModeType.LVL2, 4, 2));
		AlienLifeSupportComponent alien_support = new AlienLifeSupportComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.UNIVERSAL}, ComponentRotation.U000, AlienType.PURPLE, new ShipCoords(GameModeType.LVL2, 3, 3));
		CabinComponent human_cabin1 = new CabinComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.LVL2, 2, 2));
		CabinComponent human_cabin2 = new CabinComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.LVL2, 3, 1));
		BatteryComponent triple_battery1 = new BatteryComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, BatteryType.TRIPLE, new ShipCoords(GameModeType.LVL2, 2, 1));
		BatteryComponent triple_battery2 = new BatteryComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, BatteryType.TRIPLE, new ShipCoords(GameModeType.LVL2, 4, 1));
		StorageComponent special_storage = new StorageComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, StorageType.DOUBLESPECIAL, new ShipCoords(GameModeType.LVL2, 4, 3));
		CabinComponent alien_cabin = new CabinComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U000, new ShipCoords(GameModeType.LVL2, 2, 3));
		CannonComponent front_single_cannon = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY}, ComponentRotation.U000, CannonType.SINGLE, new ShipCoords(GameModeType.LVL2, 4, 0));
		CannonComponent right_cannon = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY}, ComponentRotation.U090, CannonType.SINGLE, new ShipCoords(GameModeType.LVL2, 5, 2));
		ShieldComponent bottom_shield = new ShieldComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U090, new ShipCoords(GameModeType.LVL2, 5, 3));
		EngineComponent right_single_engine = new EngineComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL}, ComponentRotation.U000, EngineType.SINGLE, new ShipCoords(GameModeType.LVL2, 5, 4));
		EngineComponent right_double_engine = new EngineComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, EngineType.DOUBLE, new ShipCoords(GameModeType.LVL2, 4, 4));
		EngineComponent left_double_engine = new EngineComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL}, ComponentRotation.U000, EngineType.DOUBLE, new ShipCoords(GameModeType.LVL2, 2, 4));
		EngineComponent left_single_engine = new EngineComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.EMPTY, ConnectorType.EMPTY}, ComponentRotation.U000, EngineType.SINGLE, new ShipCoords(GameModeType.LVL2, 1, 4));
		ShieldComponent top_shield = new ShieldComponent(1, new ConnectorType[]{ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U270, new ShipCoords(GameModeType.LVL2, 1, 3));
		CannonComponent left_cannon = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.UNIVERSAL}, ComponentRotation.U270, CannonType.SINGLE, new ShipCoords(GameModeType.LVL2, 1, 2));
		CannonComponent front_double_cannon = new CannonComponent(1, new ConnectorType[]{ConnectorType.EMPTY, ConnectorType.EMPTY, ConnectorType.UNIVERSAL, ConnectorType.EMPTY}, ComponentRotation.U000, CannonType.DOUBLE, new ShipCoords(GameModeType.LVL2, 1, 1));

		player1 = new Player(GameModeType.LVL2, "Player1", PlayerColor.RED);
		player1.getSpaceShip().addComponent(triple_storage, new ShipCoords(GameModeType.LVL2, 4, 2));
		player1.getSpaceShip().addComponent(alien_support, new ShipCoords(GameModeType.LVL2, 3, 3));
		player1.getSpaceShip().addComponent(human_cabin1, new ShipCoords(GameModeType.LVL2, 2, 2));
		player1.getSpaceShip().addComponent(human_cabin2, new ShipCoords(GameModeType.LVL2, 3, 1));
		player1.getSpaceShip().addComponent(triple_battery1, new ShipCoords(GameModeType.LVL2, 2, 1));
		player1.getSpaceShip().addComponent(triple_battery2, new ShipCoords(GameModeType.LVL2, 4, 1));
		player1.getSpaceShip().addComponent(special_storage, new ShipCoords(GameModeType.LVL2, 4, 3));
		player1.getSpaceShip().addComponent(alien_cabin, new ShipCoords(GameModeType.LVL2, 2, 3));
		player1.getSpaceShip().addComponent(front_single_cannon, new ShipCoords(GameModeType.LVL2, 4, 0));
		player1.getSpaceShip().addComponent(right_cannon, new ShipCoords(GameModeType.LVL2, 5, 2));
		player1.getSpaceShip().addComponent(bottom_shield, new ShipCoords(GameModeType.LVL2, 5, 3));
		player1.getSpaceShip().addComponent(right_single_engine, new ShipCoords(GameModeType.LVL2, 5, 4));
		player1.getSpaceShip().addComponent(right_double_engine, new ShipCoords(GameModeType.LVL2, 4, 4));
		player1.getSpaceShip().addComponent(left_double_engine, new ShipCoords(GameModeType.LVL2, 2, 4));
		player1.getSpaceShip().addComponent(left_single_engine, new ShipCoords(GameModeType.LVL2, 1, 4));
		player1.getSpaceShip().addComponent(top_shield, new ShipCoords(GameModeType.LVL2, 1, 3));
		player1.getSpaceShip().addComponent(left_cannon, new ShipCoords(GameModeType.LVL2, 1, 2));
		player1.getSpaceShip().addComponent(front_double_cannon, new ShipCoords(GameModeType.LVL2, 1, 1));
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

		player2 = new Player(GameModeType.LVL2, "Player2", PlayerColor.BLUE);
		player2.getSpaceShip().addComponent(triple_storage, new ShipCoords(GameModeType.LVL2, 4, 2));
		player2.getSpaceShip().addComponent(alien_support, new ShipCoords(GameModeType.LVL2, 3, 3));
		player2.getSpaceShip().addComponent(human_cabin1, new ShipCoords(GameModeType.LVL2, 2, 2));
		player2.getSpaceShip().addComponent(human_cabin2, new ShipCoords(GameModeType.LVL2, 3, 1));
		player2.getSpaceShip().addComponent(triple_battery1, new ShipCoords(GameModeType.LVL2, 2, 1));
		player2.getSpaceShip().addComponent(triple_battery2, new ShipCoords(GameModeType.LVL2, 4, 1));
		player2.getSpaceShip().addComponent(special_storage, new ShipCoords(GameModeType.LVL2, 4, 3));
		player2.getSpaceShip().addComponent(alien_cabin, new ShipCoords(GameModeType.LVL2, 2, 3));
		player2.getSpaceShip().addComponent(front_single_cannon, new ShipCoords(GameModeType.LVL2, 4, 0));
		player2.getSpaceShip().addComponent(right_cannon, new ShipCoords(GameModeType.LVL2, 5, 2));
		player2.getSpaceShip().addComponent(bottom_shield, new ShipCoords(GameModeType.LVL2, 5, 3));
		player2.getSpaceShip().addComponent(right_single_engine, new ShipCoords(GameModeType.LVL2, 5, 4));
		player2.getSpaceShip().addComponent(right_double_engine, new ShipCoords(GameModeType.LVL2, 4, 4));
		player2.getSpaceShip().addComponent(left_double_engine, new ShipCoords(GameModeType.LVL2, 2, 4));
		player2.getSpaceShip().addComponent(left_single_engine, new ShipCoords(GameModeType.LVL2, 1, 4));
		player2.getSpaceShip().addComponent(top_shield, new ShipCoords(GameModeType.LVL2, 1, 3));
		player2.getSpaceShip().addComponent(left_cannon, new ShipCoords(GameModeType.LVL2, 1, 2));
		player2.getSpaceShip().addComponent(front_double_cannon, new ShipCoords(GameModeType.LVL2, 1, 1));
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

		player_scarso = new Player(GameModeType.LVL2, "Player_scarso", PlayerColor.GREEN);
		player_scarso.getSpaceShip().addComponent(triple_storage, new ShipCoords(GameModeType.LVL2, 4, 2));
		player_scarso.getSpaceShip().addComponent(alien_support, new ShipCoords(GameModeType.LVL2, 3, 3));
		player_scarso.getSpaceShip().addComponent(human_cabin1, new ShipCoords(GameModeType.LVL2, 2, 2));
		player_scarso.getSpaceShip().addComponent(triple_battery1, new ShipCoords(GameModeType.LVL2, 2, 1));
		player_scarso.getSpaceShip().addComponent(triple_battery2, new ShipCoords(GameModeType.LVL2, 4, 1));
		player_scarso.getSpaceShip().addComponent(special_storage, new ShipCoords(GameModeType.LVL2, 4, 3));
		player_scarso.getSpaceShip().addComponent(alien_cabin, new ShipCoords(GameModeType.LVL2, 2, 3));
		player_scarso.getSpaceShip().addComponent(front_single_cannon, new ShipCoords(GameModeType.LVL2, 4, 0));
		player_scarso.getSpaceShip().addComponent(right_cannon, new ShipCoords(GameModeType.LVL2, 5, 2));
		player_scarso.getSpaceShip().addComponent(bottom_shield, new ShipCoords(GameModeType.LVL2, 5, 3));
		player_scarso.getSpaceShip().addComponent(right_single_engine, new ShipCoords(GameModeType.LVL2, 5, 4));
		player_scarso.getSpaceShip().addComponent(right_double_engine, new ShipCoords(GameModeType.LVL2, 4, 4));
		player_scarso.getSpaceShip().addComponent(left_double_engine, new ShipCoords(GameModeType.LVL2, 2, 4));
		player_scarso.getSpaceShip().addComponent(left_single_engine, new ShipCoords(GameModeType.LVL2, 1, 4));
		player_scarso.getSpaceShip().addComponent(top_shield, new ShipCoords(GameModeType.LVL2, 1, 3));
		player_scarso.getSpaceShip().addComponent(left_cannon, new ShipCoords(GameModeType.LVL2, 1, 2));
		player_scarso.getSpaceShip().addComponent(front_double_cannon, new ShipCoords(GameModeType.LVL2, 1, 1));
		psdesc = new ClientDescriptor(player_scarso.getUsername(), null);
		psdesc.bindPlayer(player_scarso);


		((StartingCabinComponent) player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 3, 2))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
		((CabinComponent) player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 2, 3))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
		ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player_scarso, player2}));
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player_scarso}));
		model = new ModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
		TestFlightCards cards = new TestFlightCards();
		planche = new Planche(GameModeType.LVL2, order);
		state = new VoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		model.setState(state);
		cstate = this.state.getCardState(player1);
		System.out.println(cstate.getClass().getSimpleName());

		LevelTwoCardFactory factory = new LevelTwoCardFactory();
		card = (EpidemicCard) factory.getCard(105);

		player1.getSpaceShip().updateShip();
		player2.getSpaceShip().updateShip();
		player_scarso.getSpaceShip().updateShip();
		cstate = card.getState(state);

	}

	@Test
	void test() {
		int[] exp1 = new int[]{4, 0, 0};
		int[] exp2 = new int[]{4, 0, 0};
		int[] expscarso = new int[]{2, 0, 0};

		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		assertArrayEquals(exp2, player2.getSpaceShip().getCrew());
		assertArrayEquals(expscarso, player_scarso.getSpaceShip().getCrew());

		state.setCardState(cstate);

		exp1 = new int[]{0, 0, 0};
		exp2 = new int[]{2, 0, 0};
		expscarso = new int[]{0, 0, 0};

		assertArrayEquals(exp1, player1.getSpaceShip().getCrew());
		assertArrayEquals(exp2, player2.getSpaceShip().getCrew());
		assertArrayEquals(expscarso, player_scarso.getSpaceShip().getCrew());
	}
}