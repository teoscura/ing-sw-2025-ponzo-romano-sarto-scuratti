package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.model.DummyModelInstance;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.DummyVoyageState;

public class MeteorSwarmCardTest {
    
    private DummyModelInstance model;
	private DummyVoyageState state;
	private TestFlightCards cards;
	private Planche planche;
	private MeteorSwarmCard card;

	Player player1;
	ClientDescriptor p1desc;
	Player player2;
	ClientDescriptor p2desc;
	Player player3;
	ClientDescriptor p3desc;
	ArrayList<Player> order, players;

    @BeforeEach
    void setUp(){
        //Scudi parano piccole/navi lisce pure.
        //Cannoni parano grossi.
        //P1 puo vincere ma non accende, e perde di conseguenza.

        iBaseComponent c = null;
        ComponentFactory f1 = new ComponentFactory();
        ComponentFactory f2 = new ComponentFactory();

        //cannon on left, smooth in front.
        player1 = new Player(GameModeType.TEST, "p1", PlayerColor.RED);
		p1desc = new ClientDescriptor(player1.getUsername(), null);
		p1desc.bindPlayer(player1);

        //got shield covering front and left, and is smooth on center block right
		player2 = new Player(GameModeType.TEST, "p2", PlayerColor.RED);
		p2desc = new ClientDescriptor(player2.getUsername(), null);
		p2desc.bindPlayer(player2);

        
		order = new ArrayList<>(Arrays.asList(player1, player2));
		players = new ArrayList<>(Arrays.asList(player1, player2));
		model = new DummyModelInstance(1, null, GameModeType.TEST, PlayerCount.TWO);
		planche = new Planche(GameModeType.LVL2, order);
		cards = new TestFlightCards();
		state = new DummyVoyageState(model, GameModeType.LVL2, PlayerCount.TWO, players, cards, planche);
    }

    @Test
    void behaviour1(){
        //3 meteorites, first one big, front left right.
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (MeteorSwarmCard) factory.getCard(9);
		model.setState(state);
		state.setCard(card);
        
		state.setCardState(card.getState(state));
        

    }

    @Test
    void behaviour2(){
        //5 meteorites, 3rd one big, front front, left left left.
        LevelOneCardFactory factory = new LevelOneCardFactory();
        card = (MeteorSwarmCard) factory.getCard(109);
		model.setState(state);
		state.setCard(card);
		state.setCardState(card.getState(state));
    }
}
