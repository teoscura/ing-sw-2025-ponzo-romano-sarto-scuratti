package it.polimi.ingsw.model.state;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.LevelOneCardFactory;
import it.polimi.ingsw.model.adventure_cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.CommonBoard;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;

public class ConstructionState extends GameState {

    private final iCommonBoard board;

    private final int[] construction_cards;
    private final iCards voyage_deck;
    private final List<Player> building;
    private final List<Player> finished;

    public ConstructionState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players) {
        super(model, type, count, players);
        this.board = new CommonBoard();
        this.voyage_deck = type.getLevel()==-1 ? new TestFlightCards() : new LevelTwoCards(); 
        this.construction_cards = this.voyage_deck.getConstructionCards();
        this.finished = new ArrayList<>();
        this.building = new ArrayList<>();
        this.building.addAll(Arrays.asList(this.players));
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        //XXX add that timer finished, and that everyone who was ready beforehand did choose to continue and not flip it.
    }

    @Override
    public GameState getNext() {
        return new ValidationState(model, type, count, players, voyage_deck, finished);
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        this.building.remove(p);
        this.finished.addLast(p);
        /*XXX timer logic. */
    }

    @Override
    public void putComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(this.reserved_components.get(p.getColor()).getFirst()==null){
            p.getDescriptor().sendMessage(new ViewMessage("You need to pick up a component!"));
            return;
        }
        try{
            p.getSpaceShip().addComponent(this.reserved_components.get(p.getColor()).getFirst(), coords);
            this.reserved_components.get(p.getColor()).removeFirst();
        } catch (OutOfBoundsException e) {
            p.getDescriptor().sendMessage(new ViewMessage("Invalid placement coordinates!"));
            return;
        } catch (IllegalComponentAdd e) {
            p.getDescriptor().sendMessage(new ViewMessage("Space is already occupied by a component!"));
            return;
        } catch (IllegalTargetException e) {
            p.getDescriptor().sendMessage(new ViewMessage("Components need to be next to the rest of the ship!"));
            return;
        }
    }

    @Override
    public void takeComponent(Player p) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(this.reserved_components.get(p.getColor()).size()>3){
            this.reserved_components.get(p.getColor()).addFirst(this.board.pullComponent());
            this.board.discardComponent(this.reserved_components.get(p.getColor()).removeLast());
            p.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
        else{
            this.reserved_components.get(p.getColor()).addFirst(this.board.pullComponent());
            p.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
    }

    //XXX rework to use component ids so that most race conditions could be avoided.
    @Override
    public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        try{
            this.reserved_components.get(p.getColor()).addFirst(this.board.pullDiscarded(id));
        } catch (ContainerEmptyException e){
            p.getDescriptor().sendMessage(new ViewMessage("There are no components in the discarded pile!"));
            return;
        } catch (OutOfBoundsException e) {
            p.getDescriptor().sendMessage(new ViewMessage("The discarded pile does not contain that id!"));
            return;
        }
    }

    @Override
    public void discardComponent(Player p, int id) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        //TODO
    }

    @Override
    public void toggleHourglass(Player p) throws ForbiddenCallException {
        if(this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You haven't finished building your ship, you can't toggle the hourglass!"));
            return;
        }
        //XXX
    }
}