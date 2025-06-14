package it.polimi.ingsw.model.client.card;

/**
 * Visitor interface that allows any implementer to distinguish between the various subclasses of {@link ClientCardState}.
 */
public interface ClientCardStateVisitor {

	/**
	 * Displays info regarding a {@link ClientAwaitConfirmDecorator}.
	 * @param state {@link ClientAwaitConfirmDecorator} State to display.
	 */
	void show(ClientAwaitConfirmCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientBaseCardState}.
	 * @param state {@link ClientBaseCardState} State to display.
	 */
	void show(ClientBaseCardState state);
	/**
	 * Displays info regarding a {@link ClientCargoPenaltyCardStateDecorator}.
	 * @param state {@link ClientCargoPenaltyCardStateDecorator} State to display.
	 */
	void show(ClientCargoPenaltyCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientCargoRewardCardStateDecorator}.
	 * @param state {@link ClientCargoRewardCardStateDecorator} State to display.
	 */
	void show(ClientCargoRewardCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientCombatZoneIndexCardStateDecorator}.
	 * @param state {@link ClientCombatZoneIndexCardStateDecorator} State to display.
	 */
	void show(ClientCombatZoneIndexCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientCreditsRewardCardStateDecorator}.
	 * @param state {@link ClientCreditsRewardCardStateDecorator} State to display.
	 */
	void show(ClientCreditsRewardCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientCrewPenaltyCardStateDecorator}.
	 * @param state {@link ClientCrewPenaltyCardStateDecorator} State to display.
	 */
	void show(ClientCrewPenaltyCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientLandingCardStateDecorator}.
	 * @param state {@link ClientLandingCardStateDecorator} State to display.
	 */
	void show(ClientLandingCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientMeteoriteCardStateDecorator}.
	 * @param state {@link ClientMeteoriteCardStateDecorator} State to display.
	 */
	void show(ClientMeteoriteCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientNewCenterCardStateDecorator}.
	 * @param state {@link ClientNewCenterCardStateDecorator} State to display.
	 */
	void show(ClientNewCenterCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientProjectileCardStateDecorator}.
	 * @param state {@link ClientProjectileCardStateDecorator} State to display.
	 */
	void show(ClientProjectileCardStateDecorator state);
	/**
	 * Displays info regarding a {@link ClientEnemyCardStateDecorator}.
	 * @param state {@link ClientEnemyCardStateDecorator} State to display.
	 */
	void show(ClientEnemyCardStateDecorator state);
}
