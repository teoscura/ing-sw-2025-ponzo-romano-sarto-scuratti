package it.polimi.ingsw.model.adventure_cards;

public class OpenSpaceCard {
    void travel(first_player){
        getPlayerEnginePower();
        movePlayer(first_player, engine_power);
        travel(next_player);
    }
}
