package it.polimi.ingsw.view.tui;

import java.io.IOException;
import java.util.List;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.utils.*;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private final Object input_lock;
    private PlayerColor selected_color;
    private ClientState client_state;
    private ServerMessage input;
    private Thread inputthread;
    private ConnectedState state;

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper();
        this.input_lock = new Object();
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
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        if(state.getLobbyList().size()==0) terminal.printCentered(List.of("No lobbies open yet!"));
        else terminal.printCenteredCorner(ClientLobbyStatesFormatter.format(state));
    }

    @Override
    public void show(ClientSetupState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        if(state.getUnfinishedList().size()==0) terminal.printCentered(List.of("No unfinished lobbies available!"));
        else terminal.printCentered(ClientLobbyStatesFormatter.format(state));
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        terminal.puts(Capability.clear_screen);
        terminal.printCentered(ClientWaitingStateFormatter.format(state));
    }

    @Override
    public void show(ClientConstructionState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        // terminal.printCenteredCorner(ClientConstructionStateFormatter.format(selected_color, state));
    }

    @Override
    public void show(ClientVerifyState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        // terminal.printCenteredCorner(ClientVerifyStateFormatter.format(selected_color, state));
    }

    @Override
    public void show(ClientVoyageState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        // terminal.printCenteredCorner(ClientVoyageStateFormatter.format(selected_color, state));
        // terminal.updateStatus(ClientVoyageStateFormatter.getBottom(state));
    }

    @Override
    public void show(ClientEndgameState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.client_state = state;
        terminal.puts(Capability.clear_screen);
        //terminal.printCentered(ClientEndgameStateFormatter.format(state));
        terminal.resetStatus();
    }

    @Override
    public void showTextMessage(String message) {
        if(state == null) throw new UnsupportedOperationException();
    }

    public ClientState getClientState(){
        return this.client_state;
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
