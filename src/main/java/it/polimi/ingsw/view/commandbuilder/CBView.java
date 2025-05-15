package it.polimi.ingsw.view.commandbuilder;

import it.polimi.ingsw.controller.client.ClientController;
import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;

public class CBView implements ClientView {

    private ClientController cc;
    private Thread cbt, ct, tst;

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
    public void show(ClientBaseComponent component) {
        return;
    }

    @Override
    public void show(ClientPoweredComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientShipmentsComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientBatteryComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientCrewComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientBrokenVerifyComponentDecorator component) {
        return;
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientBaseCardState state) {
        return;
    }

    @Override
    public void show(ClientCargoPenaltyCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientCreditsRewardCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientCrewPenaltyCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        return;
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        return;
    }

    @Override
    public void showTextMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showTitleScreen(TitleScreenState state) {
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
    public void showConnectionScreen(ConnectingState state) {
        try {
            System.out.println("Connection screen");
            this.ct = new ConnectTask(state);
            this.ct.start();
            this.ct.join();
            this.cc = state.getController();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show(ClientLobbySelectState state) {
        System.out.println("Connected!");
        this.cbt = new InputCommandTask(cc);
        this.cbt.start();
    }

    
}
