package it.polimi.ingsw.model.client.card;

public interface ClientCardStateVisitor {

	void show(ClientAwaitConfirmCardStateDecorator state);

	void show(ClientBaseCardState state);

	void show(ClientCargoPenaltyCardStateDecorator state);

	void show(ClientCargoRewardCardStateDecorator state);

	void show(ClientCombatZoneIndexCardStateDecorator state);

	void show(ClientCreditsRewardCardStateDecorator state);

	void show(ClientCrewPenaltyCardStateDecorator state);

	void show(ClientLandingCardStateDecorator state);

	void show(ClientMeteoriteCardStateDecorator state);

	void show(ClientNewCenterCardStateDecorator state);

	void show(ClientProjectileCardStateDecorator state);

	void show(ClientEnemyCardStateDecorator state);
}
