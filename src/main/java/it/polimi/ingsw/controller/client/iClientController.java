package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.VerifyResult;
import it.polimi.ingsw.model.player.iSpaceShip;

public interface iClientController {
    public void throwException(Exception e);
    public void showCard(int card_id);
    public void updateState(GameState state);
    public void updateShip(PlayerColor c, iSpaceShip ship);
    public void updateShipValidationResult(boolean pass, VerifyResult[] results);

    public void updateTurn(int turn, PlayerColor c);
    public void askTurnOn();
    public void askRemoveCrew(int number);
    public void askRemoveCargo(int number);
    public void askChooseNewCabin();
    public void giveCargo(int[] shipments);
    public void offerReward(int credits, int days_penalty);
    public void offerReward(int[] contains, int days_penalty);
    public void earnCredits(int amount);
    public void askLanding(boolean[] avail_landings, int days);

    public void setLost();
    public void moveOnBoard(int rel_movement);
    public void moveOnBoard(PlayerColor c, int rel_movement);
    public void pauseGame();
    public void endGame();
    public void ping();
    
}
