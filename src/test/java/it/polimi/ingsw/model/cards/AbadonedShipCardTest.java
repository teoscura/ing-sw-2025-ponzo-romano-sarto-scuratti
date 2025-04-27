package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.AbandonedShipRewardState;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.AlienLifeSupportComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.CannonComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.EngineComponent;
import it.polimi.ingsw.model.components.ShieldComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.StorageComponent;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.enums.BatteryType;
import it.polimi.ingsw.model.components.enums.CannonType;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.EngineType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.state.DummyVoyageState;

public class AbadonedShipCardTest {
    
    private DummyModelInstance model;
    private DummyVoyageState state;
    private Planche planche;
    private AbandonedShipCard card;
    private CardState cstate;

    Player player1;
    ClientDescriptor p1desc;
    Player player2;
    ClientDescriptor p2desc;
    Player player3;
    ClientDescriptor p3desc;

    @BeforeEach
    void setUp() throws UnknownHostException, IOException{
        ComponentFactory f2 = new ComponentFactory();
        ComponentFactory f3 = new ComponentFactory();
        iBaseComponent c = null;

        player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
        //non ha niente, tenta a salire ma non riesce, ha esattamente il richiesto.
        p1desc = new ClientDescriptor(player1.getUsername(), null);
        p1desc.bindPlayer(player1);

        player2 = new Player(GameModeType.TEST, "p2", PlayerColor.BLUE);
        c = f2.getComponent(36);
        c.rotate(ComponentRotation.U000);
        player2.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        p2desc = new ClientDescriptor(player2.getUsername(), null);
        p2desc.bindPlayer(player2);

        player3 = new Player(GameModeType.TEST, "p3", PlayerColor.GREEN);
        c = f3.getComponent(36);
        c.rotate(ComponentRotation.U000);
        player3.getSpaceShip().addComponent(c, new ShipCoords(GameModeType.TEST, 2, 2));
        p3desc = new ClientDescriptor(player3.getUsername(), null);
        p3desc.bindPlayer(player3);

        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        model = new DummyModelInstance(1, null, GameModeType.TEST, PlayerCount.THREE);
        TestFlightCards cards = new TestFlightCards();
        planche = new Planche(GameModeType.TEST, order);
        state = new DummyVoyageState(model, GameModeType.TEST, PlayerCount.THREE, players, cards, planche);
        model.setState(state);
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (AbandonedShipCard) factory.getCard(17);
        state.setCard(card);
        cstate = card.getState(state);
        state.setCardState(cstate);
    }

    @Test
    void behaviour() throws ForbiddenCallException, PlayerNotFoundException {
        ServerMessage mess = null;
        assertTrue(0==player1.getCredits());
        assertTrue(0==player2.getCredits());
        assertTrue(0==player3.getCredits());
        planche.printOrder();
        //Non e' il suo turno
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //Non ci riesce
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //Lascia andare
        mess = new SelectLandingMessage(-1);
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //Ci sale giusto
        int x = planche.getPlayerPosition(player2);
        int j = player2.getSpaceShip().getTotalCrew();
        int z = player2.getCredits();
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //Ora toglie la crew in due posti.
        mess = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 2, 2));
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //Ormai e' un'altro stato, non puo farlo.
        ServerMessage w = new SelectLandingMessage(0);
        w.setDescriptor(p3desc);
        assertThrows(ForbiddenCallException.class, ()->state.validate(w));
        //Secondo crewmate
        mess = new RemoveCrewMessage(new ShipCoords(GameModeType.TEST, 3, 2));
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //Ha dovuto saltare anche player3, ma ha guadagnato crediti, ha esaurito la carta.
        assertTrue(j-2== player2.getSpaceShip().getTotalCrew());
        assertTrue(card.getExhausted());
        assertTrue(x-card.getDays()-1==state.getPlanche().getPlayerPosition(player2));
        assertTrue(z+card.getCredits()==player2.getCredits());
        assertTrue(null == state.getCardState(player1));
    }

    @Test
    void disconnectionResilience() throws ForbiddenCallException, PlayerNotFoundException{
        //Si disconnettono due tizi, prima uno in coda, poi il leader
        state.disconnect(player2);
        state.disconnect(player1);
        //giocatore 3 rifiuta, non volendo perdere tempo.
        ServerMessage messa = new SelectLandingMessage(-1);
        messa.setDescriptor(p3desc);
        state.validate(messa);
        //carta conclusa.
        assertTrue(!card.getExhausted());
        assertTrue(null==state.getCardState(player1));
    }

}
