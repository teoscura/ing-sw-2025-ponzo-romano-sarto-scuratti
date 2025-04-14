package it.polimi.ingsw.model.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.message.client.DisconnectMessage;
import it.polimi.ingsw.message.client.NotifyPlayerUpdateMessage;
import it.polimi.ingsw.message.client.NotifyStateUpdateMessage;
import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.components.enums.AlienType;
import it.polimi.ingsw.model.components.exceptions.AlienTypeAlreadyPresentException;
import it.polimi.ingsw.model.components.exceptions.IllegalTargetException;
import it.polimi.ingsw.model.components.exceptions.UnsupportedAlienCabinException;
import it.polimi.ingsw.model.components.visitors.CrewSetVisitor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.player.VerifyResult;

public class ValidationState extends GameState {

    private final iCards voyage_deck;
    private final List<Player> to_validate;
    private final List<Player> finish_order;

    public ValidationState(ModelInstance model, GameModeType type, PlayerCount count, List<Player> players, iCards voyage_deck, List<Player> finish_order) {
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
            p.getSpaceShip().verifyAndClean();
        }
        aaaaaa //tenere l'ordine, ma se uno deve verificare toglilo e mettilo appena finisce di sistemare.
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        message.receive(this);
        if(!to_validate.isEmpty()) return;
        this.transition();
    }

    @Override
    public GameState getNext() {
        Planche planche = new Planche(type, finish_order);
        return new VoyageState(model, type, count, players, voyage_deck, planche);
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.to_validate.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already finished validating your ship!"));
            return;
        }
        VerifyResult[][] tmp = p.getSpaceShip().verify();
        for(VerifyResult[] row : tmp){
            for(VerifyResult r : row){
                if(r==VerifyResult.BROKEN){
                    p.getDescriptor().sendMessage(new ViewMessage("You need to fix your ship!"));
                    return;
                }
            }
        }
        this.to_validate.remove(p);
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
            p.getDescriptor().sendMessage(new ViewMessage("You already finished validating your ship!"));
            return;
        }
        try{
            p.getSpaceShip().removeComponent(coords);
            p.getSpaceShip().verifyAndClean();
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords correspond to a empty component!"));
            return;
        }
    }

    @Override
    public void setCrewType(Player p, ShipCoords coords, AlienType type) throws ForbiddenCallException {
        if(!this.to_validate.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already finished validating your ship!"));
            return;
        }
        try {
            CrewSetVisitor v = new CrewSetVisitor(p.getSpaceShip(), type);
            p.getSpaceShip().getComponent(coords).check(v);
        } catch (IllegalTargetException e){
            p.getDescriptor().sendMessage(new ViewMessage("Coords aren't a cabin component!"));
            return;
        } catch (IllegalArgumentException e){
            p.getDescriptor().sendMessage(new ViewMessage("AlienType is not a valid type!"));
            return;
        } catch (UnsupportedAlienCabinException e){
            p.getDescriptor().sendMessage(new ViewMessage("Selected cabin doesn't support the intended alien type!"));
            return;
        } catch (AlienTypeAlreadyPresentException e) {
            p.getDescriptor().sendMessage(new ViewMessage("This ship already contains this alien type!"));
            return;
        }
        for(Player tmp : this.players){
            tmp.getDescriptor().sendMessage(new NotifyPlayerUpdateMessage(p.getColor()));
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
