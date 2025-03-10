package it.polimi.ingsw.adventure_cards;

public abstract class Card implements iCard {
    private String title;
    private int cardId;
    private CardEffect effect;

    public String getTitle() {
        return title;
    }
    public int getCardId() {
        return cardId;
    }
    private CardEffect getEffect(){
        return effect;
    }
}
