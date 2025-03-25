package it.polimi.ingsw.model.adventure_cards;

import java.util.HashMap;

public class LevelTwoCardFactory implements iCardFactory{
    
    private HashMap<Integer, iCard> cards;

    @Override
    public iCard getCard(int id) {
        if(this.cards.containsKey(id)) throw new IllegalArgumentException("Non valid card id.");
        return this.cards.get(id);
    }

    //FIXME
    public LevelTwoCardFactory(){
        this.cards = new HashMap<Integer, iCard>(){{
            put(1, ASD
            );
            put(2, ASD
            );
            put(3, ASD
            );
            put(4, ASD
            );
            put(5, ASD
            );
            put(6, ASD
            );
            put(7, ASD
            );
            put(8, ASD
            );
            put(9, ASD
            );
            put(10, ASD
            );
            put(11, ASD
            );
            put(12, ASD
            );
            put(13, ASD
            );
            put(14, ASD
            );
            put(15, ASD
            );
            put(16, ASD
            );
            put(17, ASD
            );
            put(18, ASD
            );
            put(19, ASD
            );
            put(20, ASD
            );
        }};
    }
}
