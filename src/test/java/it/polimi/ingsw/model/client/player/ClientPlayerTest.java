package it.polimi.ingsw.model.client.player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.DummyConnection;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class ClientPlayerTest {
    
    private ClientSpaceShip s;

    @BeforeEach
    public void setup(){
        Player player1;
        ClientDescriptor p1desc;
        ComponentFactory f1 = new ComponentFactory();
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
        this.s = player1.getSpaceShip().getClientSpaceShip();
    }

    @Test 
    public void clientPlayers(){
        ClientWaitingPlayer pw = new ClientWaitingPlayer("gigio", PlayerColor.BLUE);
        assertEquals(pw.getColor(), PlayerColor.BLUE);
        assertEquals(pw.getUsername(), "gigio");

        ClientConstructionPlayer pc = new ClientConstructionPlayer("gigio", PlayerColor.BLUE, s, 2, new ArrayList<>(), false, false);
        assertEquals(pc.getUsername(), "gigio");
        assertEquals(pc.getColor(), PlayerColor.BLUE);
        assertEquals(pc.getShip(), s);
        assertEquals(pc.getCurrent(), 2);
        assertEquals(pc.getReserved(), new ArrayList<>());
        assertFalse(pc.isDisconnected());
        assertFalse(pc.isFinished());

        ClientVerifyPlayer pv = new ClientVerifyPlayer("gigio", PlayerColor.BLUE, s, true, false, false, false, 2);
        assertEquals(pv.getUsername(), "gigio");
        assertEquals(pv.getColor(), PlayerColor.BLUE);
        assertEquals(pv.getShip(), s);
        assertTrue(pv.isValid());
        assertFalse(pv.isDisconnected());
        assertFalse(pv.hasProgressed());
        assertFalse(pv.startsLosing());
        assertEquals(pv.getOrder(), 2);

        ClientVoyagePlayer pvg = new ClientVoyagePlayer("gigio", PlayerColor.BLUE, s, 1, 2, false, false);
        assertEquals(pvg.getUsername(), "gigio");
        assertEquals(pvg.getColor(), PlayerColor.BLUE);
        assertEquals(pvg.getShip(), s);
        assertEquals(pvg.getPlancheSlot(), 1);
        assertEquals(pvg.getCredits(), 2);
        assertFalse(pvg.isDisconnected());
        assertFalse(pvg.isRetired());

        ClientEndgamePlayer pe = new ClientEndgamePlayer("gigio", PlayerColor.BLUE, 1, 2, new int[]{0,1,1,1,1});
        assertEquals(pe.getUsername(), "gigio");
        assertEquals(pe.getColor(), PlayerColor.BLUE);
        assertEquals(pe.getPlanche_slot(), 1);
        assertEquals(pe.getCredits(), 2);
        assertArrayEquals(pe.getShipments(), new int[]{0,1,1,1,1});
    }

}
