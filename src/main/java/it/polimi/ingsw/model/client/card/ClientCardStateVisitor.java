package it.polimi.ingsw.model.client.card;

public interface ClientCardStateVisitor {

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
