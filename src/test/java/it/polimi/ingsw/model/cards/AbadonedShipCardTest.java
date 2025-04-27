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

        player1 = new Player(GameModeType.LVL2, "p1", PlayerColor.RED);
        //li ha giusti giusti
        
        p1desc = new ClientDescriptor(player1.getUsername(), null);
        p1desc.bindPlayer(player1);

        player2 = new Player(GameModeType.LVL2, "p2", PlayerColor.BLUE);
        //Uguale a p3 ma con due crewmate in meno, abbastanza per non salire
        p2desc = new ClientDescriptor(player2.getUsername(), null);
        p2desc.bindPlayer(player2);

        player3 = new Player(GameModeType.LVL2, "p3", PlayerColor.GREEN);
        //Uguale a p2 ma puo salire
        p3desc = new ClientDescriptor(player3.getUsername(), null);
        p3desc.bindPlayer(player3);

        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player3}));
        model = new DummyModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
        TestFlightCards cards = new TestFlightCards();
        planche = new Planche(GameModeType.LVL2, order);
        state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
        model.setState(state);
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (AbandonedShipCard) factory.getCard(17);
        state.setCard(card);
        cstate = card.getState(state);
        state.setCardState(cstate);
    }

    @Test
    void behaviour() throws ForbiddenCallException, PlayerNotFoundException {

    }

    @Test
    void selfInflictedRetire() throws ForbiddenCallException, PlayerNotFoundException {

    }

    @Test
    void disconnectionResilience() throws ForbiddenCallException, PlayerNotFoundException{
        state.disconnect(player2);
        state.disconnect(player1);
        //giocatore scarso rifiuta, sapendo che non puo entrare
        ServerMessage messa = new SelectLandingMessage(-1);
        messa.setDescriptor(p3desc);
        state.validate(messa);
        //carta conclusa.
        assertTrue(!card.getExhausted());
        assertTrue(null==state.getCardState(player1));
    }

}
