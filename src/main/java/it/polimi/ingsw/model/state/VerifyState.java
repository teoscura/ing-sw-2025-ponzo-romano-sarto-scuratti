package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.ClientDisconnectMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.client.components.ClientSpaceShip;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.state.ClientModelState;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.AlienTypeAlreadyPresentException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CrewSetVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.VerifyResult;

public class VerifyState extends GameState {

    private final iCards voyage_deck;
    private final List<Player> to_validate;
    private final List<Player> finish_order;

    public VerifyState(ModelInstance model, GameModeType type, PlayerCount count, List<Player> players, iCards voyage_deck, List<Player> finish_order) {
        super(model, type, count, players);
        if(voyage_deck==null||finish_order==null||players==null) throw new NullPointerException();
        this.voyage_deck = voyage_deck;
        this.to_validate = new ArrayList<>();
        this.to_validate.addAll(this.players);
        this.finish_order = finish_order;
    }

    @Override
    public void init(){
        super.init();
        for(Player p : this.players){
            boolean tmp = p.getSpaceShip().verifyAndClean();
            if(tmp) finish_order.remove(p);
        }
        this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!to_validate.isEmpty()){
            this.broadcastMessage(new NotifyStateUpdateMessage(this.getClientState()));
            return;
        }
        this.transition();
    }

    @Override
    public GameState getNext() {
        Planche planche = new Planche(type, finish_order);
        return new VoyageState(model, type, count, players, voyage_deck, planche);
    }

    @Override 
    public ClientModelState getClientState(){
        List<ClientVerifyPlayer> tmp = new ArrayList<>();
        for(Player p : this.players){
            tmp.add(new ClientVerifyPlayer(p.getUsername(), 
                                           p.getColor(), 
                                           p.getSpaceShip().getClientSpaceShip().getVerifyShip(p.getSpaceShip().verify()),
                                           p.getSpaceShip().verify(),
                                           this.finish_order.contains(p),
                                           this.finish_order.indexOf(p)));
        }
        return new ClientVerifyState(tmp);
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.to_validate.contains(p)){
            System.out.println("Player '"+p.getUsername()+"' already finished validating!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' already finished validating!"));
            return;
        }
        VerifyResult[][] tmp = p.getSpaceShip().verify();
        for(VerifyResult[] row : tmp){
            for(VerifyResult r : row){
                if(r==VerifyResult.BROKEN){
                    System.out.println("Player '"+p.getUsername()+"' attempted to continue when their ship is still broken!");
                    this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to continue when their ship is still broken!"));
                    return;
                }
            }
        }
        this.to_validate.remove(p);
        this.finish_order.addLast(p);
        boolean only_disconnected_left = true;
        for(Player other : this.players){
            if(!other.getDisconnected()&&this.to_validate.contains(other)) only_disconnected_left = false;
        }
        if(only_disconnected_left) {
            for(Player left : this.to_validate){
                left.getSpaceShip().verifyAndClean();
            }
            this.transition();
        }
    }
    
    @Override
    public void removeComponent(Player p, ShipCoords coords) throws ForbiddenCallException {
        if(!this.to_validate.contains(p)){
            System.out.println("Player '"+p.getUsername()+"' attempted to act on the ship after validating!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to act on the ship after validating!"));
            return;
        }
        try{
            p.getSpaceShip().removeComponent(coords);
            p.getSpaceShip().verifyAndClean();
        } catch (IllegalTargetException e){
            System.out.println("Player '"+p.getUsername()+"' tried to remove an empty component!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' tried to remove an empty component!"));
            return;
        }
    }

    @Override
    public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
        if(!this.to_validate.contains(p)){
            System.out.println("Player '"+p.getUsername()+"' tried to set crew type after finishing!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' tried to set crew type after finishing!"));
            return;
        }
        try {
            CrewSetVisitor v = new CrewSetVisitor(p.getSpaceShip(), type);
            p.getSpaceShip().getComponent(coords).check(v);
        } catch (IllegalTargetException e){
            System.out.println("Player '"+p.getUsername()+"' attempted to set crew on a invalid coordinate!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to set crew on a invalid coordinate!"));
            return;
        } catch (IllegalArgumentException e){
            System.out.println("Player '"+p.getUsername()+"' attempted to set crew with an invalid alien type!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to set crew with an invalid alien type!"));
            return;
        } catch (UnsupportedAlienCabinException e){
            System.out.println("Player '"+p.getUsername()+"' attempted to set crew on a cabin that doesn't support the type: '"+type.toString()+"'!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to set crew on a cabin that doesn't support the type: '"+type.toString()+"'!"));
            return;
        } catch (AlienTypeAlreadyPresentException e) {
            System.out.println("Player '"+p.getUsername()+"' attempted to set the type: '"+type.toString()+"' but it's already present, you can only have one!");
            this.broadcastMessage(new ViewMessage("Player '"+p.getUsername()+"' attempted to set the type: '"+type.toString()+"' but it's already present, you can only have one!"));
            return;
        }
    }
    
    @Override
    public void connect(Player p) throws ForbiddenCallException {
        if(p==null) throw new NullPointerException();
        if(!p.getDisconnected()) throw new ForbiddenCallException();
        p.reconnect();
    }

    @Override
    public void disconnect(Player p) throws ForbiddenCallException {
        if(p==null) throw new NullPointerException();
        if(p.getDisconnected()) throw new ForbiddenCallException();
        p.disconnect();
        p.getDescriptor().sendMessage(new ClientDisconnectMessage()); 
    }

}
