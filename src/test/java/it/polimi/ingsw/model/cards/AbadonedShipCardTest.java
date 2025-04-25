package it.polimi.ingsw.model.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.controller.server.SocketClient;
import it.polimi.ingsw.exceptions.PlayerNotFoundException;
import it.polimi.ingsw.message.server.*;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.AbandonedShipAnnounceState;
import it.polimi.ingsw.model.cards.state.AbandonedShipRewardState;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.components.AlienLifeSupportComponent;
import it.polimi.ingsw.model.components.BatteryComponent;
import it.polimi.ingsw.model.components.CabinComponent;
import it.polimi.ingsw.model.components.CannonComponent;
import it.polimi.ingsw.model.components.EngineComponent;
import it.polimi.ingsw.model.components.ShieldComponent;
import it.polimi.ingsw.model.components.StartingCabinComponent;
import it.polimi.ingsw.model.components.StorageComponent;
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
import it.polimi.ingsw.model.state.VoyageState;

public class AbadonedShipCardTest {
    
    private ModelInstance model;
    private VoyageState state;
    private Planche planche;
    private AbandonedShipCard card;
    private CardState cstate;

    Player player1;
    ClientDescriptor p1desc;
    Player player2;
    ClientDescriptor p2desc;
    Player player_scarso;
    ClientDescriptor psdesc;

    @BeforeEach
    void setUp() throws UnknownHostException, IOException{

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

        ((StartingCabinComponent)player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 3, 2))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
        ((CabinComponent)player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 2, 3))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player_scarso, player2}));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player_scarso}));
        model = new ModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
        TestFlightCards cards = new TestFlightCards();
        planche = new Planche(GameModeType.LVL2, order);
        state = new VoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
        model.setState(state);
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (AbandonedShipCard) factory.getCard(17);
        cstate = card.getState(state);
        state.setCardState(cstate);

        player1.getSpaceShip().updateShip();
        player2.getSpaceShip().updateShip();
        player_scarso.getSpaceShip().updateShip();
    }

    @Test
    void behaviour() throws ForbiddenCallException, PlayerNotFoundException {
        planche.printOrder();
        //giocatore giallo tenta, ma non e' il suo turno
        SelectLandingMessage mess1 = new SelectLandingMessage(0);
        mess1.setDescriptor(psdesc);
        cstate.validate(mess1);
        //Giocatore rosso rifiuta la carta.
        SelectLandingMessage mess2 = new SelectLandingMessage(-1);
        mess2.setDescriptor(p1desc);
        cstate.validate(mess2);
        //Giocatore giallo accetta, ma non puo perche' non ha crew.
        SelectLandingMessage mess3 = new SelectLandingMessage(0);
        mess3.setDescriptor(psdesc);
        cstate.validate(mess3);
        //E corregge di conseguenza.
        SelectLandingMessage mess3again = new SelectLandingMessage(-1);
        mess3again.setDescriptor(psdesc);
        cstate.validate(mess3again);
        //giocatore blue tenta, ed e' buono;
        SelectLandingMessage mess4 = new SelectLandingMessage(0);
        mess4.setDescriptor(p2desc);
        cstate.validate(mess4);
        assertTrue(this.state.getCardState(state.getPlayer(PlayerColor.RED)) instanceof AbandonedShipRewardState);
        cstate = this.state.getCardState(state.getPlayer(PlayerColor.RED));
        //blu tenta un azione proibita.
        ServerMessage messwrong = new TakeRewardMessage(true);
        messwrong.setDescriptor(p2desc);
        assertThrows(ForbiddenCallException.class, () ->cstate.validate(messwrong));
        //Rimuove la crew
        ServerMessage mess5 = new RemoveCrewMessage(new ShipCoords(GameModeType.LVL2, 2, 2));
        mess5.setDescriptor(p2desc);
        cstate.validate(mess5);
        assertThrows(ForbiddenCallException.class, () ->cstate.validate(messwrong));
        assertThrows(ForbiddenCallException.class, () ->cstate.validate(mess4));
        ServerMessage mess6 = new RemoveCrewMessage(new ShipCoords(GameModeType.LVL2, 3, 1));
        mess6.setDescriptor(p2desc);
        cstate.validate(mess6);
        //Finito.
        cstate = this.state.getCardState(player1);
        planche.printOrder();
        System.out.println(cstate.getClass().getSimpleName());
    }

    @Test
    void disconnectionResilience() throws ForbiddenCallException, PlayerNotFoundException{
        //giocatore rosso si disconnette, tocca a scarso
        state.disconnect(player1);
        cstate = this.state.getCardState(player1);
        //giocatore blu si disconnette, ma non era primo, tocca ancora a scarso.
        state.disconnect(player2);
        cstate = this.state.getCardState(player1);
        //giocatore scarso rifiuta, sapendo che non puo entrare
        ServerMessage messa = new SelectLandingMessage(-1);
        messa.setDescriptor(psdesc);
        state.validate(messa);
        cstate = this.state.getCardState(player1);
        //carta conclusa.
        System.out.println(cstate.getClass().getSimpleName());
    }

}
