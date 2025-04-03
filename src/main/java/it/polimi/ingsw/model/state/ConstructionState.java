package it.polimi.ingsw.model.state;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.message.client.ViewMessage;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.adventure_cards.LevelOneCardFactory;
import it.polimi.ingsw.model.adventure_cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.adventure_cards.iCard;
import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;
import it.polimi.ingsw.model.board.Cards;
import it.polimi.ingsw.model.board.CommonBoard;
import it.polimi.ingsw.model.board.iCards;
import it.polimi.ingsw.model.board.iCommonBoard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;

public class ConstructionState extends GameState {

    private final iCommonBoard board;
    private final int[] construction_cards;
    private final iCards voyage_deck;
    private final List<Player> building;
    private final HashMap<PlayerColor, List<Integer>> reserved_components;

    public ConstructionState(ModelInstance model, GameModeType type, PlayerCount count, Player[] players) {
        super(model, type, count, players);
        this.board = new CommonBoard();
        LevelOneCardFactory l1 = new LevelOneCardFactory();
        LevelTwoCardFactory l2 = new LevelTwoCardFactory();
        if(type.getLevel()==-1){
            ArrayList<iCard> test_cards = new ArrayList<>(){{
                add(l1.getCard(2));
                add(l1.getCard(4));
                add(l1.getCard(5));
                add(l1.getCard(9));
                add(l1.getCard(13));
                add(l1.getCard(16));
                add(l1.getCard(18));
                add(l1.getCard(19));
            }};
            Collections.shuffle(test_cards);
            this.construction_cards = null;
            this.voyage_deck = new Cards(test_cards);
        }
        else{
            ArrayList<iCard> lv1_cards = new ArrayList<>(){{
                for(int i=1;i<=20;i++) add(l1.getCard(i));
            }};
            ArrayList<iCard> lv2_cards = new ArrayList<>(){{
                for(int i=101;i<=120;i++) add(l2.getCard(i));
            }};
            Collections.shuffle(lv1_cards);
            ArrayDeque<iCard> lv1_queue = new ArrayDeque<>(lv1_cards);
            Collections.shuffle(lv2_cards);
            ArrayDeque<iCard> lv2_queue = new ArrayDeque<>(lv2_cards);
            ArrayList<iCard> tmp = new ArrayList<>(){{
                for(int i=0;i<4;i++){
                    add(lv2_queue.poll());
                    add(lv2_queue.poll());
                    add(lv1_queue.poll());
                }
            }};
            while(tmp.getFirst().getId()<100){
                iCard shuffled = tmp.removeFirst();
                tmp.addLast(shuffled);
            }
            this.construction_cards = tmp.stream().mapToInt((c)->c.getId()).toArray();
            this.voyage_deck = new Cards(tmp);
        }
        this.reserved_components = new HashMap<>();
        this.building = new ArrayList<>();
        this.building.addAll(Arrays.asList(this.players));
        for(PlayerColor c : PlayerColor.values()){
            if(c.getOrder()<0||c.getOrder()>=this.count.getNumber()) continue;
            this.reserved_components.put(c, new ArrayList<>());
        }
    }

    @Override
    public void init(){
        super.init();
        //XXX do a first verify run and send it to everyone.
    }

    @Override
    public void validate(ServerMessage message) throws ForbiddenCallException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public GameState getNext() {
        return new ValidationState(model, type, count, players, voyage_deck);
    }

    @Override
    public void sendContinue(Player p) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        this.building.remove(p);
        /*XXX timer logic. */
    }

    @Override
    public void putComponent(Player p, ShipCoords coords, int id) throws ForbiddenCallException {
        if(!this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You already confirmed your actions, can't do anything else!"));
            return;
        }
        if(this.reserved_components())
    }

    @Override
    public void takeComponent(Player p) throws ForbiddenCallException {
        x
    }

    @Override
    public void takeDiscarded(Player p, int id) throws ForbiddenCallException {
        x
    }

    @Override
    public void discardComponent(Player p, int id) throws ForbiddenCallException {
        x
    }

    @Override
    public void toggleHourglass(Player p) throws ForbiddenCallException {
        if(this.building.contains(p)){
            p.getDescriptor().sendMessage(new ViewMessage("You haven't finished building your ship, you can't toggle the hourglass!"));
            return;
        }
    }
}