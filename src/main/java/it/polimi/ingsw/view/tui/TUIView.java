package it.polimi.ingsw.view.tui;

import java.io.IOException;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.model.client.card.*;
import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.view.ClientView;

public class TUIView implements ClientView {

    //private final TerminalWrapper terminal;

    public TUIView() throws IOException {
    //    this.terminal = new TerminalWrapper();
    }

    void printTinyShip(ClientSpaceShip ship){
        StringBuffer s = new StringBuffer();
        //username and stuff.
        //ship.get
        // |----TizioSburrosColNomeLungo1982----|
        // |    <><>  <><>    |EP:00|BP:00|CR:00|2
        // |  <><><><><><><>  |CP:00|RC:00|SC:00|
        // |  <><><><><><><>  |TC:00|YC:00|/---\| 
        // |  <><><><><><><>  |BA:00|GC:00||SHD||
        // |  <><><>  <><><>  |PA:00|BC:00|\---/|
        // |------------------------------------|
    }   

    void printMainShip(ClientSpaceShip ship){
        
    }

    @Override
    public void show(TitleScreenState titlesScreenState) {
        //Setup all utility and underlying threads.
    }

    @Override
    public void show(ConnectingState connectingState) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showConnectionScreen'");
    }

    @Override
    public void show(ClientLobbySelectState state) {
    }

    @Override
    public void show(ClientSetupState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientConstructionState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientVerifyState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientVoyageState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientEndgameState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientBaseComponent component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientPoweredComponentDecorator component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientShipmentsComponentDecorator component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientBatteryComponentDecorator component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCrewComponentDecorator component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientBrokenVerifyComponentDecorator component) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientAwaitConfirmCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientBaseCardState state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCargoPenaltyCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCargoRewardCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCombatZoneIndexCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCreditsRewardCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientCrewPenaltyCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientLandingCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientMeteoriteCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientNewCenterCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void show(ClientProjectileCardStateDecorator state) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public void showTextMessage(String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showTextMessage'");
    }
    
}
