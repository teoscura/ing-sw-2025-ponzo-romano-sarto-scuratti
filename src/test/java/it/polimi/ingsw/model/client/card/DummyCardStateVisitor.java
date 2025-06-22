package it.polimi.ingsw.model.client.card;

public class DummyCardStateVisitor implements ClientCardStateVisitor {

    private int i = 0;

    public int visited(){
        return i;
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientBaseCardState state) {
        i++;
    }

    @Override
    public void show(ClientCargoPenaltyCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientCreditsRewardCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientCrewPenaltyCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        i++;
    }

    @Override
    public void show(ClientEnemyCardStateDecorator state) {
        i++;
    }
    
}
