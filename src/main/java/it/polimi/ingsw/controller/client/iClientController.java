package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.match.state.GameState;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.VerifyResult;
import it.polimi.ingsw.model.player.iSpaceShip;

public interface iClientController {
    public void throwException(Exception e);
    public void updateTurn(int turn_number, PlayerColor c);
    public void showCard(int card_id);
    public void updatePhase(GameState state);
    public void updateShip(PlayerColor c, iSpaceShip ship);
    public void updateShipValidationResult(boolean validation, VerifyResult[] results);
    public void moveOnBoard(PlayerColor c, int rel_change);
    public void pauseGame();
    public void endGame();
    public void ping();
}
