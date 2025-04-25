package it.polimi.ingsw.model.cards;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.SelectLandingMessage;
import it.polimi.ingsw.message.server.SendContinueMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.message.server.TakeCargoMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.AbandonedShipCard;
import it.polimi.ingsw.model.cards.LevelOneCardFactory;
import it.polimi.ingsw.model.cards.PlanetCard;
import it.polimi.ingsw.model.cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.cards.state.CardState;
import it.polimi.ingsw.model.cards.utils.CardOrder;
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
import it.polimi.ingsw.model.components.enums.ShipmentType;
import it.polimi.ingsw.model.components.enums.StorageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;

public class PlanetCardTest {
    
    private ModelInstance model;
    private VoyageState state;
    private Planche planche;
    private PlanetCard card;
    private CardState cstate;

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
        psdesc = new ClientDescriptor(player_scarso.getUsername(), null);
        psdesc.bindPlayer(player_scarso);

        ((StartingCabinComponent)player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 3, 2))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
        ((CabinComponent)player_scarso.getSpaceShip().getComponent(new ShipCoords(GameModeType.LVL2, 2, 3))).setCrew(player_scarso.getSpaceShip(), 0, AlienType.HUMAN);
        ArrayList<Player> order = new ArrayList<>(Arrays.asList(new Player[]{player1, player_scarso, player2}));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(new Player[]{player1, player2, player_scarso}));
        model = new ModelInstance(1, null, GameModeType.LVL2, PlayerCount.THREE);
        LevelTwoCards cards = new LevelTwoCards();
        planche = new Planche(GameModeType.LVL2, order);
        state = new VoyageState(model, GameModeType.LVL2, PlayerCount.THREE, players, cards, planche);
        model.setState(state);
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (PlanetCard) factory.getCard(14);
        cstate = card.getState(state);
        state.setCardState(cstate);

        player1.getSpaceShip().updateShip();
        player2.getSpaceShip().updateShip();
        player_scarso.getSpaceShip().updateShip();
    }

    @Test
    void behaviour() throws ForbiddenCallException {

        ServerMessage mess = null;
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
        //Attempt sbagliato
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //Giocatore 1 scende sul pianeta 0
        mess = new SelectLandingMessage(-1);
        mess.setDescriptor(p1desc);
        state.validate(mess);
        //Giocatore 1 scende sul pianeta 0
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore 1 sposta qualcosa che non c'e';
        mess = new TakeCargoMessage(new ShipCoords(GameModeType.LVL2, 4, 2), ShipmentType.RED);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore 1 sposta qualcosa che c'e';
        mess = new TakeCargoMessage(new ShipCoords(GameModeType.LVL2, 4, 2), ShipmentType.BLUE);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore 1 sposta qualcosa che c'e';
        mess = new TakeCargoMessage(new ShipCoords(GameModeType.LVL2, 4, 2), ShipmentType.BLUE);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore 1 si e' rotto;
        mess = new SendContinueMessage();
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //controllo che stato e'
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
        //Giocatore scarso tenta di atterrare gia occupato
        mess = new SelectLandingMessage(0);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore scarso tenta di atterrare sul 2
        mess = new SelectLandingMessage(1);
        mess.setDescriptor(psdesc);
        state.validate(mess);
        //Giocatore scarso si e' rotto;
        mess = new SelectLandingMessage(-1);
        mess.setDescriptor(p2desc);
        state.validate(mess);
        //carta conclusa.
        cstate = this.state.getCardState(player1);
        System.out.println(cstate.getClass().getSimpleName());
    }

    @Test
    void disconnectionResilience() throws ForbiddenCallException {
        ServerMessage mess = null;
        //Player 1 disconnects, player 2 disconnects right after, leaving player_scarso.
        state.disconnect(player1);
        cstate = this.state.getCardState(player1);
        state.disconnect(player2);
        cstate = this.state.getCardState(player1);
        //Scarso dodges.
        mess = new SelectLandingMessage(-1);
        mess.setDescriptor(psdesc);
        state.validate(mess);
    }

}
