package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CombatZoneCardTest {

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	ArrayList<Player> order, players;
	private DummyModelInstance model;
	private DummyVoyageState state;
	private TestFlightCards cards;
	private Planche planche;
	private CombatZoneCard card;
	private CardState cstate;

	@BeforeEach
	void setup() {

		BaseComponent c = null;
		ComponentFactory f = new ComponentFactory();

		//Ha motori = 3 se usa batteria. minor crew.
		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f.getComponent(14);
		c.rotate(ComponentRotation.U090);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f.getComponent(75);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f.getComponent(83);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		CrewRemoveVisitor v = new CrewRemoveVisitor(player1.getSpaceShip());
		player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 2)).check(v);
		p1desc = new ClientDescriptor(player1.getUsername(), new DummyConnection());
		p1desc.bindPlayer(player1);

		//Ha tanta tanta crew. minor cannone e motore
		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
		c = f.getComponent(53);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f.getComponent(38);
		c.rotate(ComponentRotation.U180);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
		c = f.getComponent(36);
		c.rotate(ComponentRotation.U180);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
		c = f.getComponent(137);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 4));
		c = f.getComponent(49);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
		p2desc = new ClientDescriptor(player2.getUsername(), new DummyConnection());
		p2desc.bindPlayer(player2);

		//Ha tanti cannoni, zero motori.
		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.RED);
		c = f.getComponent(14);
		c.rotate(ComponentRotation.U090);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f.getComponent(133);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		c = f.getComponent(120);
		c.rotate(ComponentRotation.U090);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		p3desc = new ClientDescriptor(player3.getUsername(), new DummyConnection());
		p3desc.bindPlayer(player3);


		order = new ArrayList<>(Arrays.asList(player1, player2, player3));
		players = new ArrayList<>(Arrays.asList(player1, player2, player3));
		model = new DummyModelInstance(1, GameModeType.LVL2, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);

	}

	//Carta vuota.
	@Test
	void behaviour1() {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		state.loseGame(player1);
		state.loseGame(player2);
		cstate = card.getState(state);
		state.setCardState(cstate);
		assertNull(state.getCardState(player1));
	}

	//Colpito e morto
	@Test
	void behaviour2() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		//Test start
		ServerMessage message = null;

		cstate = card.getState(state);
		state.setCardState(cstate);
		int x = this.planche.getPlayerPosition(player1);
		//Phase 1 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(this.planche.getPlayerPosition(player1), x - 4);
		//Phase 2 perde p2
		x = player2.getSpaceShip().getTotalCrew();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//p2 toglie due crewmate, tenta una roba proibita.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(player2.getSpaceShip().getTotalCrew(), x - 2);
		//Phase 3 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Take first shot.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);

		//He needs to choose the ship first, then take second
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		//Take second
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Over.
		assertTrue(player1.getSpaceShip().getBlobsSize() == 1 && player1.getRetired());
		assertEquals(0, player1.getSpaceShip().getTotalCrew());
		assertNull(this.state.getCardState((player1)));
	}

	//Mancato
	@Test
	void behaviour3() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 9));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 8));
		//Test start
		ServerMessage message = null;

		cstate = card.getState(state);
		state.setCardState(cstate);
		int x = this.planche.getPlayerPosition(player1);
		//Phase 1 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(this.planche.getPlayerPosition(player1), x - 4);
		//Phase 2 perde p2
		x = player2.getSpaceShip().getTotalCrew();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//p2 toglie due crewmate, tenta una roba proibita.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(player2.getSpaceShip().getTotalCrew(), x - 2);
		//Phase 3 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Take first shot.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Take second.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Over.
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3)), player1.getSpaceShip().getEmpty());
		assertFalse(player1.getRetired());
		assertEquals(1, player1.getSpaceShip().getEnginePower());
		assertEquals(1, player1.getSpaceShip().getTotalCrew());
		assertNull(this.state.getCardState((player1)));
	}

	//Scudato e cannonato
	@Test
	void behaviour4() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		//Aggiungo shield component
		BaseComponent c = null;
		ComponentFactory f = new ComponentFactory();
		c = f.getComponent(149);
		c.rotate(ComponentRotation.U090);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
		//Test start
		ServerMessage message = null;

		cstate = card.getState(state);
		state.setCardState(cstate);
		int x = this.planche.getPlayerPosition(player1);
		//Phase 1 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(this.planche.getPlayerPosition(player1), x - 4);
		//Phase 2 perde p2
		x = player2.getSpaceShip().getTotalCrew();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//p2 toglie due crewmate, tenta una roba proibita.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(player2.getSpaceShip().getTotalCrew(), x - 2);
		//Phase 3 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Turn on shield and take first shot.

		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 5, 3), new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Turn on shield and takes second, since he cant shield a big meteorite.
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 5, 3), new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Player 1 chooses ship piece.
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		//Over.

		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 5, 3)), player1.getSpaceShip().getEmpty());
		assertEquals(0, player1.getSpaceShip().getEnginePower());
		assertEquals(1, player1.getSpaceShip().getTotalCrew());
		assertNull(this.state.getCardState((player1)));
	}

	@Test
	void otherCard() throws ForbiddenCallException {
		LevelTwoCardFactory factory = new LevelTwoCardFactory();
		card = (CombatZoneCard) factory.getCard(116);
		model.setState(state);
		state.setCard(card);
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(2);
		shots.set(2, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(3);
		shots.set(3, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		//Aggiungo shield component
		BaseComponent c = null;
		ComponentFactory f = new ComponentFactory();
		c = f.getComponent(149);
		c.rotate(ComponentRotation.U090);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 3));
		c = f.getComponent(150);
		c.rotate(ComponentRotation.U270);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 5, 2));
		//Do storage a p2.
		c = f.getComponent(18);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		ContainsLoaderVisitor v = new ContainsLoaderVisitor(player2.getSpaceShip(), ShipmentType.BLUE);
		player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v);
		ServerMessage message = null;

		cstate = card.getState(state);
		state.setCardState(cstate);
		//Phase 1, perde p1, Tutti vanno avanti
		int x = this.planche.getPlayerPosition(player1);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//P1 loses
		assertEquals(this.planche.getPlayerPosition(player1), x - 6);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//P2 cargo shenanigans.
		assertEquals(2, player2.getSpaceShip().getContains()[1]);
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.RED);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(2, player2.getSpaceShip().getContains()[1]);
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(1, player2.getSpaceShip().getContains()[1]);
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 3, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(1, player2.getSpaceShip().getContains()[1]);
		ServerMessage w = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		message = new DiscardCargoMessage(new ShipCoords(GameModeType.TEST, 2, 2), ShipmentType.BLUE);
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(0, player2.getSpaceShip().getContains()[1]);
		//Phase 3, p1 loses.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//Ora il magheggio, primo colpo
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 5, 2), new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Ora il magheggio, secondo colpo
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 5, 2), new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Ora il magheggio, terzo colpo
		message = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 5, 3), new ShipCoords(GameModeType.TEST, 2, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Ultimo colpo non parabile.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Saves the crew
		//Player 1 chooses ship piece.
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		//End test

		assertSame(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)));
		assertSame(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3)));
		assertSame(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 5, 3)));
		assertSame(player1.getSpaceShip().getEmpty(), player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 5, 2)));
		assertNull(state.getCardState(player1));
	}

	@Test
	void newCabinTest() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		//Aggiungo shield component
		BaseComponent c = null;
		ComponentFactory f = new ComponentFactory();
		//Ha motori = 3 se usa batteria. minor crew.
		c = f.getComponent(48);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
		//Test start
		ServerMessage message = null;

		cstate = card.getState(state);
		state.setCardState(cstate);
		int x = this.planche.getPlayerPosition(player3);
		System.out.println(x);
		//Phase 1 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(this.planche.getPlayerPosition(player3), x - 3);
		//Phase 2 perde p2
		x = player2.getSpaceShip().getTotalCrew();
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//p2 toglie due crewmate, tenta una roba proibita.
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		ServerMessage w = new TakeRewardMessage(false);
		w.setDescriptor(p2desc);
		assertThrows(ForbiddenCallException.class, () -> state.validate(w));
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
		assertEquals(player2.getSpaceShip().getTotalCrew(), x - 2);
		//Phase 3 perde p1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		// Take two shots and lose cabin.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//P1 has to choose new ship;

		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		//Takes last one
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertTrue(player1.getSpaceShip().getBlobsSize() > 1);
		//selects the last cabin, enough to survive.
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 1));
		message.setDescriptor(p1desc);
		state.validate(message);
		assertEquals(1, player1.getSpaceShip().getBlobsSize());
		// //Over.
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 5, 3)), player1.getSpaceShip().getEmpty());
		assertEquals(0, player1.getSpaceShip().getEnginePower());
		assertEquals(2, player1.getSpaceShip().getTotalCrew());
		assertNull(this.state.getCardState((player1)));
	}

	@Test
	void disconnectionResilience1() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		ServerMessage message = null;

		int x = planche.getPlayerPosition(player1);
		cstate = card.getState(state);
		state.setCardState(cstate);
		//Phase 1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(x - 4, planche.getPlayerPosition(player1));
		assertFalse(player3.getRetired());
		//Phase 2
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		//P2 disconnects. therefore is not considered.
		message = new ServerDisconnectMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		assertFalse(player3.getRetired());
		//p2 disconnects, its p3's penalty. and he gets killed.
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		//P3 gives up two crew in coords
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p3desc);
		state.validate(message);
		assertFalse(player3.getRetired());
		message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p3desc);
		model.validate(message);
		//Phase 3
		message = new ServerDisconnectMessage();
		message.setDescriptor(p1desc);
		model.validate(message);
		//p2 disconnects, its p3's penalty. and he gets killed.
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//And loses.
		assertTrue(player3.getRetired());
		assertNull(state.getCardState(player1));
	}

	@Test
	void disconnectionResilience2() throws ForbiddenCallException {
		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
		state.setCard(card);
		//Fisso gli offset della carta per avere dei risultati deterministici.
		ArrayList<Projectile> shots = card.getShots();
		Projectile pr = shots.get(0);
		shots.set(0, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		pr = shots.get(1);
		shots.set(1, new Projectile(pr.getDirection(), pr.getDimension(), 7));
		ServerMessage message = null;

		int x = planche.getPlayerPosition(player1);
		cstate = card.getState(state);
		state.setCardState(cstate);
		//Phase 1
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		assertEquals(x - 4, planche.getPlayerPosition(player1));
		//Phase 2
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p2desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		//P2 disconnects. abd escapes penalty.
		message = new ServerDisconnectMessage();
		message.setDescriptor(p2desc);
		model.validate(message);
		//Phase 3
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		message = new SendContinueMessage();
		message.setDescriptor(p3desc);
		state.validate(message);
		// Take two shots and lose cabin.
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		//Player1 chooses ship piece

		//Player1 chooses wrong coords, nothing happens;
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 5, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		assertEquals(2, player1.getSpaceShip().getBlobsSize());
		//then he chooses right
		message = new SelectBlobMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p1desc);
		state.validate(message);
		assertEquals(1, player1.getSpaceShip().getBlobsSize());
		assertFalse(player1.getRetired());
		//Takes last
		message = new SendContinueMessage();
		message.setDescriptor(p1desc);
		state.validate(message);
		// //Over.
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 3, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 3)), player1.getSpaceShip().getEmpty());
		assertSame(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 5, 3)), player1.getSpaceShip().getEmpty());
		assertEquals(0, player1.getSpaceShip().getEnginePower());
		assertEquals(0, player1.getSpaceShip().getTotalCrew());
		assertNull(this.state.getCardState((player1)));
	}

}
