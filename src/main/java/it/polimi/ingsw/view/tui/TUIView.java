package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.controller.server.ClientDescriptor;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.GameModeType;
import it.polimi.ingsw.model.ModelInstance;
import it.polimi.ingsw.model.PlayerCount;
import it.polimi.ingsw.model.board.Planche;
import it.polimi.ingsw.model.board.TestFlightCards;
import it.polimi.ingsw.model.cards.LevelTwoCardFactory;
import it.polimi.ingsw.model.cards.state.PiratesAnnounceState;
import it.polimi.ingsw.model.cards.utils.Projectile;
import it.polimi.ingsw.model.cards.utils.ProjectileDimension;
import it.polimi.ingsw.model.cards.utils.ProjectileDirection;
import it.polimi.ingsw.model.cards.visitors.CrewRemoveVisitor;
import it.polimi.ingsw.model.client.card.ClientAwaitConfirmCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientBaseCardState;
import it.polimi.ingsw.model.client.card.ClientCardState;
import it.polimi.ingsw.model.client.card.ClientMeteoriteCardStateDecorator;
import it.polimi.ingsw.model.client.card.ClientNewCenterCardStateDecorator;
import it.polimi.ingsw.model.client.player.ClientConstructionPlayer;
import it.polimi.ingsw.model.client.player.ClientEndgamePlayer;
import it.polimi.ingsw.model.client.player.ClientVerifyPlayer;
import it.polimi.ingsw.model.client.player.ClientVoyagePlayer;
import it.polimi.ingsw.model.client.player.ClientWaitingPlayer;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.components.BaseComponent;
import it.polimi.ingsw.model.components.ComponentFactory;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.player.ShipCoords;
import it.polimi.ingsw.model.state.VoyageState;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.formatters.*;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private final Object input_lock;
    private final Object state_lock;
    private PlayerColor selected_color;
    private ClientState client_state;
    private ServerMessage input;
    private Thread inputthread;
    private ConnectedState state;

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper();
        this.input_lock = new Object();
        this.state_lock = new Object();
    }

    @Override
    public void show(TitleScreenState state) {
        if(this.inputthread!=null) this.inputthread.interrupt();
        this.inputthread = new TitleScreenThread(this.terminal, state);
        this.inputthread.start();
    }

    @Override
    public void show(ConnectingState state) {
        this.inputthread.interrupt();
        this.inputthread = new ConnectingThread(this.terminal, state);
        this.inputthread.start();
    }

    @Override
    public void show(ClientLobbySelectState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientSetupState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        ClientWaitingStateFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientConstructionState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientConstructionStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVerifyState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientVerifyStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVoyageState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientVoyageStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientEndgameState state) {
        if(state == null) throw new UnsupportedOperationException();
        ClientEndingStateFormatter.format(terminal, state);
    }

    @Override
    public void showTextMessage(String message) {
        if(state == null) throw new UnsupportedOperationException();
        ClientTextMessageFormatter.format(terminal, message);
    }

    public ClientState getClientState(){
        synchronized(this.state_lock){
            while(this.state == null)
                try {
                    state_lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("Force shutdown of view thread.");
                }
            return this.client_state;
        }
        
    }

    public void setClientState(ClientState state){
        synchronized(this.state_lock){
            terminal.puts(Capability.clear_screen);
            this.client_state = state;
            state_lock.notifyAll();
        }
    }

    public void changeShip(String s){
        switch(s){
            case "red":
                this.selected_color = PlayerColor.RED;
                return;
            case "blue":
                this.selected_color = PlayerColor.BLUE;
                return;
            case "green":
                this.selected_color = PlayerColor.GREEN;
                return;
            case "yellow":
                this.selected_color = PlayerColor.YELLOW;
                return;
            default: return;
        }
    }

    @Override
    public void setInput(ServerMessage input){
        synchronized(this.input_lock){
            this.input = input;
            this.input_lock.notifyAll();
        }
    }

    @Override
    public Object getLock() {
        return this.input_lock;
    }

    @Override
    public boolean inputAvailable() {
        synchronized(this.input_lock){
            return this.input != null;
        }
    }

    @Override
    public ServerMessage getInput() {
        synchronized(this.input_lock){
            ServerMessage res = this.input;
            this.input = null;
            return res;
        }
    }

    @Override
    public void connect(ConnectedState state){
        this.inputthread.interrupt();
        this.inputthread = new ConnectedThread(terminal, this);
        this.inputthread.start();
        this.state = state;
    }

    @Override
    public void disconnect(){
        this.inputthread.interrupt();
        this.state = null;
        this.client_state = null;
    }

}
