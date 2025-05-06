package it.polimi.ingsw.view;

import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.controller.client.state.TitlesScreenState;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.components.*;

public interface ClientView {
	//Game states
	void show(ClientLobbySelectState state);

	void show(ClientSetupState state);

	void show(ClientWaitingRoomState state);

	void show(ClientConstructionState state);

	void show(ClientVerifyState state);

	void show(ClientVoyageState state);

	void show(ClientEndgameState state);

	//Components
	void show(ClientBaseComponent component);

	void show(ClientPoweredComponentDecorator component);

	void show(ClientShipmentsComponentDecorator component);

	void show(ClientBatteryComponentDecorator component);

	void show(ClientCrewComponentDecorator component);

	void show(ClientBrokenVerifyComponentDecorator component);

	//Card states
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

	//Misc and debug
	void showTextMessage(String message);

    void showTitleScreen(TitlesScreenState titlesScreenState);

}
