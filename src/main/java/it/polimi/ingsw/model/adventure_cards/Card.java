package it.polimi.ingsw.model.adventure_cards;


public abstract class Card implements iCard {
    private CardType type;
    private int card_Id;
    
    public CardType getType() {
        return type;
    }
    public int getCardId() {
        return card_Id;
    }

    //TODO: add general methods
}
