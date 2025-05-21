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
    private final Object input_lock;
    private final Object line_lock;
    private final Object state_lock;
    private final Thread inputthread;
    private Thread line_thread;
    private Thread drawthread;

    private ConnectedState state;
    private PlayerColor selected_color;
    private ClientState client_state;
    
    private ServerMessage input;
    private String line;

    private boolean overlay;
    private Runnable screen_runnable;
    private Runnable status_runnable;
    private Runnable overlay_runnable;
    

    //XXX fix construction state message, add that when placing components, you dont lose it if the placement is invalid
    //And show cards message for construction state.
    //add info to verify state.
    //if cargopenalty is 0 nothing is needed
    //reconnecting with disconnected name doesn't reintegrate into correct notify state.
    //entering after a lobby has timedout doesnot properly work.
    //add that enter removes help screen

    public TUIView() throws IOException {
        this.terminal = new TerminalWrapper(this);
        this.inputthread = new KeyboardInputThread(terminal, this);
        inputthread.start();
        this.drawthread = new RedrawThread(this);
        drawthread.start();
        this.input_lock = new Object();
        this.state_lock = new Object();
        this.line_lock = new Object();
        this.screen_runnable = () -> {};
        this.status_runnable = () -> {};
    }

    public void redraw(){
        this.screen_runnable.run();
        this.terminal.print(" ".repeat(128), 31, 0);
        this.status_runnable.run();
        if(overlay) this.overlay_runnable.run();
    }

    @Override
    public void show(TitleScreenState state) {
        TitleScreenThread s =  new TitleScreenThread(state, this);
        this.line_thread = s;
        s.start();
        this.screen_runnable = () -> s.format(terminal);
    }

    @Override
    public void show(ConnectingState state) {
        this.line_thread.interrupt();
        ConnectingThread s = new ConnectingThread(state, this);
        this.line_thread = s;
        s.start();
        this.screen_runnable = () -> s.format(terminal);
    }

    @Override
    public void show(ClientLobbySelectState state) {
        this.selected_color = PlayerColor.NONE;
        this.screen_runnable = () -> ClientLobbyStatesFormatter.format(terminal, state);
        this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientSetupState state) {
        this.selected_color = PlayerColor.NONE;
        this.screen_runnable = () -> ClientLobbyStatesFormatter.format(terminal, state);
        this.status_runnable = () -> ClientLobbyStatesFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientWaitingRoomState state) {
        if(this.selected_color == PlayerColor.NONE) this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.screen_runnable = () -> ClientWaitingStateFormatter.format(terminal, state);
        this.status_runnable = () -> ClientWaitingStateFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientConstructionState state) {
        if(this.selected_color == PlayerColor.NONE) this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.screen_runnable = () -> ClientConstructionStateFormatter.format(terminal, state, selected_color);
        this.status_runnable = () -> ClientConstructionStateFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientVerifyState state) {
        if(this.selected_color == PlayerColor.NONE) this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.screen_runnable = () -> ClientVerifyStateFormatter.format(terminal, state, selected_color);
        this.status_runnable = () -> ClientVerifyStateFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientVoyageState state) {
        if(this.selected_color == PlayerColor.NONE) this.selected_color = state.getPlayerList().stream().filter(s->s.getUsername().equals(this.state.getUsername())).map(p->p.getColor()).findFirst().orElse(PlayerColor.NONE);
        this.screen_runnable = () -> ClientVoyageStateFormatter.format(terminal, state, selected_color);
        this.status_runnable = () -> ClientVoyageStateFormatter.formatStatus(terminal, state);
    }

    @Override
    public void show(ClientEndgameState state) {
        this.selected_color = PlayerColor.NONE;
        this.screen_runnable = () -> ClientEndingStateFormatter.format(terminal, state);
        this.status_runnable = () -> ClientEndingStateFormatter.formatStatus(terminal, state);
    }

    @Override
    public void showTextMessage(String message) {
        //TODO fix this.
        TextMessageFormatter.format(terminal, message);
    }

    public void showHelpScreen() {
        this.overlay = true;
        this.overlay_runnable = () -> HelpScreenFormatter.format(terminal);
        HelpScreenFormatter.format(terminal);
    }

    public void resetOverlay(){
        terminal.puts(Capability.clear_screen);
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

    public String takeLine(){
        synchronized(this.line_lock){
            while(this.line == null){
                try {
                    line_lock.wait();
                } catch (InterruptedException e) {
                    return "";
                }
            }
            String s = this.line;
            this.line = null;
            return s;
        }
    }

    public void setLine(String line){
        synchronized(this.line_lock){
            terminal.puts(Capability.clear_screen);
            this.line = line;
            line_lock.notifyAll();
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
        this.line_thread.interrupt();
        ConnectedThread s = new ConnectedThread(this);
        this.line_thread = s;
        s.start();
        this.state = state;
    }

    @Override
    public void disconnect(){
        this.line_thread.interrupt();
        this.status_runnable = () -> {};
        resetOverlay();
        this.state = null;
        this.client_state = null;
    }

}
