package it.polimi.ingsw.view;

import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.client.card.*;

public interface ClientView {
    //Game states
    public void show(ClientWaitingRoomState state);
    public void show(ClientConstructionState state);
    public void show(ClientVerifyState state);
    public void show(ClientVoyageState state);
    public void show(ClientEndgameState state);
    //Card states
    public void show(ClientAwaitConfirmCardStateDecorator state);
    public void show(ClientBaseCardState state);
    public void show(ClientCargoPenaltyCardStateDecorator state);
    public void show(ClientCargoRewardCardStateDecorator state);
    public void show(ClientCombatZoneIndexCardStateDecorator state);
    public void show(ClientCreditsRewardCardStateDecorator state);
    public void show(ClientCrewPenaltyCardStateDecorator state);
    public void show(ClientLandingCardStateDecorator state);
    public void show(ClientMeteoriteCardStateDecorator state);
    public void show(ClientNewCenterCardStateDecorator state);
    public void show(ClientProjectileCardStateDecorator state);
}
