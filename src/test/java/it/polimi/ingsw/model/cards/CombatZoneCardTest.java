package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.RemoveCrewMessage;
import it.polimi.ingsw.message.server.SelectLandingMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeRewardMessage;
import it.polimi.ingsw.message.server.TurnOnMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.StartingCabinComponent;
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
        //2 si arrende, sa che ha perso praticamente.
        mess = new SendContinueMessage();
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //3 accende due motori
        mess = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 1, 3), new ShipCoords(GameModeType.TEST, 3, 3));
        mess.setDescriptor(p3desc);
        state.validate(mess);
        mess = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 3), new ShipCoords(GameModeType.TEST, 4, 2));
        mess.setDescriptor(p3desc);
        state.validate(mess);
        //1 ne accende uno per non essere ultimo
        mess = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 0, 3), new ShipCoords(GameModeType.TEST, 1, 2));
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //2 si pente, viene gabbato dal fatto che ormai ha gia fatto
        mess = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 3), new ShipCoords(GameModeType.TEST, 4, 2));
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //3 sa che si salva
        mess = new SendContinueMessage();
        mess.setDescriptor(p3desc);
        state.validate(mess);
        //1 si accontenta di essere 2o
        mess = new SendContinueMessage();
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //Giusto per testare;
        ServerMessage smess = new TakeRewardMessage(false);
        smess.setDescriptor(p1desc);
        assertThrows(ForbiddenCallException.class, () ->cstate.validate(smess));
        //dove siamo?
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
        //player 2 deve cedere tutta la sua crew
        mess = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 1, 1));
        mess.setDescriptor(p2desc);
        state.validate(mess);
        mess = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 1, 1));
        mess.setDescriptor(p2desc);
        state.validate(mess);
        for(Player p : this.state.getOrder(CardOrder.NORMAL)){
			p.getSpaceShip().updateShip();
			System.out.println(" - "+p.getUsername()+": "+p.getSpaceShip().getCannonPower()+"/"+p.getSpaceShip().getEnginePower()+"/"+p.getSpaceShip().getTotalCrew());
		}
        //TODO Ha perso ( da sistemare quando sistemiamo gli aggiornamenti del model);
        System.out.println(((CabinComponent)player2.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 1, 1))).getCrew());
        player2.getSpaceShip().updateShip();
        assertTrue(player2.getRetired());
        //player 3 ha poche batterie, la rischia;
        mess = new SendContinueMessage();
        mess.setDescriptor(p3desc);
        state.validate(mess);
        System.out.println(player1.getSpaceShip().getComponent(new ShipCoords(GameModeType.TEST, 2, 0)).getClass().getSimpleName());
        //player 1 cerca la patta cosi vince lui per ordine di campo.
        mess = new TurnOnMessage(new ShipCoords(GameModeType.TEST, 2, 0), new ShipCoords(GameModeType.TEST, 1, 2));
        mess.setDescriptor(p1desc);
        state.validate(mess);

        //clutch
        mess = new SendContinueMessage();
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //Dove siamo?
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());

    }

}
