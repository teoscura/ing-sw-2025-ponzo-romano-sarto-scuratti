//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.controller.server.state.GameState;
import it.polimi.ingsw.message.server.ServerMessage;

public interface iCard {
    public int getId();

    public GameState getState();
    public void validateResponse(ServerMessage response);
    public boolean getExhausted();
}