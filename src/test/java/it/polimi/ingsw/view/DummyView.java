package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.client.state.ConnectingState;
import it.polimi.ingsw.controller.client.state.TitleScreenState;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCargoPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCargoRewardCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCombatZoneIndexCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCreditsRewardCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientCrewPenaltyCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientLandingCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientProjectileCardStateDecorator;
import it.polimi.ingsw.model.client.components.ClientBaseComponent;
import it.polimi.ingsw.model.client.components.ClientBatteryComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientBrokenVerifyComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientCrewComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientPoweredComponentDecorator;
import it.polimi.ingsw.model.client.components.ClientShipmentsComponentDecorator;
import it.polimi.ingsw.model.client.state.ClientConstructionState;
import it.polimi.ingsw.model.client.state.ClientEndgameState;
import it.polimi.ingsw.model.client.state.ClientLobbySelectState;
import it.polimi.ingsw.model.client.state.ClientSetupState;
import it.polimi.ingsw.model.client.state.ClientVerifyState;
import it.polimi.ingsw.model.client.state.ClientVoyageState;
import it.polimi.ingsw.model.client.state.ClientWaitingRoomState;

public class DummyView implements ClientView {

    @Override
    public void show(ClientLobbySelectState state) {
        return;
    }

    @Override
    public void show(ClientSetupState state) {
        return;
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        return;
    }

    @Override
    public void show(ClientConstructionState state) {
        return;
    }

    @Override
    public void show(ClientVerifyState state) {
        return;
    }

    @Override
    public void show(ClientVoyageState state) {
        return;
    }

    @Override
    public void show(ClientEndgameState state) {
        return;
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
        return;
    }

    @Override
    public void showTitleScreen(TitleScreenState titlesScreenState) {
        return;
    }

    @Override
    public void showConnectionScreen(ConnectingState connectingState) {
        return;
    }
    
}
