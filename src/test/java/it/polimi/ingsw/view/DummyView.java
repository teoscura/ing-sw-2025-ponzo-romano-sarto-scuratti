package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.client.state.*;

public class DummyView implements ClientView {

	@Override
	public void show(ClientLobbySelectState state) {
	}

	@Override
	public void show(ClientSetupState state) {
	}

	@Override
	public void show(ClientWaitingRoomState state) {
	}

	@Override
	public void show(ClientConstructionState state) {
	}

	@Override
	public void show(ClientVerifyState state) {
	}

	@Override
	public void show(ClientVoyageState state) {
	}

	@Override
	public void show(ClientEndgameState state) {
	}

	@Override
	public void show(ClientBaseComponent component) {
	}

	@Override
	public void show(ClientPoweredComponentDecorator component) {
	}

	@Override
	public void show(ClientShipmentsComponentDecorator component) {
	}

	@Override
	public void show(ClientBatteryComponentDecorator component) {
	}

	@Override
	public void show(ClientCrewComponentDecorator component) {
	}

	@Override
	public void show(ClientBrokenVerifyComponentDecorator component) {
	}

	@Override
	public void show(ClientAwaitConfirmCardStateDecorator state) {
	}

	@Override
	public void show(ClientBaseCardState state) {
	}

	@Override
	public void show(ClientCargoPenaltyCardStateDecorator state) {
	}

	@Override
	public void show(ClientCargoRewardCardStateDecorator state) {
	}

	@Override
	public void show(ClientCombatZoneIndexCardStateDecorator state) {
	}

	@Override
	public void show(ClientCreditsRewardCardStateDecorator state) {
	}

	@Override
	public void show(ClientCrewPenaltyCardStateDecorator state) {
	}

	@Override
	public void show(ClientLandingCardStateDecorator state) {
	}

	@Override
	public void show(ClientMeteoriteCardStateDecorator state) {
	}

	@Override
	public void show(ClientNewCenterCardStateDecorator state) {
	}

	@Override
	public void show(ClientProjectileCardStateDecorator state) {
	}

	@Override
	public void showTextMessage(String message) {
	}

	@Override
	public void showTitleScreen(TitleScreenState titlesScreenState) {
	}

	@Override
	public void showConnectionScreen(ConnectingState connectingState) {
	}

}
