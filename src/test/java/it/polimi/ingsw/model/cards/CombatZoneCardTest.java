package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.SelectLandingMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class CombatZoneCardTest {
    
    private ModelInstance model;
    private VoyageState state;
    private Planche planche;
    private CombatZoneCard card;
    private CardState cstate;

    Player player1;
    ClientDescriptor p1desc;
    Player player2;
    ClientDescriptor p2desc;
    Player player3;
    ClientDescriptor p3desc;

    @BeforeEach
    void setup(){
        
        player1 = new Player(GameModeType.TEST, "Player1", PlayerColor.RED);
        player2 = new Player(GameModeType.TEST, "Player2", PlayerColor.BLUE);
        player3 = new Player(GameModeType.TEST, "Player3", PlayerColor.GREEN);
        
        ComponentFactory fact = new ComponentFactory();
        iBaseComponent c = null;
        
        c = fact.getComponent(101); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 0));
        c = fact.getComponent(145); c.rotate(ComponentRotation.U270);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1, 1));
        c = fact.getComponent(23); c.rotate(ComponentRotation.U090);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 1));
        c = fact.getComponent(1); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1, 2));
        c = fact.getComponent(35); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
        c = fact.getComponent(71); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 0, 3));
        c = fact.getComponent(81); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1, 3));
        c = fact.getComponent(20); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
        c = fact.getComponent(26); c.rotate(ComponentRotation.U000);
        player1.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 3));
        p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

        c = fact.getComponent(38); c.rotate(ComponentRotation.U270);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1,1));
        c = fact.getComponent(109); c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 1));
        c = fact.getComponent(152); c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3, 1));
        c = fact.getComponent(93); c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1, 2));
        c = fact.getComponent(5); c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4, 2));
        c = fact.getComponent(73); c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 3));
        p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

        c = fact.getComponent(112); c.rotate(ComponentRotation.U270);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1,1));
        c = fact.getComponent(103); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2,1));
        c = fact.getComponent(107); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3,1));
        c = fact.getComponent(102); c.rotate(ComponentRotation.U270);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 0,2));
        c = fact.getComponent(36); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1,2));
        c = fact.getComponent(39); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3,2));
        c = fact.getComponent(9); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 4,2));
        c = fact.getComponent(82); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 1,3));
        c = fact.getComponent(75); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2,3));
        c = fact.getComponent(8); c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 3,3));
        p3desc = new ClientDescriptor(player3.getUsername(), null);
		p3desc.bindPlayer(player3);

        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player3, player2}));
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
		model = new ModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
		TestFlightCards cards = new TestFlightCards();
		planche = new Planche(GameModeType.LVL2, order);
		state = new VoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
		model.setState(state);
		cstate = this.state.getCardState(player1);
		System.out.println(cstate.getClass().getSimpleName());

		LevelOneCardFactory factory = new LevelOneCardFactory();
		card = (CombatZoneCard) factory.getCard(16);

		player1.getSpaceShip().updateShip();
		player2.getSpaceShip().updateShip();
		player3.getSpaceShip().updateShip();
		cstate = card.getState(state);
        state.setCardState(cstate);
    }

    @Test
    void behaviour() throws ForbiddenCallException{
        ServerMessage mess = null;
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
        //2 continua
        mess = new SendContinueMessage();
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //1 continua
        mess = new SendContinueMessage();
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //3 continua
        mess = new SendContinueMessage();
        mess.setDescriptor(p3desc);
        state.validate(mess);
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
        //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);
        // //Attempt sbagliato
        // mess = new SelectLandingMessage(0);
        // mess.setDescriptor(p2desc);
        // state.validate(mess);

    }

}
