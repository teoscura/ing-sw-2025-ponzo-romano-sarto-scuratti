package it.polimi.ingsw.model.client.card;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link it.polimi.ingsw.model.client.card.ClientCardState}.
 */
public interface ClientCardStateVisitor {

	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator} State to display.
	 */
	void show(ClientAwaitConfirmCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientBaseCardState}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientBaseCardState} State to display.
	 */
	void show(ClientBaseCardState state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator} State to display.
	 */
	void show(ClientCargoPenaltyCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientCargoRewardCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientCargoRewardCardStateDecorator} State to display.
	 */
	void show(ClientCargoRewardCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator} State to display.
	 */
	void show(ClientCombatZoneIndexCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientCreditsRewardCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientCreditsRewardCardStateDecorator} State to display.
	 */
	void show(ClientCreditsRewardCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator} State to display.
	 */
	void show(ClientCrewPenaltyCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator} State to display.
	 */
	void show(ClientLandingCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator} State to display.
	 */
	void show(ClientMeteoriteCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator} State to display.
	 */
	void show(ClientNewCenterCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator} State to display.
	 */
	void show(ClientProjectileCardStateDecorator state);
	/**
	 * Displays info regarding a {@link it.polimi.ingsw.model.client.card.ClientEnemyCardStateDecorator}.
	 * @param state {@link it.polimi.ingsw.model.client.card.ClientEnemyCardStateDecorator} State to display.
	 */
	void show(ClientEnemyCardStateDecorator state);
}
