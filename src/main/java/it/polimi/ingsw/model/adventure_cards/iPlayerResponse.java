//Done.
package it.polimi.ingsw.model.adventure_cards;

import it.polimi.ingsw.model.player.ShipCoords;

public interface iPlayerResponse {
    public ShipCoords[] getCoordArray();
    public int[] getMerchChoices();
    public boolean getAccept();
    public int getId();
}
