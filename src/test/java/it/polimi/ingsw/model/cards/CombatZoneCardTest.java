package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.RemoveCrewMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneCardTest {
    
    private DummyModelInstance model;
    private DummyVoyageState state;
    private TestFlightCards cards;
    private Planche planche;
    private CombatZoneCard card;
    private CardState cstate;

    Player player1;
    ClientDescriptor p1desc;
    Player player2;
    ClientDescriptor p2desc;
    Player player3;
    ClientDescriptor p3desc;

    ArrayList<Player> order, players;

    @BeforeEach
    void setup(){

        iBaseComponent c = null;
        ComponentFactory f = new ComponentFactory();

        //Ha motori = 3 se usa batteria. minor crew.
        player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
        c = f.getComponent(14);
        c.rotate(ComponentRotation.U090); 
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        c = f.getComponent(75);
        c.rotate(ComponentRotation.U000); 
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f.getComponent(98);
        c.rotate(ComponentRotation.U000); 
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 3));
        CrewRemoveVisitor v = new CrewRemoveVisitor(player1.getSpaceShip());
        player1.getSpaceShip().getComponent(player1.getSpaceShip().getCenter()).check(v);
        p1desc = new ClientDescriptor(player1.getUsername(), null);
        p1desc.bindPlayer(player1);

        //Ha tanta tanta crew. minor cannone e motore
        player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
        c = f.getComponent(53);
        c.rotate(ComponentRotation.U000); 
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        c = f.getComponent(38);
        c.rotate(ComponentRotation.U270); 
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
        p2desc = new ClientDescriptor(player2.getUsername(), null);
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
        p3desc = new ClientDescriptor(player3.getUsername(), null);
        p3desc.bindPlayer(player3);

        LevelOneCardFactory factory = new LevelOneCardFactory();
        order = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
        planche = new Planche(GameModeType.LVL2, order);
        cards = new TestFlightCards();
        state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		card = (CombatZoneCard) factory.getCard(16);
		model.setState(state);
        state.setCard(card);
    }


    @Test
    void behaviour1(){
        state.loseGame(player1);
        state.loseGame(player2);
        cstate = card.getState(state);
        state.setCardState(cstate);
        assertTrue(null==state.getCardState(player1));
    }

    @Test
    void behaviour2() throws ForbiddenCallException{
        ServerMessage message = null;
        for(Player p : this.order){
            System.out.println(p.getUsername()+" - e:"+p.getSpaceShip().getEnginePower()+" - cr:"+p.getSpaceShip().getTotalCrew()+" - c:"+p.getSpaceShip().getCannonPower());
        }
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
        assertTrue(this.planche.getPlayerPosition(player1)==x-3);
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
		assertThrows(ForbiddenCallException.class, ()->state.validate(w));
        message = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
		message.setDescriptor(p2desc);
		state.validate(message);
        assertTrue(player2.getSpaceShip().getTotalCrew()==x-2);
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
        System.out.println(player1.getSpaceShip().getBrokeCenter());
        assertTrue(null==state.getCardState(player1));
        //XXX rotto.
    }

}
