package it.polimi.ingsw.view.tui;

import java.io.IOException;

import org.jline.utils.InfoCmp.Capability;

import it.polimi.ingsw.controller.client.state.*;
import it.polimi.ingsw.message.server.ServerMessage;
import it.polimi.ingsw.model.client.state.*;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.tui.concurrent.*;
import it.polimi.ingsw.view.tui.formatters.*;

public class TUIView implements ClientView {

    private final TerminalWrapper terminal;
    private final Thread statusthread;
    private final Object input_lock;
    private final Object state_lock;
    private PlayerColor selected_color;
    private ClientState client_state;
    private Runnable status_runnable;
    private ServerMessage input;
    private Thread inputthread;
    
    private boolean overlay;
    private Runnable overlay_runnable;

    //XXX fix construction state message, add that when placing components, you dont lose it if the placement is invalid
    //And show cards message for construction state.
    //add info to verify state.
    //if cargopenalty is 0 nothing is needed
    //logger su give up e check che uno non sia gia' dentro


    private ConnectedState state;

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper(this);
        this.input_lock = new Object();
        this.state_lock = new Object();
        this.statusthread = new StatusUpdateThread(this);
        this.status_runnable = () -> {};
    }

    public void redraw(){
        //Check if null.
        terminal.puts(Capability.clear_screen);
        this.client_state.sendToView(this);
        this.status_runnable.run();
        if(overlay) this.overlay_runnable.run();
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
        this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
        ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientSetupState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
        ClientLobbyStatesFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.status_runnable = () -> ClientWaitingStateFormatter.formatStatus(terminal, state);
        ClientWaitingStateFormatter.format(terminal, state);
    }

    @Override
    public void show(ClientConstructionState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.status_runnable = () -> ClientConstructionStateFormatter.formatStatus(terminal, state);
        ClientConstructionStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVerifyState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.status_runnable = () -> ClientVerifyStateFormatter.formatStatus(terminal, state);
        ClientVerifyStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientVoyageState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.status_runnable = () -> ClientVoyageStateFormatter.formatStatus(terminal, state);
        ClientVoyageStateFormatter.format(terminal, state, selected_color);
    }

    @Override
    public void show(ClientEndgameState state) {
        if(state == null) throw new UnsupportedOperationException();
        this.status_runnable = () -> ClientEndingStateFormatter.formatStatus(terminal, state);
        ClientEndingStateFormatter.format(terminal, state);
    }

    @Override
    public void showTextMessage(String message) {
        if(state == null) throw new UnsupportedOperationException();
        TextMessageFormatter.format(terminal, message);
    }

    public void showHelpScreen() {
        this.overlay = true;
        this.overlay_runnable = () -> HelpScreenFormatter.format(terminal);
        HelpScreenFormatter.format(terminal);
    }

    public void resetOverlay(){
        this.overlay = false;
        this.overlay_runnable = null;
        this.redraw();
    }

    public Runnable getStatusRunnable(){
        return this.status_runnable;
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
            case "blue":
                this.selected_color = PlayerColor.BLUE;
            case "green":
                this.selected_color = PlayerColor.GREEN;
            case "yellow":
                this.selected_color = PlayerColor.YELLOW;
            default: this.selected_color = PlayerColor.NONE;
        }
        this.redraw();
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
        this.inputthread = new ConnectedKeyboardInputThread(terminal, this);
        this.inputthread.start();
        this.statusthread.start();
        this.state = state;
    }

    @Override
    public void disconnect(){
        this.statusthread.interrupt();
        this.inputthread.interrupt();
        this.state = null;
        this.client_state = null;
    }

    

}
