package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.exceptions.OutOfBoundsException;
import it.polimi.ingsw.message.client.DisconnectMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.CommonBoard;
import it.polimi.ingsw.model.board.LevelTwoCards;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.components.iBaseComponent;
import it.polimi.ingsw.model.components.exceptions.ContainerEmptyException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.exceptions.IllegalComponentAdd;

public class ConstructionState extends GameState {

    private final iCommonBoard board;

    private final List<Integer> construction_cards;
    private final iCards voyage_deck;
    private final List<Player> building;
    private final List<Player> finished;
    private final ConstructionStateHourglass hourglass;
    private HashMap<Player, iBaseComponent> current_tile;
    private HashMap<Player, List<iBaseComponent>> hoarded_tile;

    public ConstructionState(ModelInstance model, GameModeType type, PlayerCount count, List<Player> players) {
        super(model, type, count, players);
        this.board = new CommonBoard();
        this.voyage_deck = type.getLevel()==-1 ? new TestFlightCards() : new LevelTwoCards(); 
        this.hourglass = type.getLevel()==-1 ? new ConstructionStateHourglass(60, 0) : new ConstructionStateHourglass(60, 4);
        this.construction_cards = this.voyage_deck.getConstructionCards();
        this.finished = new ArrayList<>();
        this.building = new ArrayList<>();
        this.building.addAll(this.players);
        this.current_tile = new HashMap<>();
        this.hoarded_tile = new HashMap<>();
        for(Player p : this.players){
            this.current_tile.put(p, null);
            this.hoarded_tile.put(p, new ArrayList<>());
        }
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(this.finished.size()!=this.players.size()) return;
        this.transition();
    }

    @Override
    public GameState getNext() {
        return new VerifyState(model, type, count, players, voyage_deck, finished);
    }

    @Override
    public ClientModelState getClientState(){
        List<ClientConstructionPlayer> tmp = new ArrayList<>();
        for(Player p : this.players){
            List<Integer> stash = new ArrayList<>();
            if(this.current_tile.get(p)!=null) stash.add(this.current_tile.get(p).getID());
            stash.addAll(this.hoarded_tile.get(p).stream().filter(t->t!=null).map(t->t.getID()).toList());
            tmp.add(new ClientConstructionPlayer(p.getUsername(),
                                                 p.getColor(),
                                                 p.getSpaceShip().getClientSpaceShip(),
                                                 stash,
                                                 this.finished.contains(p)));
        }
        return new ClientConstructionState(this.type, tmp, this.construction_cards, this.board.getDiscarded(), this.board.getCoveredSize(), this.hourglass.getInstant());
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        this.building.remove(p);
        this.finished.addLast(p);
        boolean only_disconnected_left = true;
        for(Player other : this.players){
            if(!other.getDisconnected()&&this.building.contains(other)) only_disconnected_left = false;
        }
        if(!this.hourglass.started()){
            this.hourglass.enable();
            this.hourglass.toggle();
        }
        if(only_disconnected_left) this.transition();
    }

    @Override
    public void putComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(!this.hourglass.running()){
            p.getDescriptor().sendMessage(new ViewMessage("Hourglass has run out!"));
            return;
        }
        if(this.current_tile.get(p)==null){
            p.getDescriptor().sendMessage(new ViewMessage("You need to pick up a component!"));
            return;
        }
        try{
            p.getSpaceShip().addComponent(this.current_tile.get(p), coords);
            this.current_tile = null;
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
        if(!this.hourglass.running()){
            p.getDescriptor().sendMessage(new ViewMessage("Hourglass has run out!"));
            return;
        }
        iBaseComponent tmp = this.board.pullComponent();
        if(tmp==null){
            p.getDescriptor().sendMessage(new ViewMessage("Board is now empty!"));
            return;
        }
        for(Player broadcast : this.building){
            broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
        if(this.current_tile.get(p)==null){
            this.current_tile.put(p,tmp);
            p.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
        else {
            iBaseComponent old_current = this.current_tile.get(p);
            this.current_tile.put(p, tmp);
            this.hoarded_tile.get(p).addFirst(old_current);
            while (this.hoarded_tile.get(p).size()>=3){
                this.board.discardComponent(this.hoarded_tile.get(p).removeLast());
            }
            for(Player broadcast : this.building){
                broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
            }
        }
    }

    @Override
    public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(!this.hourglass.running()){
            p.getDescriptor().sendMessage(new ViewMessage("Hourglass has run out!"));
            return;
        }
        iBaseComponent tmp = null;
        try{
            tmp = this.board.pullDiscarded(id);
        } catch (ContainerEmptyException e){
            p.getDescriptor().sendMessage(new ViewMessage("There is no tile with that id in the discarded ones!"));
            return;
        }
        for(Player broadcast : this.building){
            broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
        this.hoarded_tile.get(p).addFirst(tmp);
        while (this.hoarded_tile.get(p).size()>=3){
            this.board.discardComponent(this.hoarded_tile.get(p).removeLast());
        }
        for(Player broadcast : this.building){
            broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
        }
    }

    @Override
    public void discardComponent(Player p, int id) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(!this.hourglass.running()){
            p.getDescriptor().sendMessage(new ViewMessage("Hourglass has run out!"));
            return;
        }
        if(this.current_tile.get(p).getID()==id){
            iBaseComponent tmp = this.current_tile.get(p);
            this.current_tile.put(p, null);
            this.board.discardComponent(tmp);
            for(Player broadcast : this.building){
                broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
            }
            return;
        }
        boolean found = false;
        int index = -1;
        int i = 0;
        for(iBaseComponent c : this.hoarded_tile.get(p)){
            if(c.getID()==id) {
                found = true;
                index = i;
            }
            i++;
        }
        if(found && index>=0 ){
            this.board.discardComponent(this.hoarded_tile.get(p).remove(index));
            for(Player broadcast : this.building){
                broadcast.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
            }
            return;
        }
        else{
            p.getDescriptor().sendMessage(new ViewMessage("Tried to discard a component that you dont own!"));
            return;
        }
    }

    @Override
    public void toggleHourglass(Player p) throws ForbiddenCallException {
        if(this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You haven't finished building your ship, you can't toggle the hourglass!"));
            return;
        }
        try{
            this.hourglass.toggle();
        } catch (ForbiddenCallException e){
            p.getDescriptor().sendMessage(new ViewMessage("Hourglass wasn't started, or has run out, can't toggle it!"));
            return;
        }
    }

    @Override
    public void connect(Player p) throws ForbiddenCallException {
        if(p==null) throw new NullPointerException();
        if(!p.getDisconnected()) throw new ForbiddenCallException();
        p.reconnect();
        p.getDescriptor().sendMessage(new NotifyStateUpdateMessage());
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(p==null) throw new NullPointerException();
        if(p.getDisconnected()) throw new ForbiddenCallException();
        p.disconnect();
        p.getDescriptor().sendMessage(new DisconnectMessage()); 
    }

}