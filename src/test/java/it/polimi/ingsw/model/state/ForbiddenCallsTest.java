package it.polimi.ingsw.model.state;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.DummyController;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.DiscardComponentMessage;
import it.polimi.ingsw.message.server.PlayerGiveUpMessage;
import it.polimi.ingsw.message.server.RemoveComponentMessage;
import it.polimi.ingsw.message.server.SelectBlobMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.SetCrewMessage;
import it.polimi.ingsw.message.server.TakeComponentMessage;
import it.polimi.ingsw.message.server.TakeDiscardedComponentMessage;
import it.polimi.ingsw.message.server.ToggleHourglassMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.visitors.ContainsLoaderVisitor;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class ForbiddenCallsTest {

    private DummyModelInstance model;
	private EndscreenState state;
	private Planche planche;
	private Player player1;
	private ClientDescriptor p1desc;
	private Player player2;
	private ClientDescriptor p2desc;
	private Player player3;
	private ClientDescriptor p3desc;

	@BeforeEach
	void setUp() {
		ComponentFactory f1 = new ComponentFactory();
		ComponentFactory f2 = new ComponentFactory();
		ComponentFactory f3 = new ComponentFactory();
		BaseComponent c = null;

		player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		c = f1.getComponent(14);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
		c = f1.getComponent(18);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f1.getComponent(53);
		c.rotate(ComponentRotation.U000);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		player1.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 2));
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		player1.getSpaceShip().removeComponent(new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(54);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		c = f1.getComponent(55);
		player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 1));
		p1desc = new ClientDescriptor("p1", new DummyConnection());
		p1desc.bindPlayer(player1);

		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.BLUE);
		p2desc = new ClientDescriptor("p2", new DummyConnection());
		p2desc.bindPlayer(player2);
		c = f2.getComponent(62);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f2.getComponent(31);
		c.rotate(ComponentRotation.U000);
		player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		ContainsLoaderVisitor v2r = new ContainsLoaderVisitor(player2.getSpaceShip(), ShipmentType.RED);
		player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v2r);
		ContainsLoaderVisitor v2g = new ContainsLoaderVisitor(player2.getSpaceShip(), ShipmentType.BLUE);
		player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(v2g);
		player2.giveCredits(10);

		player3 = new Player(GameModeType.TEST, "p3", PlayerColor.GREEN);
		p3desc = new ClientDescriptor("p3", new DummyConnection());
		p3desc.bindPlayer(player3);
		c = f3.getComponent(62);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
		c = f3.getComponent(31);
		c.rotate(ComponentRotation.U000);
		player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
		ContainsLoaderVisitor v3r = new ContainsLoaderVisitor(player3.getSpaceShip(), ShipmentType.RED);
		player3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 2)).check(v3r);
		ContainsLoaderVisitor v3g = new ContainsLoaderVisitor(player3.getSpaceShip(), ShipmentType.GREEN);
		player3.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 4, 2)).check(v3g);

		ArrayList<Player> order = new ArrayList<>(Arrays.asList(player1, player3));
		planche = new Planche(GameModeType.TEST, order);
		ArrayList<Player> tmp = new ArrayList<>();
		tmp.addAll(order);
		planche.loseGame(player2);
		player2.retire();
		model = new DummyModelInstance(0, GameModeType.TEST, PlayerCount.THREE);
		model.setController(new DummyController(model.getID()));
		tmp = new ArrayList<>(tmp.stream().filter(p -> !p.getRetired()).toList());
		tmp.sort((p1, p2) -> Integer.compare(planche.getPlayerPosition(p1), planche.getPlayerPosition(p2)));
		this.state = new EndscreenState(model, GameModeType.TEST, PlayerCount.THREE, new ArrayList<>(Arrays.asList(player1, player2, player3)), order);
	}

    @Test
    public void forbidden(){
        ShipCoords c = new ShipCoords(GameModeType.TEST, 0, 0);
        ShipmentType t = ShipmentType.RED;
        AlienType a = AlienType.BROWN;
        ServerMessage forb1 = new TurnOnMessage(c, c);
        forb1.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb1));
        ServerMessage forb2 = new SetCrewMessage(c, a);
        forb2.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb2));
        ServerMessage forb3 = new ToggleHourglassMessage();
        forb3.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb3));
        ServerMessage forb4 = new SendContinueMessage();
        forb4.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb4));
        ServerMessage forb5 = new TakeComponentMessage();
        forb5.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb5));
        ServerMessage forb6 = new DiscardComponentMessage();
        forb6.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb6));
        ServerMessage forb7 = new TakeDiscardedComponentMessage(132);
        forb7.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb7));
        ServerMessage forb8 = new RemoveComponentMessage(c);
        forb8.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb8));
        ServerMessage forb9 = new SelectBlobMessage(c);
        forb9.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb9));
        ServerMessage forb10 = new PlayerGiveUpMessage();
        forb10.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () -> model.validate(forb10));
        ClientDescriptor tst = new ClientDescriptor("Gino", new DummyConnection());
        //Shouldnt connect.
        model.startGame();
        model.connect(tst);
        model.disconnect(tst);
    }

}
