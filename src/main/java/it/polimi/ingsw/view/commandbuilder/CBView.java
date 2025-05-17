package it.polimi.ingsw.view.commandbuilder;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;

public class CBView implements ClientView {

	private Thread ct, tst;

	@Override
	public void show(TitleScreenState state) {
		try {
			this.tst = new TitleScreenTask(state);
			System.out.println("Title screen");
			this.tst.start();
			this.tst.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show(ConnectingState state) {
		try {
			System.out.println("Connection screen");
			this.ct = new ConnectTask(state);
			this.ct.start();
			this.ct.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show(ClientLobbySelectState state) {
		System.out.println("Connected!");
	}

	@Override
	public void show(ClientSetupState state) {
		System.out.println("Lobby setup state");
	}

	@Override
	public void show(ClientWaitingRoomState state) {
		System.out.println("Waiting room state");
	}

	@Override
	public void show(ClientConstructionState state) {
		System.out.println("Construction state");
	}

	@Override
	public void show(ClientVerifyState state) {
		System.out.println("Verify state");
	}

	@Override
	public void show(ClientVoyageState state) {
		System.out.println("Voyage state");
	}

	@Override
	public void show(ClientEndgameState state) {
		System.out.println("Endgame state");
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
		System.out.println(message);
	}

}
