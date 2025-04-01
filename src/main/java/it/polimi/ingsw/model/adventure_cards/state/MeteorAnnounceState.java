package it.polimi.ingsw.model.adventure_cards.state;

import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.message.exceptions.MessageInvalidException;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.adventure_cards.MeteorSwarmCard;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.utils.CardOrder;
import it.polimi.ingsw.model.adventure_cards.utils.ProjectileArray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

public class MeteorAnnounceState extends CardState {

    private final ProjectileArray left;
    private final boolean[] responded;
    private List<PlayerColor> broken_cabins;
    private boolean done = false;

    public MeteorAnnounceState(ModelInstance model, MeteorSwarmCard card, ProjectileArray array){
        super(model);
        if(array==null) throw new NullPointerException();
        this.left = array;
        this.responded = new boolean[model.getCount().getNumber()];
        Arrays.fill(this.responded, false);
    }

    @Override
    public void init() {
        for(Player p : this.model.getOrder(CardOrder.NORMAL)){
            p.getDescriptor().sendMessage(new MeteorMessage(this.left.getProjectiles()[0]));
        }
    }

    @Override
    public void validate(ServerMessage message) throws MessageInvalidException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    protected CardState getNext(){
        if(this.left.getProjectiles().length==1) return null;
        if()
    }
    
}
